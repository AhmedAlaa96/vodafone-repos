package com.ahmed.vodafonerepos.ui.reposlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ahmed.vodafonerepos.data.models.LoadingModel
import com.ahmed.vodafonerepos.data.models.ProgressTypes
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.ui.base.MainLoadingScreen
import com.ahmed.vodafonerepos.utils.alternate


@Composable
fun ReposListScreen(
    viewModel: GetRepositoriesListViewModel = hiltViewModel()
) {
    val status by viewModel.reposResponseSharedFlow.collectAsState(Status.Idle())
    val loading by viewModel.loadingObservable.collectAsState(LoadingModel(false))

    viewModel.getReposListResponse()
    var loadingMore by remember { mutableStateOf(false) }

    // Detect if the user has scrolled to the end of the list
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
            if (loading.progressType == ProgressTypes.PAGING_PROGRESS) {
                loadingMore = true
            } else {
                MainLoadingScreen()
            }
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
                        loadingMore = loading.shouldShow && loading.progressType == ProgressTypes.PAGING_PROGRESS
                        ReposList(status.data!!, loadingMore, scrollState, isScrolledToBottom) {
                            loadingMore = true
                            viewModel.onScroll()
                        }
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

@Composable
fun BoxState() {
    Box {

    }
}