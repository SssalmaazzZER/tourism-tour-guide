package com.example.tourismguide.domain.usecase

import com.example.tourismguide.domain.repository.ChatRepository
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(bookingId: String, senderId: String, text: String, imageUrl: String?) {
        if (text.isBlank() && imageUrl.isNullOrBlank()) return
        chatRepository.sendMessage(bookingId, senderId, text.trim(), imageUrl)
    }
}
