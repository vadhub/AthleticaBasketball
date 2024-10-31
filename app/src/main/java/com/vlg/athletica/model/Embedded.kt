package com.vlg.athletica.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Embedded(
    @SerializedName("spots")
    @Expose
    val spotResponses: List<SpotResponse>,

    )