package com.example.tourismguide.data.repository

import com.example.tourismguide.domain.model.ChatMessage
import com.example.tourismguide.domain.repository.ChatRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/*
Firestore rules: chat_messages read/write only if auth.uid is a participant of the booking.
*/
class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatRepository {
    override suspend fun sendMessage(bookingId: String, senderId: String, text: String, imageUrl: String?) {
        val ref = messagesRef(bookingId).document()
        ref.set(
            mapOf(
                "id" to ref.id,
                "bookingId" to bookingId,
                "senderId" to senderId,
                "senderName" to senderId,
                "text" to text,
                "imageUrl" to imageUrl,
                "sentAt" to Timestamp.now(),
                "isRead" to false
            )
        ).await()
    }

    override fun listenToMessages(bookingId: String, beforeMessageId: String?): Flow<List<ChatMessage>> = callbackFlow {
        var query: Query = messagesRef(bookingId).orderBy("sentAt").limitToLast(50)
        if (beforeMessageId != null) {
            runCatching { messagesRef(bookingId).document(beforeMessageId).get().await() }.getOrNull()?.let {
                query = messagesRef(bookingId).orderBy("sentAt").endBefore(it).limitToLast(50)
            }
        }
        val listener = query.addSnapshotListener { snapshot, _ ->
            trySend(snapshot?.documents.orEmpty().mapNotNull { it.toObject(ChatMessage::class.java) })
        }
        awaitClose { listener.remove() }
    }

    override suspend fun markMessagesRead(bookingId: String, readerId: String) {
        val unread = messagesRef(bookingId).whereNotEqualTo("senderId", readerId).whereEqualTo("isRead", false).get().await()
        unread.documents.forEach { it.reference.update(mapOf("isRead" to true, "readAt" to FieldValue.serverTimestamp())).await() }
    }

    private fun messagesRef(bookingId: String) =
        firestore.collection("chat_messages").document(bookingId).collection("messages")
}
