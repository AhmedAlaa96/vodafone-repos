package com.ahmed.vodafonerepos.domain.usecases.getrepositorieslist

import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.data.models.dto.RepoUiResponse
import com.ahmed.vodafonerepos.ui.base.IBaseUseCase
import kotlinx.coroutines.flow.Flow

interface IGetRepositoriesListUseCase: IBaseUseCase {
    fun getRepositoriesList(pageModel: PageModel,  shouldCall: Boolean): Flow<Status<RepoUiResponse>>
}