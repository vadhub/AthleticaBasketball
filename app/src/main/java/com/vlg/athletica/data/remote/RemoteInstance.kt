package com.vlg.athletica.data.remote

import android.content.Context
import android.widget.ImageView
import com.google.gson.GsonBuilder
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.vlg.athletica.R
import com.vlg.athletica.model.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RemoteInstance {

    var user: User = User(-1, "", "", "", "", "")
        private set

    fun setUser(user: User) {
        this.user = user
    }

    private const val baseUrl: String = "http://10.0.2.2:8080"  //"http://10.0.2.2:8090/"

    private val interceptorBody: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun basicAuthInterceptor(username: String, password: String): Interceptor {
        return BasicAuthInterceptor(username, password)
    }

    private fun client(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(interceptorBody)
            .build()

    private val gson = GsonBuilder().setLenient().create()

    private fun retrofitBase(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))

    fun setPicasso(context: Context) {
        try {
            val picasso = Picasso.Builder(context)
                .memoryCache(LruCache(context))
                .downloader(
                    OkHttp3Downloader(
                        client(basicAuthInterceptor(user.email, user.password))
                    )
                ).build()
            Picasso.setSingletonInstance(picasso)
        } catch (_: IllegalStateException) {
        }

    }

    fun apiIcon(imageView: ImageView, userId: Long, invalidate: Boolean) {

        val picasso = Picasso.get()

        if (invalidate) {
            picasso.invalidate("${baseUrl}api-v1/files/icon/search?userId=$userId")
        } else {
            Picasso.get()
                .load("${baseUrl}api-v1/files/icon/search?userId=$userId")
                .error(R.drawable.account_circle_24dp_ffffff_fill0_wght200_grad0_opsz24)
                .into(imageView)
        }
    }

    private fun retrofitWithAuth(): Retrofit.Builder =
        retrofitBase().client(client(basicAuthInterceptor(user.email, user.password)))

    private fun retrofitForLogin(username: String, password: String): Retrofit.Builder =
        retrofitBase().client(client(basicAuthInterceptor(username, password)))

    fun apiUser(): UserService {
        return retrofitWithAuth().build().create(UserService::class.java)
    }

    fun apiFileHandle(): FileService =
        retrofitWithAuth().build().create(FileService::class.java)

    fun userRegistration(): AuthService =
        retrofitBase().build().create(AuthService::class.java)

    fun userLogin(username: String, password: String): AuthService =
        retrofitForLogin(username, password).build().create(AuthService::class.java)

    fun apiSpots(): SpotService = retrofitWithAuth().build().create(SpotService::class.java)
    fun apiSlots(): SlotService = retrofitWithAuth().build().create(SlotService::class.java)
    fun apiEvents(): EventService = retrofitWithAuth().build().create(EventService::class.java)
    fun apiVotes(): VoteService = retrofitWithAuth().build().create(VoteService::class.java)

}