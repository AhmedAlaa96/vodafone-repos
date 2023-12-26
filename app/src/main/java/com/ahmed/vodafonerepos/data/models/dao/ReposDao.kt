package com.ahmed.vodafonerepos.data.models.dao

import androidx.room.*
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse

@Dao
interface ReposDao {
    @Query("SELECT * FROM repos")
    fun getAllRepos(): List<RepoResponse>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reposList: ArrayList<RepoResponse>)


    @Query("DELETE FROM repos")
    suspend fun clearReposList()
}