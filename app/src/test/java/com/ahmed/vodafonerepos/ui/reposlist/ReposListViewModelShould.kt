package com.ahmed.vodafonerepos.ui.reposlist

import androidx.lifecycle.SavedStateHandle
import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoUiResponse
import com.ahmed.vodafonerepos.domain.usecases.getrepositorieslist.IGetRepositoriesListUseCase
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
import org.junit.Test

@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
class ReposListViewModelShould : BaseViewModelUnitTest() {

    private val pageModel = PageModel()
    private val repoUiResponse:RepoUiResponse = mock()
    private val mGson: Gson = mock()
    private val noNetworkError: String = "No Network"
    private val exception = RuntimeException("Something Went Wrong")
    private val reposListUseCase: IGetRepositoriesListUseCase = mock()
    private val handle = mock<SavedStateHandle>()
    private val testDispatcher = Dispatchers.Unconfined

    @Test
    fun getReposListResponseFromUseCase() = runBlockingTest {
        val viewModel =
            GetRepositoriesListViewModel(reposListUseCase, testDispatcher, mGson, handle)
        viewModel.getReposListResponse()
        verify(reposListUseCase, times(1)).getRepositoriesList(pageModel, true)
    }

    @Test
    fun emitReposListResponseFromUseCaseOnGetReposListCalled() = runBlockingTest {
        val viewModel = initSuccessViewModel()
        test(onEvent = { viewModel.getReposListResponse() },
            onState = {
                val response =
                    viewModel.reposResponseSharedFlow.first()
                TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
                TestCase.assertEquals(repoUiResponse.reposList, response.data)
            })
    }

    @Test
    fun propagateGetReposListResponseErrorOnGetReposListCalled() = runBlockingTest {
        val viewModel = initFailureViewModel()
        test(onEvent = { viewModel.getReposListResponse() },
            onState = {
                val response =
                    viewModel.reposResponseSharedFlow.first()
                TestCase.assertEquals(StatusCode.ERROR, response.statusCode)
                TestCase.assertEquals(exception.message, response.error)
            })
    }

    @Test
    fun propagateNoNetworkErrorOnGetReposListCalled() = runBlockingTest {
        val viewModel = initNoNetworkViewModel()
        test(onEvent = { viewModel.getReposListResponse() },
            onState = {
                val response =
                    viewModel.reposResponseSharedFlow.first()
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
                    viewModel.reposResponseSharedFlow.first()
                TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
                TestCase.assertEquals(repoUiResponse.reposList, response.data)
            })
    }

    @Test
    fun emitReposListResponseFromUseCaseOnScrollCalled() = runBlockingTest {
        val viewModel = initSuccessScrollViewModel()
        test(onEvent = { viewModel.onScroll() },
            onState = {
                val response =
                    viewModel.reposResponseSharedFlow.first()
                TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
                TestCase.assertEquals(repoUiResponse.reposList, response.data)
            })
    }


    private suspend fun initSuccessViewModel(): GetRepositoriesListViewModel {
        whenever(reposListUseCase.getRepositoriesList(pageModel,true)).thenReturn(
            flow {
                emit(Status.Success(repoUiResponse))
            },
        )

        return GetRepositoriesListViewModel(
            reposListUseCase,
            testDispatcher,
            mGson,
            handle
        )
    }

    private suspend fun initSuccessScrollViewModel(): GetRepositoriesListViewModel {
        whenever(reposListUseCase.getRepositoriesList(pageModel,false)).thenReturn(
            flow {
                emit(Status.Success(repoUiResponse))
            },
        )

        return GetRepositoriesListViewModel(
            reposListUseCase,
            testDispatcher,
            mGson,
            handle
        )
    }

    private suspend fun initFailureViewModel(): GetRepositoriesListViewModel {
        whenever(reposListUseCase.getRepositoriesList(pageModel,true)).thenReturn(
            flow {
                emit(Status.Error(error = exception.message))
            },
        )
        return GetRepositoriesListViewModel(
            reposListUseCase,
            testDispatcher,
            mGson,
            handle
        )
    }

    private suspend fun initNoNetworkViewModel(): GetRepositoriesListViewModel {
        whenever(reposListUseCase.getRepositoriesList(pageModel,true)).thenReturn(
            flow {
                emit(Status.NoNetwork(error = noNetworkError))
            },
        )

        return GetRepositoriesListViewModel(
            reposListUseCase,
            testDispatcher,
            mGson,
            handle
        )
    }

}