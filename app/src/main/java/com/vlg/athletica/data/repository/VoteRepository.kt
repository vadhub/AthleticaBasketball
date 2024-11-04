package com.vlg.athletica.data.repository

import com.vlg.athletica.data.remote.RemoteInstance
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.model.Vote

class VoteRepository (private val remoteInstance: RemoteInstance) {
    suspend fun addVote(vote: Vote): Resource<Unit> {
        val response = remoteInstance.apiVotes()
            .saveVote(vote)

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Resource.Success(body)
        }

        return Resource.Failure(Exception("fail"))
    }

    suspend fun getCountVoteBySlotId(slotId: Long): Int =
        Resource.Success(remoteInstance.apiVotes().getCountComeUserBySlot(slotId).body()).result?: 0

    suspend fun removeVote(idUser: Long, idTimeSlot: Long) {
        remoteInstance.apiVotes().deleteVote(idUser, idTimeSlot)
    }

    suspend fun getVoteByUserIdAnTimeSlotId(idUser: Long, idTimeSlot: Long): Vote =
        Resource.Success(remoteInstance.apiVotes().getVoteByUserIdAnTimeSlotId(idUser, idTimeSlot).body()).result ?: Vote(-1, -1, -1)

}