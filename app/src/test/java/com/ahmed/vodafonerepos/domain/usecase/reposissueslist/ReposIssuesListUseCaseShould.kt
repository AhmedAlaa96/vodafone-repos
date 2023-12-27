package com.ahmed.vodafonerepos.domain.usecase.reposissueslist

import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.data.repositories.getrepoissuesList.IGetRepoIssuesListRepository
import com.ahmed.vodafonerepos.domain.usecases.getrepoissueslist.GetRepoIssuesListUseCase
import com.ahmed.vodafonerepos.domain.usecases.getrepoissueslist.IGetRepoIssuesListUseCase
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
class ReposIssuesListUseCaseShould : BaseUnitTest() {

    private val getRepoIssuesListRepository: IGetRepoIssuesListRepository = mock()
    private val repoIssuesListResponse: ArrayList<RepoIssueResponse> = mock()
    private val repoDetailsRequest = RepoDetailsRequest("test", "test")
    private val pageModel = PageModel()
    private val noNetworkError: String = "No Network"
    private val noData: String = "No Data"
    private val exception = RuntimeException("Something Went Wrong")

    @Test
    fun getRepoIssuesListResponseFromRepository() = runBlockingTest {
        val getReposUseCase = GetRepoIssuesListUseCase(getRepoIssuesListRepository)
        getReposUseCase.getRepoIssuesList(repoDetailsRequest, pageModel)
        verify(getRepoIssuesListRepository, times(1)).getRepoIssuesList(
            repoDetailsRequest,
            pageModel
        )
    }


    @Test
    fun emitRepoIssuesListResponseFromRepository() = runBlockingTest {
        val useCase = initSuccessUseCase()
        val response = useCase.getRepoIssuesList(repoDetailsRequest, pageModel).first()
        TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
        TestCase.assertEquals(repoIssuesListResponse, response.data)
    }

    @Test
    fun emitRepoIssuesListResponseFromRepositoryButNoData() = runBlockingTest {
        val useCase = initSuccessUseCaseButNoData()
        val response = useCase.getRepoIssuesList(repoDetailsRequest, pageModel).first()
        TestCase.assertEquals(StatusCode.NO_DATA, response.statusCode)
        TestCase.assertEquals(noData, response.error)
    }

    @Test
    fun propagateNoNetworkError() = runBlockingTest {
        val useCase = initNoNetworkUseCase()
        val response = useCase.getRepoIssuesList(repoDetailsRequest, pageModel).first()
        TestCase.assertEquals(StatusCode.NO_NETWORK, response.statusCode)
        TestCase.assertEquals(noNetworkError, response.error)
    }

    @Test
    fun propagateGetRepoIssuesListResponseError() = runBlockingTest {
        val useCase = initFailureUseCase()
        val response = useCase.getRepoIssuesList(repoDetailsRequest, pageModel).first()
        TestCase.assertEquals(StatusCode.ERROR, response.statusCode)
        TestCase.assertEquals(exception.message, response.error)
    }


    private suspend fun initSuccessUseCase(): IGetRepoIssuesListUseCase {
        whenever(
            getRepoIssuesListRepository.getRepoIssuesList(
                repoDetailsRequest,
                pageModel
            )
        ).thenReturn(
            flow {
                emit(Status.Success(repoIssuesListResponse))
            },
        )

        return GetRepoIssuesListUseCase(
            getRepoIssuesListRepository
        )
    }

    private suspend fun initSuccessUseCaseButNoData(): IGetRepoIssuesListUseCase {
        whenever(
            getRepoIssuesListRepository.getRepoIssuesList(
                repoDetailsRequest,
                pageModel
            )
        ).thenReturn(
            flow {
                emit(Status.NoData(error = noData))
            },
        )

        return GetRepoIssuesListUseCase(
            getRepoIssuesListRepository
        )
    }

    private suspend fun initFailureUseCase(): IGetRepoIssuesListUseCase {
        whenever(
            getRepoIssuesListRepository.getRepoIssuesList(
                repoDetailsRequest,
                pageModel
            )
        ).thenReturn(
            flow {
                emit(Status.Error(error = exception.message))
            },
        )

        return GetRepoIssuesListUseCase(
            getRepoIssuesListRepository
        )
    }

    private suspend fun initNoNetworkUseCase(): IGetRepoIssuesListUseCase {
        whenever(
            getRepoIssuesListRepository.getRepoIssuesList(
                repoDetailsRequest,
                pageModel
            )
        ).thenReturn(
            flow {
                emit(Status.NoNetwork(error = noNetworkError))
            },
        )

        return GetRepoIssuesListUseCase(
            getRepoIssuesListRepository
        )
    }

}