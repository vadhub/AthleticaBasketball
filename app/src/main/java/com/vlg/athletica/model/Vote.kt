package com.vlg.athletica.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Vote(
    @SerializedName("userId")
    @Expose
    val userId: Long,

    @SerializedName("timeSlotId")
    @Expose
    val timeSlotId: Long,

    @SerializedName("isCome")
    @Expose
    val isCome: Int,
)
