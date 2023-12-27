package com.ahmed.vodafonerepos.domain.usecase.repodetails

import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.data.repositories.getrepodetails.IGetRepoDetailsRepository
import com.ahmed.vodafonerepos.domain.usecases.getrepodetails.GetRepoDetailsUseCase
import com.ahmed.vodafonerepos.domain.usecases.getrepodetails.IGetRepoDetailsUseCase
import com.ahmed.vodafonerepos.utils.BaseUnitTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test


@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
class RepoDetailsUseCaseShould : BaseUnitTest() {

    private val getRepoDetailsRepository: IGetRepoDetailsRepository = mock()
    private val repoDetailsResponse: RepoDetailsResponse = mock()
    private val repoDetailsRequest = RepoDetailsRequest("test", "test")
    private val noNetworkError: String = "No Network"
    private val exception = RuntimeException("Something Went Wrong")

    @Test
    fun getRepoDetailsResponseFromRepository() = runBlockingTest {
        val getRepoDetailsUseCase = GetRepoDetailsUseCase(getRepoDetailsRepository)
        getRepoDetailsUseCase.getRepoDetails(repoDetailsRequest)
        verify(getRepoDetailsRepository, times(1)).getRepoDetails(repoDetailsRequest)
    }


    @Test
    fun emitRepoDetailsResponseFromRepository() = runBlockingTest {
        val useCase = initSuccessUseCase()
        val response = useCase.getRepoDetails(repoDetailsRequest).first()
        TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
        TestCase.assertEquals(repoDetailsResponse, response.data)
    }

    @Test
    fun propagateNoNetworkError() = runBlockingTest {
        val useCase = initNoNetworkUseCase()
        val response = useCase.getRepoDetails(repoDetailsRequest).first()
        TestCase.assertEquals(StatusCode.NO_NETWORK, response.statusCode)
        TestCase.assertEquals(noNetworkError, response.error)
    }

    @Test
    fun propagateGetRepoDetailsResponseError() = runBlockingTest {
        val useCase = initFailureUseCase()
        val response = useCase.getRepoDetails(repoDetailsRequest).first()
        TestCase.assertEquals(StatusCode.ERROR, response.statusCode)
        TestCase.assertEquals(exception.message, response.error)
    }


    private suspend fun initSuccessUseCase(): IGetRepoDetailsUseCase {
        whenever(getRepoDetailsRepository.getRepoDetails(repoDetailsRequest)).thenReturn(
            flow {
                emit(Status.Success(repoDetailsResponse))
            },
        )

        return GetRepoDetailsUseCase(
            getRepoDetailsRepository
        )
    }

    private suspend fun initFailureUseCase(): IGetRepoDetailsUseCase {
        whenever(getRepoDetailsRepository.getRepoDetails(repoDetailsRequest)).thenReturn(
            flow {
                emit(Status.Error(error = exception.message))
            },
        )

        return GetRepoDetailsUseCase(
            getRepoDetailsRepository
        )
    }

    private suspend fun initNoNetworkUseCase(): IGetRepoDetailsUseCase {
        whenever(getRepoDetailsRepository.getRepoDetails(repoDetailsRequest)).thenReturn(
            flow {
                emit(Status.NoNetwork(error = noNetworkError))
            },
        )

        return GetRepoDetailsUseCase(
            getRepoDetailsRepository
        )
    }
}
