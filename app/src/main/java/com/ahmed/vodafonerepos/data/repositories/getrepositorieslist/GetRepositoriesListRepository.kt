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
    private val mIPreferencesDataSource: IPreferencesDataSource,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRepository(connectionUtils, mIRemoteDataSource, mIPreferencesDataSource, dispatcher),
    IGetRepositoriesListRepository {
    override fun getRepositoriesList(): Flow<Status<ArrayList<RepoResponse>>> {
        return safeApiCalls {
            mIRemoteDataSource.getReposList()
        }
    }

    override fun getReposListLocally(pageModel: PageModel): Flow<Status<ArrayList<RepoResponse>>> {
        return flow {
            val reposStatus = mILocalDataSource.getAllRepos()
            if (reposStatus is Status.OfflineData) {
                val reposList =
                    reposStatus.data?.subList(
                        getFromIndex(pageModel.page - 1, reposStatus.data.size),
                        getToIndex(pageModel.page, reposStatus.data.size),
                    )
                        ?.toCollection(
                            arrayListOf()
                        )
                emit(
                    Status.OfflineData(
                        reposList,
                        error = null
                    )
                )
            } else {
                emit(Status.NoData(error = "No Data"))
            }
        }.flowOn((dispatcher))
    }

    private fun getFromIndex(page: Int, listSize: Int): Int {
        return if ((listSize - (page * 10)) < 10) listSize - 1
        else
            (page * 10)
    }

    private fun getToIndex(page: Int, listSize: Int): Int {
        return if ((listSize - (page * 10)) < 10) listSize - 1
        else
            (page * 10) - 1
    }

    override fun insertReposListLocally(reposList: ArrayList<RepoResponse>): Flow<Unit> {
        return flow { emit(mILocalDataSource.insertRepos(reposList)) }.flowOn((dispatcher))
    }
}