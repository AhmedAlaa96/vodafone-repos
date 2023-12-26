package com.ahmed.vodafonerepos.data.repositories.getrepositorieslist

import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.ui.base.IBaseRepository
import kotlinx.coroutines.flow.Flow

interface IGetRepositoriesListRepository: IBaseRepository {
    fun getRepositoriesList(shouldCall: Boolean): Flow<Status<ArrayList<RepoResponse>>>
}