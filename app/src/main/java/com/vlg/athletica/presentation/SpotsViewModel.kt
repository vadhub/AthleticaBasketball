package com.vlg.athletica.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.data.repository.SpotRepository
import com.vlg.athletica.model.Slot
import com.vlg.athletica.model.SpotResponse
import ir.logicbase.livex.SingleLiveEvent
import kotlinx.coroutines.launch

class SpotsViewModel(private val spotRepository: SpotRepository): ViewModel() {

    var spots: MutableLiveData<List<SpotResponse>> = MutableLiveData()
        private set

    var spotResponse: SingleLiveEvent<Resource<Unit>> = SingleLiveEvent()
    var spot: MutableLiveData<Resource<SpotResponse>> = MutableLiveData()

    fun getSpots() = viewModelScope.launch {
        spots.postValue(spotRepository.getSpots().value)
    }

    fun saveSpot(spotResponse: SpotResponse) = viewModelScope.launch {
        this@SpotsViewModel.spotResponse.postValue(spotRepository.addSpot(spotResponse))
    }

    fun saveSlot(slot: Slot) = viewModelScope.launch {
        this@SpotsViewModel.spotResponse.postValue(spotRepository.addSlot(slot))
    }

    fun getSpotByLatAndLon(lat: String, lon: String) = viewModelScope.launch {
        spot.postValue(spotRepository.getSpotByLatAndLon(lat, lon))
    }
}

@Suppress("UNCHECKED_CAST")
class SpotsViewModelFactory(private val spotRepository: SpotRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SpotsViewModel(spotRepository) as T
    }
}