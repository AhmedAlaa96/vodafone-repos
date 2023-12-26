package com.ahmed.vodafonerepos.ui.repodetails

import androidx.lifecycle.SavedStateHandle
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.domain.usecases.getrepodetails.IGetRepoDetailsUseCase
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
class RepoDetailsViewModelShould : BaseViewModelUnitTest() {

    private val repoDetailsRequest = RepoDetailsRequest("test", "test")
    private val repoDetailsResponse: RepoDetailsResponse = mock()
    private val noNetworkError: String = "No Network"
    private val exception = RuntimeException("Something Went Wrong")
    private val repoDetailsUseCase: IGetRepoDetailsUseCase = mock()
    private val handle = mock<SavedStateHandle>()
    private val mGson: Gson = Gson()
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
    fun getRepoDetailsResponseFromUseCase() = runBlockingTest {
        val viewModel = GetRepoDetailsViewModel(repoDetailsUseCase, testDispatcher, mGson, handle)
        viewModel.getRepoDetails()
        verify(repoDetailsUseCase, times(1)).getRepoDetails(repoDetailsRequest)
    }

    @Test
    fun emitRepoDetailsResponseFromUseCase() = runBlockingTest {
        val viewModel = initSuccessViewModel()
        test(onEvent = { viewModel.getRepoDetails() },
            onState = {
                val response =
                    viewModel.repoDetailsSharedFlow.first()
                TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
                TestCase.assertEquals(repoDetailsResponse, response.data)
            })
    }

    @Test
    fun propagateGetRepoDetailsResponseWithError() = runBlockingTest {
        val viewModel = initFailureViewModel()
        test(onEvent = { viewModel.getRepoDetails() },
            onState = {
                val response =
                    viewModel.repoDetailsSharedFlow.first()
                TestCase.assertEquals(StatusCode.ERROR, response.statusCode)
                TestCase.assertEquals(exception.message, response.error)
            })
    }

    @Test
    fun propagateNoNetworkErrorOnGetRepoDetails() = runBlockingTest {
        val viewModel = initNoNetworkViewModel()
        test(onEvent = { viewModel.getRepoDetails() },
            onState = {
                val response =
                    viewModel.repoDetailsSharedFlow.first()
                TestCase.assertEquals(StatusCode.NO_NETWORK, response.statusCode)
                TestCase.assertEquals(noNetworkError, response.error)
            })
    }

    private suspend fun initSuccessViewModel(): GetRepoDetailsViewModel {
        whenever(repoDetailsUseCase.getRepoDetails(repoDetailsRequest)).thenReturn(
            flow {
                emit(Status.Success(repoDetailsResponse))
            },
        )

        return GetRepoDetailsViewModel(
            repoDetailsUseCase,
            testDispatcher,
            mGson,
            handle
        )
    }

    private suspend fun initFailureViewModel(): GetRepoDetailsViewModel {
        whenever(repoDetailsUseCase.getRepoDetails(repoDetailsRequest)).thenReturn(
            flow {
                emit(Status.Error(error = exception.message))
            },
        )
        return GetRepoDetailsViewModel(
            repoDetailsUseCase,
            testDispatcher,
            mGson,
            handle
        )
    }

    private suspend fun initNoNetworkViewModel(): GetRepoDetailsViewModel {
        whenever(repoDetailsUseCase.getRepoDetails(repoDetailsRequest)).thenReturn(
            flow {
                emit(Status.NoNetwork(error = noNetworkError))
            },
        )

        return GetRepoDetailsViewModel(
            repoDetailsUseCase,
            testDispatcher,
            mGson,
            handle
        )
    }

}