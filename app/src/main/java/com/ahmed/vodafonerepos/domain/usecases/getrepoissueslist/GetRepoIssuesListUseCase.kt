package com.ahmed.vodafonerepos.domain.usecases.getrepoissueslist

import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.data.repositories.getrepoissuesList.IGetRepoIssuesListRepository
import com.ahmed.vodafonerepos.ui.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepoIssuesListUseCase @Inject constructor(private val repository: IGetRepoIssuesListRepository) :
    BaseUseCase(repository), IGetRepoIssuesListUseCase {
    override fun getRepoIssuesList(repoDetailsRequest: RepoDetailsRequest?): Flow<Status<ArrayList<RepoIssueResponse>>> {
        return repository.getRepoIssuesList(repoDetailsRequest)
    }
}