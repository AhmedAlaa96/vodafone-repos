package com.ahmed.vodafonerepos.di

import android.content.Context
import androidx.room.Room
import com.ahmed.vodafonerepos.data.local.ILocalDataSource
import com.ahmed.vodafonerepos.data.local.LocalDataSource
import com.ahmed.vodafonerepos.data.remote.IRemoteDataSource
import com.ahmed.vodafonerepos.data.remote.RemoteDataSource
import com.ahmed.vodafonerepos.data.room.AppDatabase
import com.ahmed.vodafonerepos.data.sharedprefrences.IPreferencesDataSource
import com.ahmed.vodafonerepos.data.sharedprefrences.PreferencesDataSource
import com.ahmed.vodafonerepos.retrofit.ApiInterface
import com.ahmed.vodafonerepos.utils.connection_utils.ConnectionUtils
import com.ahmed.vodafonerepos.utils.connection_utils.IConnectionUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().serializeNulls().create()
    }
//
//    @Provides
//    @Singleton
//    fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
//        return Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java, "vodafone-repos"
//        )
//            .build()
//    }

    @Provides
    @Singleton
    fun provideConnectivity(@ApplicationContext context: Context): IConnectionUtils {
        return ConnectionUtils(context)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(appDatabase: AppDatabase): ILocalDataSource {
        return LocalDataSource(appDatabase)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiInterface: ApiInterface): IRemoteDataSource {
        return RemoteDataSource(apiInterface)
    }

    @Provides
    @Singleton
    fun providePreferencesHelper(
        @ApplicationContext context: Context,
        gson: Gson
    ): IPreferencesDataSource {
        return PreferencesDataSource(context, gson)
    }

    @Provides
    @IoDispatcher
    fun provideDispatcher(
    ): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(
    ): CoroutineDispatcher {
        return Dispatchers.Main
    }
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher