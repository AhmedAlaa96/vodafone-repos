package com.ahmed.vodafonerepos.data.models

import android.widget.ProgressBar

data class LoadingModel(
        val shouldShow: Boolean = false,
        val progressType: ProgressTypes = ProgressTypes.MAIN_PROGRESS,
        var loadingProgressView: ProgressBar? = null,
        var pagingProgressView: ProgressBar? = null,
)
