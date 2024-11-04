package com.vlg.athletica.data.remote

import com.vlg.athletica.model.Event
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EventService {

    @POST("api-v1/addEvent")
    suspend fun addEvent(@Body event: Event) : Response<Unit>

}