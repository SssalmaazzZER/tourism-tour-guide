package com.example.tourismguide.presentation.chat

import com.example.tourismguide.domain.model.ChatMessage

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)
