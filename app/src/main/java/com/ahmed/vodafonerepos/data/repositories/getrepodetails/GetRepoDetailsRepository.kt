package com.ahmed.vodafonerepos.data.repositories.getrepodetails

import com.ahmed.vodafonerepos.data.local.ILocalDataSource
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsResponse
import com.ahmed.vodafonerepos.data.remote.IRemoteDataSource
import com.ahmed.vodafonerepos.data.sharedprefrences.IPreferencesDataSource
import com.ahmed.vodafonerepos.di.IoDispatcher
import com.ahmed.vodafonerepos.ui.base.BaseRepository
import com.ahmed.vodafonerepos.utils.connection_utils.IConnectionUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepoDetailsRepository @Inject constructor(
    private val connectionUtils: IConnectionUtils,
    private val mIRemoteDataSource: IRemoteDataSource,
    mILocalDataSource: ILocalDataSource,
    private val mIPreferencesDataSource: IPreferencesDataSource,
    @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository(connectionUtils, mIRemoteDataSource, mIPreferencesDataSource, dispatcher),
    IGetRepoDetailsRepository {
    override fun getRepoDetails(repoDetailsRequest: RepoDetailsRequest?): Flow<Status<RepoDetailsResponse>> {
        return safeApiCalls {
            mIRemoteDataSource.getRepoDetails(repoDetailsRequest)
        }
    }
}