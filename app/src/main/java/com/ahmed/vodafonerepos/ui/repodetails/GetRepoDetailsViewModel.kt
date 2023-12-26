package com.ahmed.vodafonerepos.ui.repodetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ahmed.vodafonerepos.data.models.ProgressTypes
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.di.MainDispatcher
import com.ahmed.vodafonerepos.domain.usecases.getrepodetails.IGetRepoDetailsUseCase
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
class GetRepoDetailsViewModel @Inject constructor(
    private val mIGetRepoDetailsUseCase: IGetRepoDetailsUseCase,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    handle: SavedStateHandle
) : BaseViewModel(handle, mIGetRepoDetailsUseCase) {

    private var repoDetailsResponseStatus: Status<RepoDetailsResponse>? = null

    private var repoDetailsRequest: RepoDetailsRequest?

    init {
        repoDetailsRequest = RepoDetailsRequest("octocat", "Hello-World")
//        repoDetailsRequest = handle.get<RepoDetailsRequest>("repoDetailsRequest")
    }

    private val _repoDetailsResponseMutableSharedFlow = MutableSharedFlow<Status<RepoDetailsResponse>>()
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

}