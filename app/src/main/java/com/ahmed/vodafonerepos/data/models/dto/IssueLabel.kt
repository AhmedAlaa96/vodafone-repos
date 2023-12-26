package com.ahmed.vodafonerepos.data.models.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class IssueLabel(
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("default")
    val default: Boolean? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("node_id")
    val nodeId: String? = null,
    @SerializedName("url")
    val url: String? = null
)