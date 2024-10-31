package com.vlg.athletica.presentation.auth.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.vlg.athletica.R
import com.vlg.athletica.databinding.FragmentRegistrationBinding
import com.vlg.athletica.model.User
import com.vlg.athletica.presentation.auth.AuthBaseFragment
import com.vlg.athletica.presentation.auth.Validator

class RegistrationFragment : AuthBaseFragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val buttonRegistration: Button = binding.enterRegistrationButton
        val buttonLogin: Button = binding.loginRegistrationButton
        val username = binding.nikEditText
        val firstname = binding.firstNameEditText
        val lastname = binding.lastNameEditText
        val email = binding.emailEditText
        val password = binding.passwordEditText

        buttonRegistration.setOnClickListener {

            if (username.text.isNullOrBlank()) {
                username.error = getString(R.string.field_empty)
                Toast.makeText(thisContext, getString(R.string.enter_username), Toast.LENGTH_SHORT).show()
            } else if (!Validator.emailValidator(email.text.toString())) {
                email.error = getString(R.string.valid_email)
                Toast.makeText(thisContext, getString(R.string.valid_email), Toast.LENGTH_SHORT).show()
            } else if (password.text.isNullOrBlank()) {
                password.error = getString(R.string.field_empty)
                Toast.makeText(thisContext, getString(R.string.enter_password), Toast.LENGTH_SHORT).show()
            } else {
                qwrt = password.text?.trim().toString()

                authViewModel.register(
                    User(
                        0,
                        username.text?.trim().toString(),
                        firstname.text?.trim().toString(),
                        lastname.text?.trim().toString(),
                        email.text?.trim().toString(),
                        password.text?.trim().toString()
                    )
                )
            }

        }

        buttonLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
    }

}