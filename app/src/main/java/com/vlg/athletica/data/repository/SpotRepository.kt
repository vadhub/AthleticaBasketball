package com.vlg.athletica.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.vlg.athletica.data.remote.RemoteInstance
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.model.SpotResponse

class SpotRepository(private val remoteInstance: RemoteInstance) {
    suspend fun getSpots() : MutableLiveData<List<SpotResponse>> = MutableLiveData(
        remoteInstance.apiSpots().getSpots().body()?.embedded?.spotResponses
    )

    suspend fun addSpot(spotResponse: SpotResponse): Resource<Unit> {
        val response = remoteInstance.apiSpots()
            .addSpot(spotResponse)

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Resource.Success(body)
        }

        Log.d("$$$$$$", spotResponse.toString())

        return Resource.Failure(Exception("fail"))
    }

    suspend fun getSpotByLatAndLon(lat: String, lon: String) : Resource<SpotResponse> {

        val response = remoteInstance.apiSpots().getSpotByLatAndLon(lat, lon)

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Resource.Success(body)
        }

        return Resource.Failure(Exception("fail"))
    }

}