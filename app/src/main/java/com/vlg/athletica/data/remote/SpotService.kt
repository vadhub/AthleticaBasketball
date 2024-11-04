package com.vlg.athletica.data.remote

import com.vlg.athletica.model.Main
import com.vlg.athletica.model.SpotResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SpotService {

    @GET("api-v1/spots?projection=spotWithSlots")
    suspend fun getSpots() : Response<Main>

    @POST("api-v1/spots")
    suspend fun addSpot(@Body spotResponse: SpotResponse) : Response<Unit>

    @GET("api-v1/spots/search/findByLatAndLon")
    suspend fun getSpotByLatAndLon(@Query("lat") lat: String, @Query("lon") lon: String) : Response<SpotResponse>
}