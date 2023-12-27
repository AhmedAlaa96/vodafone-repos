package com.ahmed.vodafonerepos.data.remote

import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.retrofit.ApiInterface
import com.ahmed.vodafonerepos.utils.BaseUnitTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("DEPRECATION")
@OptIn(ExperimentalCoroutinesApi::class)
class RemoteDateSourceShould : BaseUnitTest() {

    private val apiInterface: ApiInterface = mock()
    private val reposListResponse: ArrayList<RepoResponse> = mock()
    private val repoIssuesListResponse: ArrayList<RepoIssueResponse> = mock()
    private val repoDetailsResponse: RepoDetailsResponse = mock()
    private val pageModel = PageModel()
    private val repoDetailsRequest = RepoDetailsRequest("test", "test")

    @Test
    fun getReposListResponseFromAPI() = runBlockingTest {
        val remoteDataSource = RemoteDataSource(apiInterface)
        remoteDataSource.getReposList()
        verify(apiInterface, times(1)).getReposList()
    }

    @Test
    fun emitReposListResponseFromAPIService() = runBlockingTest {
        val remoteDataSource = initSuccessRemoteDataSource()
        assertEquals(reposListResponse, remoteDataSource.getReposList())
    }

    @Test
    fun getReposDetailsResponseFromAPI() = runBlockingTest {
        val remoteDataSource = RemoteDataSource(apiInterface)
        remoteDataSource.getRepoDetails(repoDetailsRequest)
        verify(apiInterface, times(1)).getRepoDetails(
            repoDetailsRequest.owner,
            repoDetailsRequest.repo
        )
    }

    @Test
    fun emitReposDetailsResponseFromAPIService() = runBlockingTest {
        val remoteDataSource = initSuccessRemoteDataSource()
        assertEquals(repoDetailsResponse, remoteDataSource.getRepoDetails(repoDetailsRequest))
    }


    @Test
    fun getRepoIssuesListResponseFromAPI() = runBlockingTest {
        val remoteDataSource = RemoteDataSource(apiInterface)
        remoteDataSource.getRepoIssuesList(repoDetailsRequest, pageModel)
        verify(apiInterface, times(1)).getRepoIssuesList(
            repoDetailsRequest.owner,
            repoDetailsRequest.repo,
            pageModel.page
        )
    }

    @Test
    fun emitRepoIssuesListResponseFromAPIService() = runBlockingTest {
        val remoteDataSource = initSuccessRemoteDataSource()
        assertEquals(
            repoIssuesListResponse,
            remoteDataSource.getRepoIssuesList(repoDetailsRequest, pageModel)
        )
    }

    private fun initSuccessRemoteDataSource(): IRemoteDataSource {
        runBlocking {
            whenever(apiInterface.getReposList()).thenReturn(reposListResponse)
            whenever(
                apiInterface.getRepoDetails(
                    repoDetailsRequest.owner,
                    repoDetailsRequest.repo
                )
            ).thenReturn(repoDetailsResponse)
            whenever(
                apiInterface.getRepoIssuesList(
                    repoDetailsRequest.owner,
                    repoDetailsRequest.repo,
                    pageModel.page
                )
            ).thenReturn(repoIssuesListResponse)
        }
        return RemoteDataSource(apiInterface)
    }

}