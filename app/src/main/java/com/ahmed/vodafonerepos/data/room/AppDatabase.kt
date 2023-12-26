package com.ahmed.vodafonerepos.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmed.vodafonerepos.data.models.dao.ReposDao
import com.ahmed.vodafonerepos.data.models.dto.RepoResponse
import com.ahmed.vodafonerepos.data.roommodels.OwnerConverter

@TypeConverters(OwnerConverter::class)
@Database(entities = [RepoResponse::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reposDao(): ReposDao
}