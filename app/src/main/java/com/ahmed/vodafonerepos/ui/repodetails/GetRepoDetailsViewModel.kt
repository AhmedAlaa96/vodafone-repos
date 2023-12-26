package com.ahmed.vodafonerepos.ui.repodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ahmed.vodafonerepos.data.models.ProgressTypes
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.di.MainDispatcher
import com.ahmed.vodafonerepos.domain.usecases.getrepodetails.IGetRepoDetailsUseCase
import com.ahmed.vodafonerepos.ui.base.BaseViewModel
import com.ahmed.vodafonerepos.ui.main.NavigationItem
import com.ahmed.vodafonerepos.utils.alternate
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRepoDetailsViewModel @Inject constructor(
    private val mIGetRepoDetailsUseCase: IGetRepoDetailsUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val mGson: Gson,
    handle: SavedStateHandle
) : BaseViewModel(handle, mIGetRepoDetailsUseCase) {

    private var repoDetailsResponseStatus: Status<RepoDetailsResponse>? = null

    val status: Status<RepoDetailsResponse>?
        get() = repoDetailsResponseStatus

    private var repoDetailsRequest: RepoDetailsRequest = RepoDetailsRequest("", "")

    init {
        handle.get<String>("repoDetailsRequest")?.let {
            repoDetailsRequest = mGson.fromJson(it, RepoDetailsRequest::class.java)
        }
    }

    private val _repoDetailsResponseMutableSharedFlow =
        MutableSharedFlow<Status<RepoDetailsResponse>>()
    val repoDetailsSharedFlow = _repoDetailsResponseMutableSharedFlow.asSharedFlow()

    internal fun getRepoDetails() {
        val handler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch {
                setRepoDetailsResponseStatus(Status.Error(error = exception.message))
            }
        }

        viewModelScope.launch(mainDispatcher + handler) {
            if (repoDetailsResponseStatus != null && repoDetailsResponseStatus?.isIdle() != true) {
                setRepoDetailsResponseStatus(repoDetailsResponseStatus!!)
            } else {
                callGetRepoDetailsDetails(ProgressTypes.MAIN_PROGRESS)
            }
        }
    }
    internal fun onRetryClicked() {
        val handler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch {
                setRepoDetailsResponseStatus(Status.Error(error = exception.message))
            }
        }
        viewModelScope.launch(mainDispatcher + handler) {
            callGetRepoDetailsDetails(ProgressTypes.MAIN_PROGRESS)
        }

    }

    private suspend fun callGetRepoDetailsDetails(progressType: ProgressTypes) {
        onGetRepoDetailsSubscribe(progressType)
        mIGetRepoDetailsUseCase.getRepoDetails(repoDetailsRequest)
            .onStart {
                showProgress(true, progressType)
            }.onCompletion {
                showProgress(false)
            }.collect {
                setRepoDetailsResponseStatus(it)
            }
    }

    private fun onGetRepoDetailsSubscribe(progressType: ProgressTypes) {
        showProgress(true, progressType)
        shouldShowError(false)
    }

    private suspend fun setRepoDetailsResponseStatus(repoDetailsResponseStatus: Status<RepoDetailsResponse>) {
        this.repoDetailsResponseStatus = repoDetailsResponseStatus
        _repoDetailsResponseMutableSharedFlow.emit(repoDetailsResponseStatus)
    }

    fun onIssuesClicked(
        navHostController: NavHostController,
        repoDetailsResponse: RepoDetailsResponse
    ) {
        navHostController.navigate(
            "${NavigationItem.RepoIssues.route}/${
                mGson.toJson(
                    RepoDetailsRequest(
                        repoDetailsResponse.owner?.login.alternate(),
                        repoDetailsResponse.name.alternate(),
                    )
                )
            }"
        )
    }
}