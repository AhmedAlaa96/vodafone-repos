package com.ahmed.vodafonerepos.data.repositories.getrepodetails

import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.ui.base.IBaseRepository
import kotlinx.coroutines.flow.Flow

interface IGetRepoDetailsRepository: IBaseRepository {
    fun getRepoDetails(repoDetailsRequest: RepoDetailsRequest?): Flow<Status<RepoDetailsResponse>>
}