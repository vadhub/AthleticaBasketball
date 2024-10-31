package com.vlg.athletica.data.repository

import android.widget.ImageView
import com.vlg.athletica.data.remote.RemoteInstance
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.model.Image
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.sql.Date


class FileRepository(private val remoteInstance: RemoteInstance) {

    suspend fun uploadIcon(icon: File, userId: Long): Resource<Image> {

        val requestIcon: RequestBody =
            icon.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData(
                "file",
                "${icon.name}${System.currentTimeMillis()}",
                requestIcon
            )

        val dateCreated: RequestBody =
            "${Date(System.currentTimeMillis())}".toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val dateChanged: RequestBody =
            "${Date(System.currentTimeMillis())}".toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val userIdL: RequestBody =
            "$userId".toRequestBody("multipart/form-data".toMediaTypeOrNull())

        val response =
            remoteInstance.apiFileHandle().uploadIcon(body, dateCreated, dateChanged, userIdL)

        if (response.isSuccessful) {
            return Resource.Success(response.body()!!)
        }

        return Resource.Failure(IllegalAccessException("bad request"))
    }

    fun getIcon(userId: Long, imageView: ImageView, invalidate: Boolean) {
        remoteInstance.apiIcon(imageView, userId, invalidate)
    }
}