package com.vlg.athletica.presentation.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.vlg.athletica.R
import com.vlg.athletica.databinding.FragmentLoginBinding
import com.vlg.athletica.presentation.auth.AuthBaseFragment

class LoginFragment : AuthBaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val buttonLogin: Button = binding.loginButton
        val buttonRegistration: Button = binding.loginRegistrationButton
        val email = binding.emailLoginEditText
        val password = binding.passwordLoginEditText

        buttonRegistration.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }

        buttonLogin.setOnClickListener {

            if (email.text.isNullOrBlank()) {
                email.error = getString(R.string.field_empty)
                Toast.makeText(thisContext, getString(R.string.enter_mail), Toast.LENGTH_SHORT).show()
            } else if (password.text.isNullOrBlank()) {
                password.error = getString(R.string.field_empty)
                Toast.makeText(thisContext, getString(R.string.enter_password), Toast.LENGTH_SHORT).show()
            } else {
                qwrt = password.text.toString().trim()
                authViewModel.login(email.text.toString().trim(), qwrt)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}