package com.ahmed.vodafonerepos.ui.reposlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.ProgressTypes
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoUiResponse
import com.ahmed.vodafonerepos.di.MainDispatcher
import com.ahmed.vodafonerepos.domain.usecases.getrepositorieslist.IGetRepositoriesListUseCase
import com.ahmed.vodafonerepos.ui.base.BasePagingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRepositoriesListViewModel @Inject constructor(
    private val mIGetRepositoriesListUseCase: IGetRepositoriesListUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    handle: SavedStateHandle
) :
    BasePagingViewModel(handle, mIGetRepositoriesListUseCase) {

    private var reposResponseStatus: Status<ArrayList<RepoResponse>>? = null
    private val mPageModel = PageModel()

    private val _reposResponseMutableSharedFlow =
        MutableSharedFlow<Status<ArrayList<RepoResponse>>>()
    val reposResponseSharedFlow = _reposResponseMutableSharedFlow.asSharedFlow()

    internal fun getReposListResponse() {
        val handler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch {
                setReposResponseStatus(Status.Error(error = exception.message))
            }
        }

        viewModelScope.launch(mainDispatcher + handler) {
            if (reposResponseStatus != null && reposResponseStatus?.isIdle() != true && reposResponseStatus?.isOfflineData() != true) {
                setReposResponseStatus(reposResponseStatus!!)
            } else {
                getLocalReposList()
            }
        }
    }

    internal fun onScroll() {
        if (!isLoading) {
            val handler = CoroutineExceptionHandler { _, exception ->
                viewModelScope.launch {
                    setReposResponseStatus(Status.Error(error = exception.message))
                }
            }

            viewModelScope.launch(mainDispatcher + handler) {
                callGetReposList(ProgressTypes.PAGING_PROGRESS, false)
            }
        }
    }

    internal fun onRefresh() {
        if (!isLoading) {
            mPageModel.reset()
            val handler = CoroutineExceptionHandler { _, exception ->
                viewModelScope.launch {
                    setReposResponseStatus(Status.Error(error = exception.message))
                }
            }

            viewModelScope.launch(mainDispatcher + handler) {
                callGetReposList(ProgressTypes.PULL_TO_REFRESH_PROGRESS, true)
            }
        }

    }

    internal fun onRetryClicked() {
        mPageModel.reset()
        val handler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch {
                setReposResponseStatus(Status.Error(error = exception.message))
            }
        }

        viewModelScope.launch(mainDispatcher + handler) {
            callGetReposList(ProgressTypes.MAIN_PROGRESS, true)
        }

    }

    private suspend fun callGetReposList(progressType: ProgressTypes, shouldClear: Boolean) {
        onGetReposSubscribe(progressType)
        isLoading = true
        mIGetRepositoriesListUseCase.getRepositoriesListLocally(mPageModel)
            .onStart {
                showProgress(true, progressType)
            }.onCompletion {
                showProgress(false, progressType)
                isLoading = false
            }.catch {
                setReposResponseStatus(Status.Error(error = it.message))
                showProgress(false, progressType)
                isLoading = false
            }
            .collect {
                mapReposListResponse(it, progressType, shouldClear)
            }
    }

    private fun getLocalReposList() {
        viewModelScope.launch {
            mIGetRepositoriesListUseCase.getRepositoriesListLocally(mPageModel)
                .collect { reposResponseStatus ->
                    if (reposResponseStatus.isOfflineData() && !reposResponseStatus.data?.reposList.isNullOrEmpty()) {
                        setReposResponseStatus(
                            Status.OfflineData(
                                reposResponseStatus.data?.reposList,
                                error = null
                            )
                        )
                    }
                    callGetReposList(ProgressTypes.MAIN_PROGRESS, true)
                }

        }
    }

    private suspend fun mapReposListResponse(
        reposListResponse: Status<RepoUiResponse>,
        progressType: ProgressTypes,
        shouldClear: Boolean
    ) {
        isLoading = false
        when (reposListResponse.statusCode) {
            StatusCode.SUCCESS , StatusCode.OFFLINE_DATA-> {
                clearData(shouldClear)
                val reposList = reposListResponse.data?.reposList!!
                val totalCount = reposListResponse.data.totalCount

                val currentList = reposResponseStatus?.data ?: ArrayList()
                currentList.addAll(reposList)
                mPageModel.incrementPageNumber()

                if (totalCount != null && currentList.size >= totalCount)
                    shouldLoadMore = false

                setReposResponseStatus(Status.Success(currentList))
            }
            else -> {
                if (!reposResponseStatus?.data.isNullOrEmpty()) {
                    handleStatusErrorWithExistingData(progressType, reposListResponse)
                } else {
                    setReposResponseStatus(
                        Status.CopyStatus(
                            reposListResponse,
                            reposListResponse.data?.reposList,
                        )

                    )
                }
            }
        }
    }

    private suspend fun clearData(shouldClear: Boolean) {
        shouldClearObservable.emit(shouldClear)
        if (shouldClear) {
            reposResponseStatus?.data?.clear()
        }
    }

    private fun onGetReposSubscribe(progressType: ProgressTypes) {
        showProgress(true, progressType)
        shouldShowError(false)
    }

    private suspend fun setReposResponseStatus(reposResponseStatus: Status<ArrayList<RepoResponse>>) {
        if (!reposResponseStatus.isSuccess() && this.reposResponseStatus?.isOfflineData() == true) {
            val repos = this.reposResponseStatus?.data
            this.reposResponseStatus = Status.Success(repos)
        } else {
            this.reposResponseStatus = reposResponseStatus
        }

        _reposResponseMutableSharedFlow.emit(reposResponseStatus)
    }

}