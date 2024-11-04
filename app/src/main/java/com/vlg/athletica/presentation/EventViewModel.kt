package com.vlg.athletica.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.data.repository.EventRepository
import com.vlg.athletica.model.Event
import ir.logicbase.livex.SingleLiveEvent
import kotlinx.coroutines.launch

class EventViewModel(private val eventRepository: EventRepository): ViewModel() {

    var eventLiveData: SingleLiveEvent<Resource<Unit>> = SingleLiveEvent()

    fun saveEvent(event: Event) = viewModelScope.launch {
        eventLiveData.postValue(eventRepository.addSlot(event))
    }
}

@Suppress("UNCHECKED_CAST")
class EventViewModelFactory(private val eventRepository: EventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EventViewModel(eventRepository) as T
    }
}