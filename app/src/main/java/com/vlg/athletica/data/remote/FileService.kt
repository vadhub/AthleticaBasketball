package com.vlg.athletica.data.remote

import com.vlg.athletica.model.Image
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileService {

    @Multipart
    @POST("/api-v1/upload/icon")
    suspend fun uploadIcon(
        @Part file: MultipartBody.Part,
        @Part("dateCreated") dateCreated: RequestBody,
        @Part("dateChanged") dateChanged: RequestBody,
        @Part("userId") userId: RequestBody
    ) : Response<Image>
}