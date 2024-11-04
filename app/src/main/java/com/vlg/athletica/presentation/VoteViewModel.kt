package com.vlg.athletica.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.data.repository.VoteRepository
import com.vlg.athletica.model.Vote
import ir.logicbase.livex.SingleLiveEvent
import kotlinx.coroutines.launch

class VoteViewModel(private val voteRepository: VoteRepository): ViewModel() {

    var voteLiveData: SingleLiveEvent<Resource<Unit>> = SingleLiveEvent()
    var voteOneLiveData: SingleLiveEvent<Vote> = SingleLiveEvent()
    var voteCountLiveData: SingleLiveEvent<Int> = SingleLiveEvent()

    fun saveVote(vote: Vote) = viewModelScope.launch {
        voteLiveData.postValue(voteRepository.addVote(vote))
    }

    fun getCountVoteBySlotId(slotId: Long) = viewModelScope.launch {
        voteCountLiveData.postValue(voteRepository.getCountVoteBySlotId(slotId))
    }

    fun removeVote(userId: Long, slotId: Long) = viewModelScope.launch {
        voteRepository.removeVote(userId, slotId)
    }

    fun getVote(userId: Long, slotId: Long) = viewModelScope.launch {
        voteOneLiveData.postValue(voteRepository.getVoteByUserIdAnTimeSlotId(userId, slotId))
    }
}

@Suppress("UNCHECKED_CAST")
class VoteViewModelFactory(private val voteRepository: VoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VoteViewModel(voteRepository) as T
    }
}