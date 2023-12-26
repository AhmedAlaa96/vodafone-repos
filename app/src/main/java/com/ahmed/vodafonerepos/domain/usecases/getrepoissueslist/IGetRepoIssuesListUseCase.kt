package com.ahmed.vodafonerepos.domain.usecases.getrepoissueslist

import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.ui.base.IBaseUseCase
import kotlinx.coroutines.flow.Flow

interface IGetRepoIssuesListUseCase: IBaseUseCase {
    fun getRepoIssuesList(repoDetailsRequest: RepoDetailsRequest?, pageModel: PageModel): Flow<Status<ArrayList<RepoIssueResponse>>>
}