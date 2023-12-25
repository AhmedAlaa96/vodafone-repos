package com.ahmed.vodafonerepos.data.remote

import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse

interface IRemoteDataSource {
    suspend fun getReposList(): ArrayList<RepoResponse>
    suspend fun getRepoDetails(repoDetailsRequest: RepoDetailsRequest?): RepoDetailsResponse
    suspend fun getRepoIssuesList(repoDetailsRequest: RepoDetailsRequest?): ArrayList<RepoIssueResponse>
}