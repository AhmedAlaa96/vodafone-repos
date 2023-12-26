package com.ahmed.vodafonerepos.data.local

import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.data.room.AppDatabase
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val appDatabase: AppDatabase) : ILocalDataSource {

    override suspend fun getAllRepos(): Status<ArrayList<RepoResponse>> {
        val reposList = appDatabase.reposDao().getAllRepos()?.toCollection(arrayListOf())
        return if (!reposList.isNullOrEmpty()) {
            Status.OfflineData(data = reposList, error = null)
        } else
            Status.NoData(error = "No Data")
    }

    override suspend fun insertRepos(reposList: ArrayList<RepoResponse>) {
        deleteAllRepos()
        appDatabase.reposDao().insertAll(reposList)
        println("HELLO")
    }

    private suspend fun deleteAllRepos() {
        appDatabase.reposDao().clearReposList()
    }

}