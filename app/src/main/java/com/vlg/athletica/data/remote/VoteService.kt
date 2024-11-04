package com.vlg.athletica.data.remote

import com.vlg.athletica.model.Vote
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VoteService {

    @GET("api-v1/votes/search/countComeUser")
    suspend fun getCountComeUserBySlot(@Query("slotId") slotId: Long): Response<Int>

    @POST("api-v1/votes")
    suspend fun saveVote(@Body vote: Vote): Response<Unit>

    @DELETE("http://127.0.0.1:8080/api-v1/votes/deleteByUserIdAndTimeSlotId")
    suspend fun deleteVote(@Query("userId") userId: Long, @Query("timeSlotId") timeSlotId: Long)

    @GET("api-v1/votes/search/getVoteByUserIdAndTimeSlotId")
    suspend fun getVoteByUserIdAnTimeSlotId(@Query("userId") userId: Long, @Query("timeSlotId") timeSlotId: Long): Response<Vote>
}