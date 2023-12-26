package com.ahmed.vodafonerepos.ui.repoissues

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ahmed.vodafonerepos.data.models.ProgressTypes
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.di.MainDispatcher
import com.ahmed.vodafonerepos.domain.usecases.getrepoissueslist.IGetRepoIssuesListUseCase
import com.ahmed.vodafonerepos.ui.base.BaseViewModel
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
class GetRepoIssuesListViewModel @Inject constructor(
    private val mIGetRepoIssuesUseCase: IGetRepoIssuesListUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    handle: SavedStateHandle
) : BaseViewModel(handle, mIGetRepoIssuesUseCase) {

    private var repoIssuesResponseStatus: Status<ArrayList<RepoIssueResponse>>? = null

    private var repoIssuesRequest: RepoDetailsRequest?

    init {
        repoIssuesRequest = RepoDetailsRequest("flutter", "flutter")
//        repoIssuesRequest = handle.get<RepoIssuesRequest>("repoIssuesRequest")
    }

    private val _repoIssuesResponseMutableSharedFlow = MutableSharedFlow<Status<ArrayList<RepoIssueResponse>>>()
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
                callGetRepoIssuesDetails(ProgressTypes.MAIN_PROGRESS)
            }
        }
    }

    private suspend fun callGetRepoIssuesDetails(progressType: ProgressTypes) {
        onGetRepoIssuesSubscribe(progressType)
        mIGetRepoIssuesUseCase.getRepoIssuesList(repoIssuesRequest)
            .onStart {
                showProgress(true, progressType)
            }.onCompletion {
                showProgress(false)
            }.collect {
                setRepoIssuesResponseStatus(it)
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