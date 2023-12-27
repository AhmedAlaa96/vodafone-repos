package com.ahmed.vodafonerepos.data.repositories.repolist

import com.ahmed.vodafonerepos.data.local.ILocalDataSource
import com.ahmed.vodafonerepos.data.local.LocalDataSource
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.data.remote.IRemoteDataSource
import com.ahmed.vodafonerepos.data.repositories.getrepositorieslist.GetRepositoriesListRepository
import com.ahmed.vodafonerepos.data.repositories.getrepositorieslist.IGetRepositoriesListRepository
import com.ahmed.vodafonerepos.data.sharedprefrences.IPreferencesDataSource
import com.ahmed.vodafonerepos.data.sharedprefrences.PreferencesDataSource
import com.ahmed.vodafonerepos.utils.BaseUnitTest
import com.ahmed.vodafonerepos.utils.connection_utils.ConnectionUtils
import com.ahmed.vodafonerepos.utils.connection_utils.IConnectionUtils
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test


@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
class GetReposListRepositoryShould : BaseUnitTest() {

    private val remoteDataSource: IRemoteDataSource = mock()
    private val connectionUtils: IConnectionUtils = mock<ConnectionUtils>()
    private val localDataSource: ILocalDataSource = mock<LocalDataSource>()
    private val sharedPreferences: IPreferencesDataSource = mock<PreferencesDataSource>()
    private val reposListResponse: ArrayList<RepoResponse> = mock()
    private val noNetworkError: String = "No Network"
    private val exception = RuntimeException("Something Went Wrong")

    @Test
    fun emitReposListResponseFromRemoteDataSource() = runBlockingTest {
        val repository = initSuccessRepository()
        val response = repository.getRepositoriesList(true).first()
        assertEquals(StatusCode.SUCCESS, response.statusCode)
        assertEquals(reposListResponse, response.data)
    }

    @Test
    fun emitReposListResponseFromLocalDataSourceWhenNoNetwork() = runBlockingTest {
        val repository = initNoNetworkButOfflineDataRepository()
        val response = repository.getRepositoriesList(true).first()
        assertEquals(StatusCode.OFFLINE_DATA, response.statusCode)
        assertEquals(reposListResponse, response.data)
        assertNull(response.error)
    }

    @Test
    fun emitReposListResponseFromLocalDataSource() = runBlockingTest {
        val repository = initSuccessRepositoryWithLocalData(reposListResponse)
        val response = repository.getRepositoriesList(false).first()
        assertEquals(StatusCode.SUCCESS, response.statusCode)
        assertEquals(reposListResponse, response.data)
    }

    @Test
    fun emitReposListResponseFromLocalDataSourceButNeedToCallAPI() = runBlockingTest {
        val repository = initSuccessRepositoryWithLocalData(arrayListOf())
        val response = repository.getRepositoriesList(false).first()
        assertEquals(StatusCode.SUCCESS, response.statusCode)
        assertEquals(reposListResponse, response.data)
    }

    @Test
    fun propagateNoNetworkError() = runBlockingTest {
        val repository = initNoNetworkRepository()
        val response = repository.getRepositoriesList(true).first()
        assertEquals(StatusCode.NO_NETWORK, response.statusCode)
        assertEquals(noNetworkError, response.error)

    }

    @Test
    fun propagateGetReposListResponseError() = runBlockingTest {
        val repository = initFailureRepository()
        val response = repository.getRepositoriesList(true).first()
        assertEquals(StatusCode.ERROR, response.statusCode)
        assertEquals(exception.message, response.error)
    }


    private suspend fun initSuccessRepository(): IGetRepositoriesListRepository {

        whenever(connectionUtils.isConnected).thenReturn(true)

        whenever(remoteDataSource.getReposList()).thenReturn(
            reposListResponse
        )
        return GetRepositoriesListRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }


    private suspend fun initSuccessRepositoryWithLocalData(mockedList: ArrayList<RepoResponse>): IGetRepositoriesListRepository {

        whenever(connectionUtils.isConnected).thenReturn(true)

        whenever(localDataSource.getAllRepos()).thenReturn(mockedList)

        whenever(remoteDataSource.getReposList()).thenReturn(
            reposListResponse
        )

        return GetRepositoriesListRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }

    private suspend fun initFailureRepository(): IGetRepositoriesListRepository {

        whenever(connectionUtils.isConnected).thenReturn(true)

        whenever(remoteDataSource.getReposList()).thenThrow(
            exception
        )
        return GetRepositoriesListRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }

    private suspend fun initNoNetworkButOfflineDataRepository(): IGetRepositoriesListRepository {
        whenever(connectionUtils.isConnected).thenReturn(false)
        whenever(localDataSource.getAllRepos()).thenReturn(reposListResponse)

        return GetRepositoriesListRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }

    private suspend fun initNoNetworkRepository(): IGetRepositoriesListRepository {
        whenever(connectionUtils.isConnected).thenReturn(false)
        whenever(localDataSource.getAllRepos()).thenReturn(arrayListOf())

        return GetRepositoriesListRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }
}