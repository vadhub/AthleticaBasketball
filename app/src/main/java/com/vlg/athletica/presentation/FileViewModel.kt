package com.vlg.athletica.presentation

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.imageview.ShapeableImageView
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.data.repository.FileRepository
import com.vlg.athletica.model.Image
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel(private val fileRepository: FileRepository) : ViewModel() {

    val uploadIcon: MutableLiveData<Resource<Image>> = MutableLiveData()

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.printStackTrace()
    }

    fun getIcon(userId: Long, imageIcon: ShapeableImageView) {
        fileRepository.getIcon(userId, imageIcon, false)
    }

    fun invalidate(userId: Long, imageIcon: ShapeableImageView) {
        fileRepository.getIcon(userId, imageIcon, true)
    }

    fun uploadIcon(context: Context, file: File, userId: Long) = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

        uploadIcon.postValue(Resource.Loading)

        val compressImage = Compressor.compress(context, file) {
            quality(40)
            size(500_000)
        }

        uploadIcon.postValue(fileRepository.uploadIcon(compressImage, userId))
    }

}

@Suppress("UNCHECKED_CAST")
class LoadViewModelFactory(private val repository: FileRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FileViewModel(repository) as T
    }
}