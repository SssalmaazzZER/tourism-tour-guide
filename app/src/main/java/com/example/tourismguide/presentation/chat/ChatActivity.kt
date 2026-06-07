package com.example.tourismguide.presentation.chat

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tourismguide.databinding.ActivityChatBinding
import com.example.tourismguide.presentation.common.LocalizedActivity
import com.example.tourismguide.util.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatActivity : LocalizedActivity() {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter
    private val picker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            lifecycleScope.launch {
                ImageUtils.uploadToFirebaseStorage(
                    it,
                    "chat/${viewModel.bookingId}/${System.currentTimeMillis()}.jpg"
                ).onSuccess { url ->
                    viewModel.sendMessage("", url)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyEdgeToEdge(binding.root)
        adapter = ChatAdapter("")
        binding.recyclerMessages.adapter = adapter
        binding.recyclerMessages.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        binding.buttonSend.setOnClickListener { viewModel.sendMessage(binding.inputMessage.editText?.text.toString()) }
        binding.buttonAttach.setOnClickListener { picker.launch("image/*") }
        observe()
    }

    override fun onResume() {
        super.onResume()
        viewModel.markRead()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currentUserId.collect { userId ->
                        val currentMessages = if (::adapter.isInitialized) adapter.currentList else emptyList()
                        adapter = ChatAdapter(userId)
                        binding.recyclerMessages.adapter = adapter
                        adapter.submitList(currentMessages)
                    }
                }
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.loading
                    adapter.submitList(state.messages)
                    binding.recyclerMessages.scrollToPosition((state.messages.size - 1).coerceAtLeast(0))
                }
            }
        }
    }
}
