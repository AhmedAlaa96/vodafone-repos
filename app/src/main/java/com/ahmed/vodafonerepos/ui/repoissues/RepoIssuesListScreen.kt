package com.ahmed.vodafonerepos.ui.repoissues

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.vodafonerepos.data.models.LoadingModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.ui.base.MainLoadingScreen
import com.ahmed.vodafonerepos.utils.alternate


@Composable
fun RepoIssuesListScreen(
    viewModel: GetRepoIssuesListViewModel = hiltViewModel()
) {
    val status by viewModel.repoIssuesSharedFlow.collectAsState(Status.Idle())
    val loading by viewModel.loadingObservable.collectAsState(LoadingModel(false))

    viewModel.getRepoIssues()
    Box(modifier = Modifier.fillMaxSize()) {
        if (loading.shouldShow) {
            MainLoadingScreen()
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (status.statusCode) {
                    StatusCode.IDLE -> {
                        Box {}
                    }
                    StatusCode.SUCCESS, StatusCode.OFFLINE_DATA -> {
                        RepoIssuesList(status.data!!)
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
}