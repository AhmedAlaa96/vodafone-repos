package com.ahmed.vodafonerepos.data.models.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Reactions(
    @SerializedName("confused")
    val confused: Int? = null,
    @SerializedName("eyes")
    val eyes: Int? = null,
    @SerializedName("heart")
    val heart: Int? = null,
    @SerializedName("hooray")
    val hooray: Int? = null,
    @SerializedName("laugh")
    val laugh: Int? = null,
    @SerializedName("rocket")
    val rocket: Int? = null,
    @SerializedName("total_count")
    val totalCount: Int? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("+1")
    val reactionPlus1: Int? = null,
    @SerializedName("-1")
    val reactionMinus1: Int? = null
)