package com.example.tourismguide.presentation.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.repository.AuthRepository
import com.example.tourismguide.domain.usecase.AuthenticateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authenticateUserUseCase(email, password).collect { result -> _uiState.value = result.toUiState() }
        }
    }

    fun loginWithGoogle(data: Intent?) {
        viewModelScope.launch {
            authRepository.signInWithGoogle(data).collect { result -> _uiState.value = result.toUiState() }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            authRepository.register(name, email, password).collect { result -> _uiState.value = result.toUiState() }
        }
    }

    fun reset() {
        _uiState.value = AuthUiState.Idle
    }

    private fun NetworkResult<*>.toUiState(): AuthUiState = when (this) {
        NetworkResult.Loading -> AuthUiState.Loading
        is NetworkResult.Success -> AuthUiState.Success
        is NetworkResult.Error -> AuthUiState.Error(message)
    }
}
