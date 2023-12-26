package com.ahmed.vodafonerepos.data.local

import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse

interface ILocalDataSource {

    suspend fun getAllRepos(): ArrayList<RepoResponse>
    suspend fun insertRepos(reposList: ArrayList<RepoResponse>)

}