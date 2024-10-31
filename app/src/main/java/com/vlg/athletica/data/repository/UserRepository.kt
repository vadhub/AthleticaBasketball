package com.vlg.athletica.data.repository

import com.vlg.athletica.data.remote.RemoteInstance
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.data.remote.UnauthorizedException
import com.vlg.athletica.data.remote.UserAlreadyExistException
import com.vlg.athletica.data.remote.UserNotFoundException
import com.vlg.athletica.model.User

class UserRepository(private val retrofitInstance: RemoteInstance) {

    suspend fun getUserById(id: Long): User? =
        retrofitInstance.apiUser().getUser(id).body()

    suspend fun login(email: String, password: String): Resource<User> {
        val response = retrofitInstance.userLogin(email, password).login(email)
        if (response.code() == 401) {
            return Resource.Failure(UnauthorizedException("user unauthorized"))
        } else if (response.code() == 409) {
            return Resource.Failure(UserNotFoundException("user not found"))
        }

        return Resource.Success(response.body()!!)
    }

    suspend fun createUser(user: User) : Resource<User> {
        val response = retrofitInstance.userRegistration().registration(user)

        if (response.code() == 401) {
            return Resource.Failure(UnauthorizedException("user unauthorized"))
        } else if (response.code() == 409) {
            return Resource.Failure(UserAlreadyExistException("user already exist"))
        }

        return Resource.Success(response.body()!!)
    }

    suspend fun changeUsername(newNik: String, id: Long) : Resource<User> {
        val response = retrofitInstance.apiUser().changeUsername(newNik, id)

        if (response.code() == 401) {
            return Resource.Failure(UnauthorizedException("user unauthorized"))
        } else if (response.code() == 409) {
            return Resource.Failure(UserAlreadyExistException("user already exist"))
        }

        return Resource.Success(response.body()!!)
    }
}