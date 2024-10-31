package com.vlg.athletica.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Review(

    @SerializedName("id")
    @Expose
    val idReview: Long,

    @SerializedName("rating")
    @Expose
    val rating: Int,

    @SerializedName("comment")
    @Expose
    val comment: String,

    @SerializedName("dateCreated")
    @Expose
    val dateCreated: String
)
