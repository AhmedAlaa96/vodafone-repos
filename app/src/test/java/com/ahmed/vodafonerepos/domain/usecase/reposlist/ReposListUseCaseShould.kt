package com.ahmed.vodafonerepos.domain.usecase.reposlist

import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoUiResponse
import com.ahmed.vodafonerepos.data.repositories.getrepositorieslist.IGetRepositoriesListRepository
import com.ahmed.vodafonerepos.domain.usecases.getrepositorieslist.GetRepositoriesListUseCase
import com.ahmed.vodafonerepos.domain.usecases.getrepositorieslist.IGetRepositoriesListUseCase
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
class ReposListUseCaseShould : BaseUnitTest() {

    private val getReposListRepository: IGetRepositoriesListRepository = mock()
    private val reposListResponse: ArrayList<RepoResponse> = mock()
    private val pageModel = PageModel()
    private val noNetworkError: String = "No Network"
    private val noData: String = "No Data"
    private val exception = RuntimeException("Something Went Wrong")

    @Test
    fun getReposListResponseFromRepository() = runBlockingTest {
        val getReposUseCase = GetRepositoriesListUseCase(getReposListRepository)
        getReposUseCase.getRepositoriesList(pageModel, true)
        verify(getReposListRepository, times(1)).getRepositoriesList(true)
    }


    @Test
    fun emitReposListResponseFromRepository() = runBlockingTest {
        val useCase = initSuccessUseCase()
        val response = useCase.getRepositoriesList(pageModel, true).first()
        TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
        TestCase.assertEquals(RepoUiResponse(reposList = arrayListOf()), response.data)
    }

    @Test
    fun emitReposListResponseFromRepositoryButNoData() = runBlockingTest {
        val useCase = initSuccessUseCaseButNoData()
        val response = useCase.getRepositoriesList(pageModel, false).first()
        TestCase.assertEquals(StatusCode.NO_DATA, response.statusCode)
        TestCase.assertEquals(noData, response.error)
    }

    @Test
    fun propagateNoNetworkError() = runBlockingTest {
        val useCase = initNoNetworkUseCase()
        val response = useCase.getRepositoriesList(pageModel, true).first()
        TestCase.assertEquals(StatusCode.NO_NETWORK, response.statusCode)
        TestCase.assertEquals(noNetworkError, response.error)
    }

    @Test
    fun propagateGetReposListResponseError() = runBlockingTest {
        val useCase = initFailureUseCase()
        val response = useCase.getRepositoriesList(pageModel, true).first()
        TestCase.assertEquals(StatusCode.ERROR, response.statusCode)
        TestCase.assertEquals(exception.message, response.error)
    }


    private suspend fun initSuccessUseCase(): IGetRepositoriesListUseCase {
        whenever(getReposListRepository.getRepositoriesList(true)).thenReturn(
            flow {
                emit(Status.Success(reposListResponse))
            },
        )

        return GetRepositoriesListUseCase(
            getReposListRepository
        )
    }

    private suspend fun initSuccessUseCaseButNoData(): IGetRepositoriesListUseCase {
        whenever(getReposListRepository.getRepositoriesList(false)).thenReturn(
            flow {
                emit(Status.NoData(error = noData))
            },
        )

        return GetRepositoriesListUseCase(
            getReposListRepository
        )
    }

    private suspend fun initFailureUseCase(): IGetRepositoriesListUseCase {
        whenever(getReposListRepository.getRepositoriesList(true)).thenReturn(
            flow {
                emit(Status.Error(error = exception.message))
            },
        )

        return GetRepositoriesListUseCase(
            getReposListRepository
        )
    }

    private suspend fun initNoNetworkUseCase(): IGetRepositoriesListUseCase {
        whenever(getReposListRepository.getRepositoriesList(true)).thenReturn(
            flow {
                emit(Status.NoNetwork(error = noNetworkError))
            },
        )

        return GetRepositoriesListUseCase(
            getReposListRepository
        )
    }

}