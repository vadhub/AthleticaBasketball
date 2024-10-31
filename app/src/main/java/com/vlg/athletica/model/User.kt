package com.vlg.athletica.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("id")
    @Expose
    val userId: Long,

    @SerializedName("nickname")
    @Expose
    val nickname: String,

    @SerializedName("firstname")
    @Expose
    val firstname: String,

    @SerializedName("lastname")
    @Expose
    val lastname: String,

    @SerializedName("email")
    @Expose
    val email: String,

    @SerializedName("password")
    @Expose
    val password: String
)