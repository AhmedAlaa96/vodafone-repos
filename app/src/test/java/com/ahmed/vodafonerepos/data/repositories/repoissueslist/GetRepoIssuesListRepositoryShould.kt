package com.ahmed.vodafonerepos.data.repositories.repoissueslist

import com.ahmed.vodafonerepos.data.local.ILocalDataSource
import com.ahmed.vodafonerepos.data.local.LocalDataSource
import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.data.remote.IRemoteDataSource
import com.ahmed.vodafonerepos.data.repositories.getrepoissuesList.GetRepoIssuesListRepository
import com.ahmed.vodafonerepos.data.repositories.getrepoissuesList.IGetRepoIssuesListRepository
import com.ahmed.vodafonerepos.data.sharedprefrences.IPreferencesDataSource
import com.ahmed.vodafonerepos.data.sharedprefrences.PreferencesDataSource
import com.ahmed.vodafonerepos.utils.BaseUnitTest
import com.ahmed.vodafonerepos.utils.connection_utils.ConnectionUtils
import com.ahmed.vodafonerepos.utils.connection_utils.IConnectionUtils
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test


@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
class GetRepoIssuesListRepositoryShould : BaseUnitTest() {

    private val remoteDataSource: IRemoteDataSource = mock()
    private val connectionUtils: IConnectionUtils = mock<ConnectionUtils>()
    private val localDataSource: ILocalDataSource = mock<LocalDataSource>()
    private val sharedPreferences: IPreferencesDataSource = mock<PreferencesDataSource>()
    private val repoIssuesListResponse: ArrayList<RepoIssueResponse> = mock()
    private val repoDetailsRequest = RepoDetailsRequest("test", "test")
    private val pageModel = PageModel()
    private val noNetworkError: String = "No Network"
    private val exception = RuntimeException("Something Went Wrong")

    @Test
    fun emitRepoIssuesListResponseFromRemoteDataSource() = runBlockingTest {
        val repository = initSuccessRepository()
        val response = repository.getRepoIssuesList(repoDetailsRequest, pageModel).first()
        assertEquals(StatusCode.SUCCESS, response.statusCode)
        assertEquals(repoIssuesListResponse, response.data)
    }

    @Test
    fun propagateNoNetworkError() = runBlockingTest {
        val repository = initNoNetworkRepository()
        val response = repository.getRepoIssuesList(repoDetailsRequest, pageModel).first()
        assertEquals(StatusCode.NO_NETWORK, response.statusCode)
        assertEquals(noNetworkError, response.error)

    }

    @Test
    fun propagateGetRepoIssuesListResponseError() = runBlockingTest {
        val repository = initFailureRepository()
        val response = repository.getRepoIssuesList(repoDetailsRequest, pageModel).first()
        assertEquals(StatusCode.ERROR, response.statusCode)
        assertEquals(exception.message, response.error)
    }


    private suspend fun initSuccessRepository(): IGetRepoIssuesListRepository {

        whenever(connectionUtils.isConnected).thenReturn(true)

        whenever(remoteDataSource.getRepoIssuesList(repoDetailsRequest, pageModel)).thenReturn(
            repoIssuesListResponse
        )
        return GetRepoIssuesListRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }

    private suspend fun initFailureRepository(): IGetRepoIssuesListRepository {

        whenever(connectionUtils.isConnected).thenReturn(true)

        whenever(remoteDataSource.getRepoIssuesList(repoDetailsRequest, pageModel)).thenThrow(
            exception
        )
        return GetRepoIssuesListRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }

    private fun initNoNetworkRepository(): IGetRepoIssuesListRepository {
        whenever(connectionUtils.isConnected).thenReturn(false)

        return GetRepoIssuesListRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }
}