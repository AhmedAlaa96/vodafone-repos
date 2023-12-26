package com.ahmed.vodafonerepos.data.models.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class RepoDetailsRequest(
    val owner: String,
    val repo: String
): Parcelable
