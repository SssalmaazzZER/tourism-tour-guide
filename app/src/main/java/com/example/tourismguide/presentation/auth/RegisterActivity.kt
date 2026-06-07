package com.example.tourismguide.presentation.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tourismguide.R
import com.example.tourismguide.databinding.ActivityRegisterBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : LocalizedActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        binding.buttonRegister.setOnClickListener {
            val password = binding.inputPassword.editText?.text.toString()
            val confirm = binding.inputConfirmPassword.editText?.text.toString()
            if (password != confirm) {
                Snackbar.make(binding.root, R.string.error_passwords_do_not_match, Snackbar.LENGTH_LONG).show()
            } else {
                viewModel.register(binding.inputName.editText?.text.toString(), binding.inputEmail.editText?.text.toString(), password)
            }
        }
        binding.textLogin.setOnClickListener { finish() }
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state is AuthUiState.Loading
                    when (state) {
                        AuthUiState.Success -> finish()
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
