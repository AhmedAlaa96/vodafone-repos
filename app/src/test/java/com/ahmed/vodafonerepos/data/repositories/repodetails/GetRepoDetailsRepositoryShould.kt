package com.ahmed.vodafonerepos.data.repositories.repodetails

import com.ahmed.vodafonerepos.data.local.ILocalDataSource
import com.ahmed.vodafonerepos.data.local.LocalDataSource
import com.ahmed.vodafonerepos.data.models.StatusCode
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.data.remote.IRemoteDataSource
import com.ahmed.vodafonerepos.data.remote.RemoteDataSource
import com.ahmed.vodafonerepos.data.repositories.getrepodetails.GetRepoDetailsRepository
import com.ahmed.vodafonerepos.data.repositories.getrepodetails.IGetRepoDetailsRepository
import com.ahmed.vodafonerepos.data.sharedprefrences.IPreferencesDataSource
import com.ahmed.vodafonerepos.data.sharedprefrences.PreferencesDataSource
import com.ahmed.vodafonerepos.utils.BaseUnitTest
import com.ahmed.vodafonerepos.utils.connection_utils.ConnectionUtils
import com.ahmed.vodafonerepos.utils.connection_utils.IConnectionUtils
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
class GetRepoDetailsRepositoryShould : BaseUnitTest() {

    private val remoteDataSource: IRemoteDataSource = mock<RemoteDataSource>()
    private val connectionUtils: IConnectionUtils = mock<ConnectionUtils>()
    private val localDataSource: ILocalDataSource = mock<LocalDataSource>()
    private val sharedPreferences: IPreferencesDataSource = mock<PreferencesDataSource>()
    private val repoDetailsRequest = RepoDetailsRequest("test", "test")
    private val repoDetailsResponse: RepoDetailsResponse = mock()
    private val noNetworkError: String = "No Network"
    private val exception = RuntimeException("Something Went Wrong")

    @Test
    fun emitRepoDetailsResponseFromRemoteDataSource() = runBlockingTest {
        val repository = initSuccessRepository()
        val response = repository.getRepoDetails(repoDetailsRequest).first()
        TestCase.assertEquals(StatusCode.SUCCESS, response.statusCode)
        TestCase.assertEquals(repoDetailsResponse, response.data)
    }

    @Test
    fun propagateNoNetworkError() = runBlockingTest {
        val repository = initNoNetworkRepository()
        val response = repository.getRepoDetails(repoDetailsRequest).first()
        TestCase.assertEquals(StatusCode.NO_NETWORK, response.statusCode)
        TestCase.assertEquals(noNetworkError, response.error)
    }

    @Test
    fun propagateGetRepoDetailsResponseError() = runBlockingTest {
        val repository = initFailureRepository()
        val response = repository.getRepoDetails(repoDetailsRequest).first()
        TestCase.assertEquals(StatusCode.ERROR, response.statusCode)
        TestCase.assertEquals(exception.message, response.error)
    }


    private suspend fun initSuccessRepository(): IGetRepoDetailsRepository {

        whenever(connectionUtils.isConnected).thenReturn(true)

        whenever(remoteDataSource.getRepoDetails(repoDetailsRequest)).thenReturn(
            repoDetailsResponse
        )
        return GetRepoDetailsRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }

    private suspend fun initFailureRepository(): IGetRepoDetailsRepository {

        whenever(connectionUtils.isConnected).thenReturn(true)

        whenever(remoteDataSource.getRepoDetails(repoDetailsRequest)).thenThrow(
            exception
        )
        return GetRepoDetailsRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }

    private fun initNoNetworkRepository(): IGetRepoDetailsRepository {

        whenever(connectionUtils.isConnected).thenReturn(false)

        return GetRepoDetailsRepository(
            connectionUtils,
            remoteDataSource,
            localDataSource,
            sharedPreferences,
            Dispatchers.Unconfined
        )
    }

}