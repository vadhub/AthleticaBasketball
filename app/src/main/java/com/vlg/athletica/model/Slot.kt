package com.vlg.athletica.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Slot(

    @SerializedName("id")
    @Expose
    val idSlot: Long,

    @SerializedName("startTime")
    @Expose
    val startTime: String,

    @SerializedName("endTime")
    @Expose
    val endTime: String,

    @SerializedName("availability")
    @Expose
    val availability: Int
    )
