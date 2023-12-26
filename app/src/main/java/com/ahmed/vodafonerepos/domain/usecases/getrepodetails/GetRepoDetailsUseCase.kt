package com.ahmed.vodafonerepos.domain.usecases.getrepodetails

import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.data.repositories.getrepodetails.IGetRepoDetailsRepository
import com.ahmed.vodafonerepos.ui.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepoDetailsUseCase @Inject constructor(private val repository: IGetRepoDetailsRepository) :
    BaseUseCase(repository), IGetRepoDetailsUseCase {
    override fun getRepoDetails(repoDetailsRequest: RepoDetailsRequest?): Flow<Status<RepoDetailsResponse>> {
        return repository.getRepoDetails(repoDetailsRequest)
    }
}