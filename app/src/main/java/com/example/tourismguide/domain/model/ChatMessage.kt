package com.example.tourismguide.domain.model

import com.google.firebase.Timestamp

data class ChatMessage(
    val id: String = "",
    val bookingId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val imageUrl: String? = null,
    val sentAt: Timestamp = Timestamp.now(),
    val isRead: Boolean = false
)
