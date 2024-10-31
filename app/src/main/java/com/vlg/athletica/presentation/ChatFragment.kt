package com.vlg.athletica.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vlg.athletica.R
import com.vlg.athletica.databinding.FragmentChatBinding
import com.vlg.athletica.databinding.FragmentMapBinding

class ChatFragment : BaseFragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sendButton = binding.sendButton
        val message = binding.messageEditText
        var messagesText = ""

    }

}