package com.ahmed.vodafonerepos.data.local

import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.data.room.AppDatabase
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val appDatabase: AppDatabase) : ILocalDataSource {

    override suspend fun getAllRepos(): ArrayList<RepoResponse> {
        return appDatabase.reposDao().getAllRepos()?.toCollection(arrayListOf()) ?: arrayListOf()
    }

    override suspend fun insertRepos(reposList: ArrayList<RepoResponse>) {
        val reposListLocally = appDatabase.reposDao().getAllRepos()
        if (reposListLocally.isNullOrEmpty())
            appDatabase.reposDao().insertAll(reposList)
        else {
            reposList.forEach { repo ->
                val existingRepo = appDatabase.reposDao().getRepoById(repo.repoId)
                if (existingRepo == null) {
                    appDatabase.reposDao().insertRepo(repo)
                } else {
                    appDatabase.reposDao().updateRepo(repo)
                }
            }
        }
    }

}