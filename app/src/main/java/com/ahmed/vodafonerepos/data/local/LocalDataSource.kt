package com.ahmed.vodafonerepos.data.local

import com.ahmed.vodafonerepos.data.room.AppDatabase
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val appDatabase: AppDatabase) : ILocalDataSource {

}