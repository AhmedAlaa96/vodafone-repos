package com.ahmed.vodafonerepos.ui.repoissues

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.data.models.LoadingModel
import com.ahmed.vodafonerepos.data.models.ProgressTypes
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.ui.base.MainLoadingScreen
import com.ahmed.vodafonerepos.ui.base.Toolbar
import com.ahmed.vodafonerepos.utils.alternate


@Composable
fun RepoIssuesListScreen(
    viewModel: GetRepoIssuesListViewModel = hiltViewModel()
) {
    val status by viewModel.repoIssuesSharedFlow.collectAsState(Status.Idle())
    val loading by viewModel.loadingObservable.collectAsState(LoadingModel(false))

    viewModel.getRepoIssues()
    var loadingMore by remember { mutableStateOf(false) }
    var refreshState by remember { mutableStateOf(false) }
    val scrollState = rememberLazyListState()
    val isScrolledToBottom by remember(scrollState) {
        derivedStateOf {
            val lastVisibleItemIndex =
                scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = status.data!!.size
            lastVisibleItemIndex >= totalItems - 1
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (loading.shouldShow && status.isIdle()) {
            MainLoadingScreen()
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Toolbar(title = stringResource(id = R.string.issues), hasBackButton = true) {
                    // TODO:: ADD NAVIGATE UP
                }
                when (status.statusCode) {
                    StatusCode.IDLE -> {
                        Box {}
                    }
                    StatusCode.SUCCESS, StatusCode.OFFLINE_DATA -> {
                        loadingMore =
                            loading.shouldShow && loading.progressType == ProgressTypes.PAGING_PROGRESS
                        refreshState =
                            loading.shouldShow && loading.progressType == ProgressTypes.PULL_TO_REFRESH_PROGRESS
                        RepoIssuesList(reposList = status.data!!,
                            loadingMore = loadingMore,
                            scrollState = scrollState,
                            isScrolledToBottom = isScrolledToBottom,
                            refreshState = refreshState,
                            onScroll = {
                                loadingMore = true
                                viewModel.onScroll()
                            },
                            onRefresh = {
                                refreshState = true
                                viewModel.onRefresh()
                            })
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