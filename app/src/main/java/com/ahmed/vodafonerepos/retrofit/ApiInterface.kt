package com.ahmed.vodafonerepos.retrofit

import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.utils.Constants
import com.ahmed.vodafonerepos.utils.Constants.URL.OWNER
import com.ahmed.vodafonerepos.utils.Constants.URL.REPO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {

    @GET(Constants.URL.GET_REPOS_LIST)
    suspend fun getReposList(): ArrayList<RepoResponse>

    @GET(Constants.URL.GET_REPO_DETAILS)
    suspend fun getRepoDetails(
        @Path(OWNER) owner: String,
        @Path(REPO) repo: String,
    ): RepoDetailsResponse

    @GET(Constants.URL.GET_REPO_ISSUES)
    suspend fun getRepoIssuesList(
        @Path(OWNER) owner: String,
        @Path(REPO) repo: String,
        @Query("page") page: Int
    ): ArrayList<RepoIssueResponse>


}
