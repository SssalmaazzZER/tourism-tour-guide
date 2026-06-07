package com.example.tourismguide.domain.repository

import android.content.Intent
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signInWithEmail(email: String, password: String): Flow<NetworkResult<AuthUser>>
    fun signInWithGoogle(data: Intent?): Flow<NetworkResult<AuthUser>>
    fun register(name: String, email: String, password: String): Flow<NetworkResult<AuthUser>>
    suspend fun signOut()
    suspend fun getCurrentUser(): AuthUser?
    fun isLoggedIn(): Flow<Boolean>
}
