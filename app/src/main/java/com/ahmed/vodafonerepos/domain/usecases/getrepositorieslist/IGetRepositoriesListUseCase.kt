package com.ahmed.vodafonerepos.domain.usecases.getrepositorieslist

import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoUiResponse
import com.ahmed.vodafonerepos.ui.base.IBaseUseCase
import kotlinx.coroutines.flow.Flow

interface IGetRepositoriesListUseCase: IBaseUseCase {
    fun getRepositoriesListLocally(pageModel: PageModel): Flow<Status<RepoUiResponse>>
}