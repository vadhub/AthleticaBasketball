package com.vlg.athletica.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TimeSlot(

    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("spotId")
    @Expose
    val spotId: Long,

    @SerializedName("startTime")
    @Expose
    val startTime: String,

    @SerializedName("endTime")
    @Expose
    val endTime: String,

    @SerializedName("availability")
    @Expose
    val availability: Int
    ) {
    companion object {
        fun empty() = TimeSlot(-1, -1,"", "", -1)
    }
}
