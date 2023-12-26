package com.ahmed.vodafonerepos.ui.repodetails

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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ahmed.vodafonerepos.R
import com.ahmed.vodafonerepos.data.models.LoadingModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.ui.base.ErrorLayout
import com.ahmed.vodafonerepos.ui.base.MainLoadingScreen
import com.ahmed.vodafonerepos.ui.base.Toolbar
import com.ahmed.vodafonerepos.utils.alternate


@Composable
fun RepoDetailsScreen(
    viewModel: GetRepoDetailsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val status by viewModel.repoDetailsSharedFlow.collectAsState(viewModel.status ?: Status.Idle())
    val loading by viewModel.loadingObservable.collectAsState(LoadingModel(false))

    viewModel.getRepoDetails()
    Box(modifier = Modifier.fillMaxSize()) {
        if (loading.shouldShow) {
            MainLoadingScreen()
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Toolbar(
                    title = stringResource(id = R.string.details),
                    hasBackButton = true,

                    ) {
                    navHostController.navigateUp()
                }
                when (status.statusCode) {
                    StatusCode.IDLE -> {
                    }
                    StatusCode.SUCCESS, StatusCode.OFFLINE_DATA -> {
                        RepoDetailsContent(
                            status.data!!,
                            onIssuesClicked = {
                                viewModel.onIssuesClicked(navHostController, it)
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
                            icon = R.drawable.ic_error,
                            title = stringResource(id = R.string.no_data)
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

