package com.ahmed.vodafonerepos.data.remote

import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.retrofit.ApiInterface
import com.ahmed.vodafonerepos.utils.alternate

class RemoteDataSource(private val mRetrofitInterface: ApiInterface) : IRemoteDataSource {
    override suspend fun getReposList(): ArrayList<RepoResponse> {
        return mRetrofitInterface.getReposList()
    }

    override suspend fun getRepoDetails(repoDetailsRequest: RepoDetailsRequest?): RepoDetailsResponse {
        return mRetrofitInterface.getRepoDetails(
            repoDetailsRequest?.owner.alternate(),
            repoDetailsRequest?.repo.alternate()
        )
    }

    override suspend fun getRepoIssuesList(repoDetailsRequest: RepoDetailsRequest?): ArrayList<RepoIssueResponse> {
        return mRetrofitInterface.getRepoIssuesList(
            repoDetailsRequest?.owner.alternate(),
            repoDetailsRequest?.repo.alternate()
        )
    }

}