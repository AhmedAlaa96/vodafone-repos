package com.ahmed.vodafonerepos.ui.repodetails

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.data.models.LoadingModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.ui.base.MainLoadingScreen
import com.ahmed.vodafonerepos.utils.alternate


@Composable
fun RepoDetailsScreen(
    viewModel: GetRepoDetailsViewModel = hiltViewModel()
) {
    val status by viewModel.repoDetailsSharedFlow.collectAsState(Status.Idle())
    val loading by viewModel.loadingObservable.collectAsState(LoadingModel(false))

    viewModel.getRepoDetails()
    Box(modifier = Modifier.fillMaxSize()) {
        if (loading.shouldShow) {
            MainLoadingScreen()
        } else {
            when (status.statusCode) {
                StatusCode.IDLE -> {
                    Box {}
                }
                StatusCode.SUCCESS, StatusCode.OFFLINE_DATA -> {
                    RepoDetailsContent(status.data!!)
                }
                StatusCode.ERROR -> {
                    Text(text = status.error.alternate())
                }
                else -> {
                    Box {}
                }
            }
        }
    }

}

