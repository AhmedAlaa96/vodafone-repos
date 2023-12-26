package com.ahmed.vodafonerepos.data.repositories.getrepositorieslist

import com.ahmed.vodafonerepos.data.local.ILocalDataSource
import com.ahmed.vodafonerepos.data.models.PageModel
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.data.remote.IRemoteDataSource
import com.ahmed.vodafonerepos.data.sharedprefrences.IPreferencesDataSource
import com.ahmed.vodafonerepos.di.IoDispatcher
import com.ahmed.vodafonerepos.ui.base.BaseRepository
import com.ahmed.vodafonerepos.utils.connection_utils.IConnectionUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetRepositoriesListRepository @Inject constructor(
    private val connectionUtils: IConnectionUtils,
    private val mIRemoteDataSource: IRemoteDataSource,
    private val mILocalDataSource: ILocalDataSource,
    mIPreferencesDataSource: IPreferencesDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository(connectionUtils, mIRemoteDataSource, mIPreferencesDataSource, dispatcher),
    IGetRepositoriesListRepository {
    override fun getRepositoriesList(shouldCall: Boolean): Flow<Status<ArrayList<RepoResponse>>> {
        return if (connectionUtils.isConnected) {
            safeApiCalls {
                if (shouldCall) {
                    return@safeApiCalls fetchReposList()
                } else {
                    val reposList = mILocalDataSource.getAllRepos()
                    if (reposList.isNotEmpty()) {
                        return@safeApiCalls reposList
                    } else {
                        return@safeApiCalls fetchReposList()
                    }
                }
            }
        } else {
            flow {
                val reposList = mILocalDataSource.getAllRepos()
                if (reposList.isNotEmpty()) {
                    emit(Status.OfflineData(reposList, null))
                } else {
                    emit(Status.NoNetwork(error = "No Network"))
                }
            }.flowOn(dispatcher)
        }

    }

    private suspend fun fetchReposList(): ArrayList<RepoResponse> {
        val list = mIRemoteDataSource.getReposList()
        if (list.isNotEmpty()) {
            mILocalDataSource.insertRepos(list)
        }
        return list
    }
}