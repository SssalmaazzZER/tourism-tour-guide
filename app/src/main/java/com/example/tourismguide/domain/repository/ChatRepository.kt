package com.example.tourismguide.domain.repository

import com.example.tourismguide.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun sendMessage(bookingId: String, senderId: String, text: String, imageUrl: String?)
    fun listenToMessages(bookingId: String, beforeMessageId: String? = null): Flow<List<ChatMessage>>
    suspend fun markMessagesRead(bookingId: String, readerId: String)
}
