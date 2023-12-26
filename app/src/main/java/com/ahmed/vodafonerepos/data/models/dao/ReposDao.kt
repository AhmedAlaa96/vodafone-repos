package com.ahmed.vodafonerepos.data.models.dao

import androidx.room.*
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse

@Dao
interface ReposDao {
    @Query("SELECT * FROM repos")
    fun getAllRepos(): List<RepoResponse>?

    @Query("SELECT * FROM repos Where repoId=:id")
    fun getRepoById(id: Int?): RepoResponse?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRepo(repoResponse: RepoResponse)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(repoResponse: RepoResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reposList: ArrayList<RepoResponse>)

}
