package com.vlg.athletica.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vlg.athletica.data.remote.HandleResponse
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.data.repository.UserRepository
import com.vlg.athletica.model.User
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository, private val handleResponse: HandleResponse<User>) : ViewModel() {

    fun login(email: String, password: String) = viewModelScope.launch {
        handleResponse(userRepository.login(email, password))
    }

    fun register(user: User) = viewModelScope.launch {
        handleResponse(userRepository.createUser(user))
    }

    private fun handleResponse(response: Resource<User>) {
        if (response is Resource.Failure) {
            handleResponse.error(response.exception)
        } else {
            handleResponse.success((response as Resource.Success).result)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val userRepository: UserRepository, private val handleResponse: HandleResponse<User>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(userRepository, handleResponse) as T
    }
}