package com.vlg.athletica.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SpotResponse(
    @SerializedName("id")
    @Expose
    val idSpot: Long,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("lat")
    @Expose
    val lat: String,

    @SerializedName("lon")
    @Expose
    val lon: String,

    @SerializedName("address")
    @Expose
    val address: String,

    @SerializedName("reviews")
    @Expose
    val reviews: List<Review>?,

    @SerializedName("slots")
    @Expose
    val slots: List<Slot>?
)