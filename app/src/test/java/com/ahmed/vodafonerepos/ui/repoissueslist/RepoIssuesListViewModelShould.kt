package com.ahmed.vodafonerepos.ui.repoissueslist

import androidx.lifecycle.SavedStateHandle
import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.domain.usecases.getrepoissueslist.IGetRepoIssuesListUseCase
import com.ahmed.vodafonerepos.ui.repoissues.GetRepoIssuesListViewModel
import com.ahmed.vodafonerepos.utils.BaseViewModelUnitTest
import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test

@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
class RepoIssuesListViewModelShould : BaseViewModelUnitTest() {

    private val repoDetailsRequest = RepoDetailsRequest("test", "test")
    private val pageModel = PageModel()
    private val repoIssuesList: ArrayList<RepoIssueResponse> = (1..30).toList().map {
        RepoIssueResponse()
    }.toCollection(arrayListOf())
    private val mGson: Gson = Gson()
    private val noNetworkError: String = "No Network"
    private val exception = RuntimeException("Something Went Wrong")
    private val reposListUseCase: IGetRepoIssuesListUseCase = mock()
    private val handle = mock<SavedStateHandle>()
    private val testDispatcher = Dispatchers.Unconfined


    @Before
    fun setup() {
        whenever(handle.get<String>("repoDetailsRequest")).thenReturn(
            mGson.toJson(
                repoDetailsRequest
            )
        )
    }

    @Test
    fun getReposListResponseFromUseCase() = runBlockingTest {
        val viewModel =
            GetRepoIssuesListViewModel(reposListUseCase, testDispatcher, mGson, handle)
        viewModel.getRepoIssues()
        verify(reposListUseCase, times(1)).getRepoIssuesList(repoDetailsRequest, pageModel)
    }

    @Test
    fun emitReposListResponseFromUseCaseOnGetReposListCalled() = runBlockingTest {
        val viewModel = initSuccessViewModel()
        test(onEvent = { viewModel.getRepoIssues() },
            onState = {
                val response =
                    viewModel.repoIssuesSharedFlow.first()
                TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
                TestCase.assertEquals(repoIssuesList, response.data)
            })
    }

    @Test
    fun propagateGetReposListResponseErrorOnGetReposListCalled() = runBlockingTest {
        val viewModel = initFailureViewModel()
        test(onEvent = { viewModel.getRepoIssues() },
            onState = {
                val response =
                    viewModel.repoIssuesSharedFlow.first()
                TestCase.assertEquals(StatusCode.ERROR, response.statusCode)
                TestCase.assertEquals(exception.message, response.error)
            })
    }

    @Test
    fun propagateNoNetworkErrorOnGetReposListCalled() = runBlockingTest {
        val viewModel = initNoNetworkViewModel()
        test(onEvent = { viewModel.getRepoIssues() },
            onState = {
                val response =
                    viewModel.repoIssuesSharedFlow.first()
                TestCase.assertEquals(StatusCode.NO_NETWORK, response.statusCode)
                TestCase.assertEquals(noNetworkError, response.error)
            })
    }

    @Test
    fun emitReposListResponseFromUseCaseOnRefreshCalled() = runBlockingTest {
        val viewModel = initSuccessViewModel()
        test(onEvent = { viewModel.onRefresh() },
            onState = {
                val response =
                    viewModel.repoIssuesSharedFlow.first()
                TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
                TestCase.assertEquals(repoIssuesList, response.data)
            })
    }

    @Test
    fun emitReposListResponseFromUseCaseOnScrollCalled() = runBlockingTest {
        val viewModel = initSuccessViewModel()
        test(onEvent = { viewModel.onScroll() },
            onState = {
                val response =
                    viewModel.repoIssuesSharedFlow.first()
                TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
                TestCase.assertEquals(repoIssuesList, response.data)
            })
    }


    private suspend fun initSuccessViewModel(): GetRepoIssuesListViewModel {
        whenever(reposListUseCase.getRepoIssuesList(repoDetailsRequest, pageModel)).thenReturn(
            flow {
                emit(Status.Success(repoIssuesList))
            },
        )

        return GetRepoIssuesListViewModel(
            reposListUseCase,
            testDispatcher,
            mGson,
            handle
        )
    }

    private suspend fun initFailureViewModel(): GetRepoIssuesListViewModel {
        whenever(reposListUseCase.getRepoIssuesList(repoDetailsRequest, pageModel)).thenReturn(
            flow {
                emit(Status.Error(error = exception.message))
            },
        )
        return GetRepoIssuesListViewModel(
            reposListUseCase,
            testDispatcher,
            mGson,
            handle
        )
    }

    private suspend fun initNoNetworkViewModel(): GetRepoIssuesListViewModel {
        whenever(reposListUseCase.getRepoIssuesList(repoDetailsRequest, pageModel)).thenReturn(
            flow {
                emit(Status.NoNetwork(error = noNetworkError))
            },
        )

        return GetRepoIssuesListViewModel(
            reposListUseCase,
            testDispatcher,
            mGson,
            handle
        )
    }

}