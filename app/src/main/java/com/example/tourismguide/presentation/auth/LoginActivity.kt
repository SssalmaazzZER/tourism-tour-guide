package com.example.tourismguide.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ActivityLoginBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.presentation.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : LocalizedActivity() {
    @Inject lateinit var googleSignInClient: GoogleSignInClient
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private val googleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        viewModel.loginWithGoogle(it.data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        binding.buttonLogin.setOnClickListener {
            viewModel.login(binding.inputEmail.editText?.text.toString(), binding.inputPassword.editText?.text.toString())
        }
        binding.buttonGoogle.setOnClickListener { googleLauncher.launch(googleSignInClient.signInIntent) }
        binding.textRegister.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state is AuthUiState.Loading
                    when (state) {
                        AuthUiState.Success -> {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                        is AuthUiState.Error -> {
                            Snackbar.make(binding.root, state.message.ifBlank { getString(R.string.error_generic) }, Snackbar.LENGTH_LONG).show()
                            viewModel.reset()
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}
