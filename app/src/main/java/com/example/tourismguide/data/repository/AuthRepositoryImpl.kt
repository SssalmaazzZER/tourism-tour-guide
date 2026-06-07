package com.example.tourismguide.data.repository

import android.content.Intent
import com.example.tourismguide.data.preferences.DataStoreManager
import com.example.tourismguide.data.remote.NetworkResult
import com.example.tourismguide.domain.model.AuthUser
import com.example.tourismguide.domain.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataStoreManager: DataStoreManager
) : AuthRepository {
    override fun signInWithEmail(email: String, password: String): Flow<NetworkResult<AuthUser>> = flow {
        emit(NetworkResult.Loading)
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        emit(NetworkResult.Success(result.user!!.toAuthUserAndPersist()))
    }

    override fun signInWithGoogle(data: Intent?): Flow<NetworkResult<AuthUser>> = flow {
        emit(NetworkResult.Loading)
        val account = GoogleSignIn.getSignedInAccountFromIntent(data).await()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        emit(NetworkResult.Success(result.user!!.toAuthUserAndPersist()))
    }

    override fun register(name: String, email: String, password: String): Flow<NetworkResult<AuthUser>> = flow {
        emit(NetworkResult.Loading)
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        emit(NetworkResult.Success(result.user!!.toAuthUserAndPersist(name)))
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
        dataStoreManager.clearSession()
    }

    override suspend fun getCurrentUser(): AuthUser? = firebaseAuth.currentUser?.toAuthUserAndPersist()

    override fun isLoggedIn(): Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser != null) }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    private suspend fun com.google.firebase.auth.FirebaseUser.toAuthUserAndPersist(nameOverride: String? = null): AuthUser {
        val token = getIdToken(false).await().token
        if (!token.isNullOrBlank()) dataStoreManager.setSessionToken(token, uid)
        return AuthUser(uid, nameOverride ?: displayName, email, token)
    }
}
