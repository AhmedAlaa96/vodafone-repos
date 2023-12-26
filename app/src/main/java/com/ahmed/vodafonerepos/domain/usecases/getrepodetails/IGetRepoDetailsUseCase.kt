package com.ahmed.vodafonerepos.domain.usecases.getrepodetails

import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.ui.base.IBaseUseCase
import kotlinx.coroutines.flow.Flow

interface IGetRepoDetailsUseCase: IBaseUseCase {
    fun getRepoDetails(repoDetailsRequest: RepoDetailsRequest?): Flow<Status<RepoDetailsResponse>>


}