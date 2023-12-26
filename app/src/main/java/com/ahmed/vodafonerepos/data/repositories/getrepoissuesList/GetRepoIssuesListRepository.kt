package com.ahmed.vodafonerepos.data.repositories.getrepoissuesList

import com.ahmed.vodafonerepos.data.local.ILocalDataSource
import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoIssueResponse
import com.ahmed.vodafonerepos.data.remote.IRemoteDataSource
import com.ahmed.vodafonerepos.data.sharedprefrences.IPreferencesDataSource
import com.ahmed.vodafonerepos.di.IoDispatcher
import com.ahmed.vodafonerepos.ui.base.BaseRepository
import com.ahmed.vodafonerepos.utils.connection_utils.IConnectionUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepoIssuesListRepository @Inject constructor(
    private val connectionUtils: IConnectionUtils,
    private val mIRemoteDataSource: IRemoteDataSource,
    mILocalDataSource: ILocalDataSource,
    private val mIPreferencesDataSource: IPreferencesDataSource,
    @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository(connectionUtils, mIRemoteDataSource, mIPreferencesDataSource, dispatcher),
    IGetRepoIssuesListRepository {
    override fun getRepoIssuesList(repoDetailsRequest: RepoDetailsRequest?, pageModel: PageModel): Flow<Status<ArrayList<RepoIssueResponse>>> {
        return safeApiCalls {
            mIRemoteDataSource.getRepoIssuesList(repoDetailsRequest, pageModel)
        }
    }
}