package com.vlg.athletica.data.remote

import com.vlg.athletica.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("api-v1/users/{id}")
    suspend fun getUser(@Path("id") id: Long): Response<User>

    @PUT("/change")
    suspend fun changeUsername(@Query("newNik") newNik: String, @Query("id") id: Long): Response<User>
}