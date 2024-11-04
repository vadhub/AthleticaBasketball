package com.vlg.athletica.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Event(

    @SerializedName("id")
    @Expose
    val eventId: Int,

    @SerializedName("organizerId")
    @Expose
    val organizerId: Long,

    @SerializedName("timeSlotOne")
    @Expose
    val timeSlot: TimeSlot,

    @SerializedName("name")
    @Expose
    val name: String,

    @SerializedName("participantLimit")
    @Expose
    val participantLimit: Int,

    @SerializedName("description")
    @Expose
    val description: String,

)
