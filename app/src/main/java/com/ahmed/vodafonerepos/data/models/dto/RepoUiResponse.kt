package com.ahmed.vodafonerepos.data.models.dto


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class RepoUiResponse(
    val totalCount: Int = 0,
    val reposList: ArrayList<RepoResponse>? = null
): Parcelable