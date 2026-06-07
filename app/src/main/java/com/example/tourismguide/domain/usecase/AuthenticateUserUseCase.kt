package com.example.tourismguide.domain.usecase

import com.example.tourismguide.domain.repository.AuthRepository
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String) = authRepository.signInWithEmail(email, password)
}
