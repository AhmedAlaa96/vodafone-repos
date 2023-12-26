package com.ahmed.vodafonerepos.ui.repoissues

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.ProgressTypes
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoUiResponse
import com.ahmed.vodafonerepos.di.MainDispatcher
import com.ahmed.vodafonerepos.domain.usecases.getrepoissueslist.IGetRepoIssuesListUseCase
import com.ahmed.vodafonerepos.ui.base.BasePagingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRepoIssuesListViewModel @Inject constructor(
    private val mIGetRepoIssuesUseCase: IGetRepoIssuesListUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    handle: SavedStateHandle
) : BasePagingViewModel(handle, mIGetRepoIssuesUseCase) {

    private var repoIssuesResponseStatus: Status<ArrayList<RepoIssueResponse>>? = null

    private var repoIssuesRequest: RepoDetailsRequest?

    private val mPageModel = PageModel()

    init {
        repoIssuesRequest = RepoDetailsRequest("flutter", "flutter")
//        repoIssuesRequest = handle.get<RepoIssuesRequest>("repoIssuesRequest")
    }

    private val _repoIssuesResponseMutableSharedFlow =
        MutableSharedFlow<Status<ArrayList<RepoIssueResponse>>>()
    val repoIssuesSharedFlow = _repoIssuesResponseMutableSharedFlow.asSharedFlow()

    internal fun getRepoIssues() {
        val handler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch {
                setRepoIssuesResponseStatus(Status.Error(error = exception.message))
            }
        }

        viewModelScope.launch(mainDispatcher + handler) {
            if (repoIssuesResponseStatus != null && repoIssuesResponseStatus?.isIdle() != true) {
                setRepoIssuesResponseStatus(repoIssuesResponseStatus!!)
            } else {
                callGetRepoIssuesList(ProgressTypes.MAIN_PROGRESS)
            }
        }
    }


    internal fun onScroll() {
        if (!isLoading && shouldLoadMore) {
            val handler = CoroutineExceptionHandler { _, exception ->
                viewModelScope.launch {
                    setRepoIssuesResponseStatus(Status.Error(error = exception.message))
                }
            }

            viewModelScope.launch(mainDispatcher + handler) {
                callGetRepoIssuesList(ProgressTypes.PAGING_PROGRESS, false)
            }
        }
    }

    internal fun onRefresh() {
        if (!isLoading) {
            mPageModel.reset()
            val handler = CoroutineExceptionHandler { _, exception ->
                viewModelScope.launch {
                    setRepoIssuesResponseStatus(Status.Error(error = exception.message))
                }
            }

            viewModelScope.launch(mainDispatcher + handler) {
                callGetRepoIssuesList(ProgressTypes.PULL_TO_REFRESH_PROGRESS, true)
            }
        }

    }

    internal fun onRetryClicked() {
        mPageModel.reset()
        val handler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch {
                setRepoIssuesResponseStatus(Status.Error(error = exception.message))
            }
        }

        viewModelScope.launch(mainDispatcher + handler) {
            callGetRepoIssuesList(ProgressTypes.MAIN_PROGRESS, true)
        }

    }


    private suspend fun callGetRepoIssuesList(
        progressType: ProgressTypes,
        shouldClear: Boolean = true
    ) {
        onGetRepoIssuesSubscribe(progressType)
        mIGetRepoIssuesUseCase.getRepoIssuesList(repoIssuesRequest, mPageModel)
            .onStart {
                showProgress(true, progressType)
            }.onCompletion {
                showProgress(false)
            }.catch {
                setRepoIssuesResponseStatus(Status.Error(error = it.message))
                showProgress(false, progressType)
                isLoading = false
            }.collect {
                mapReposListResponse(it, progressType, shouldClear)
            }


    }

    private suspend fun mapReposListResponse(
        reposListResponse: Status<ArrayList<RepoIssueResponse>>,
        progressType: ProgressTypes,
        shouldClear: Boolean
    ) {
        isLoading = false
        when (reposListResponse.statusCode) {
            StatusCode.SUCCESS, StatusCode.OFFLINE_DATA -> {
                clearData(shouldClear)
                val reposList = reposListResponse.data!!
                val currentList = repoIssuesResponseStatus?.data ?: ArrayList()
                currentList.addAll(reposList)
                mPageModel.incrementPageNumber()
                setRepoIssuesResponseStatus(Status.Success(currentList))
            }
            StatusCode.NO_DATA -> {
                shouldLoadMore = false
                repoIssuesResponseStatus?.let { setRepoIssuesResponseStatus(it) }
            }
            else -> {
                if (!repoIssuesResponseStatus?.data.isNullOrEmpty()) {
                    handleStatusErrorWithExistingData(progressType, reposListResponse)
                } else {
                    setRepoIssuesResponseStatus(
                        Status.CopyStatus(
                            reposListResponse,
                            reposListResponse.data,
                        )

                    )
                }
            }
        }
    }

    private suspend fun clearData(shouldClear: Boolean) {
        shouldClearObservable.emit(shouldClear)
        if (shouldClear) {
            repoIssuesResponseStatus?.data?.clear()
        }
    }

    private fun onGetRepoIssuesSubscribe(progressType: ProgressTypes) {
        showProgress(true, progressType)
        shouldShowError(false)
    }

    private suspend fun setRepoIssuesResponseStatus(repoIssuesResponseStatus: Status<ArrayList<RepoIssueResponse>>) {
        this.repoIssuesResponseStatus = repoIssuesResponseStatus
        _repoIssuesResponseMutableSharedFlow.emit(repoIssuesResponseStatus)
    }

}