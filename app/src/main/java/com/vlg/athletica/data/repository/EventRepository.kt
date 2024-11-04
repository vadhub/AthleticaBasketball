package com.vlg.athletica.data.repository

import com.vlg.athletica.data.remote.RemoteInstance
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.model.Event

class EventRepository(private val remoteInstance: RemoteInstance) {
    suspend fun addSlot(event: Event): Resource<Unit> {
        val response = remoteInstance.apiEvents()
            .addEvent(event)

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Resource.Success(body)
        }

        return Resource.Failure(Exception("fail"))
    }
}