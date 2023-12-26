package com.ahmed.vodafonerepos.data.repositories.getrepoissuesList

import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.ui.base.IBaseRepository
import kotlinx.coroutines.flow.Flow

interface IGetRepoIssuesListRepository : IBaseRepository {
    fun getRepoIssuesList(repoDetailsRequest: RepoDetailsRequest?): Flow<Status<ArrayList<RepoIssueResponse>>>
}