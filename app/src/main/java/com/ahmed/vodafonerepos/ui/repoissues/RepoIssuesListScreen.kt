package com.ahmed.vodafonerepos.ui.repoissues

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.data.models.LoadingModel
import com.ahmed.vodafonerepos.data.models.ProgressTypes
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.ui.base.ErrorLayout
import com.ahmed.vodafonerepos.ui.base.MainLoadingScreen
import com.ahmed.vodafonerepos.ui.base.Toolbar
import com.ahmed.vodafonerepos.utils.alternate


@Composable
fun RepoIssuesListScreen(
    viewModel: GetRepoIssuesListViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val status by viewModel.repoIssuesSharedFlow.collectAsState(viewModel.state ?: Status.Idle())
    val loading by viewModel.loadingObservable.collectAsState(viewModel.loadingModel ?: LoadingModel(true))

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
                    navHostController.navigateUp()
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
                        ErrorLayout(
                            icon = R.drawable.ic_error,
                            title = status.error.alternate(),
                            subTitle = stringResource(
                                id = R.string.something_went_wrong
                            )
                        ) {
                            viewModel.onRetryClicked()
                        }
                    }
                    StatusCode.NO_DATA -> {
                        ErrorLayout(
                            icon = R.drawable.ic_no_data,
                            title = stringResource(id = R.string.no_opened_issue)
                        )
                    }
                    StatusCode.NO_NETWORK -> {
                        ErrorLayout(
                            icon = R.drawable.ic_no_network,
                            title = stringResource(id = R.string.no_network),
                            subTitle = stringResource(
                                id = R.string.there_is_no_network
                            )
                        ) {
                            viewModel.onRetryClicked()
                        }
                    }
                    StatusCode.SERVER_ERROR -> {
                        ErrorLayout(
                            icon = R.drawable.ic_error,
                            title = status.error.alternate()
                        ) {
                            viewModel.onRetryClicked()
                        }
                    }
                    else -> {
                        Box {}
                    }
                }
            }
        }
    }
}