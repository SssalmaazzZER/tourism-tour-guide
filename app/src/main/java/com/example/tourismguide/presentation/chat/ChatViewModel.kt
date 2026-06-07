package com.example.tourismguide.presentation.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.domain.repository.ChatRepository
import com.example.tourismguide.domain.usecase.SendChatMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val chatRepository: ChatRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {
    val bookingId: String = savedStateHandle["bookingId"] ?: ""
    private val _currentUserId = MutableStateFlow("")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _currentUserId.value = dataStoreManager.userId.first().orEmpty()
            chatRepository.listenToMessages(bookingId).collect { _uiState.value = ChatUiState(messages = it, loading = false) }
        }
    }

    fun sendMessage(text: String, imageUrl: String? = null) {
        viewModelScope.launch {
            val senderId = _currentUserId.value.ifBlank { dataStoreManager.userId.first().orEmpty() }
            sendChatMessageUseCase(bookingId, senderId, text, imageUrl)
        }
    }

    fun markRead() {
        viewModelScope.launch {
            val userId = _currentUserId.value.ifBlank { dataStoreManager.userId.first().orEmpty() }
            chatRepository.markMessagesRead(bookingId, userId)
        }
    }
}
