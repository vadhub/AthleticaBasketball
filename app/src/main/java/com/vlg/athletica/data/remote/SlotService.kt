package com.vlg.athletica.data.remote

import com.vlg.athletica.model.Slot
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SlotService {
    @POST("api-v1/spots")
    suspend fun addSlot(@Body slot: Slot) : Response<Unit>
}