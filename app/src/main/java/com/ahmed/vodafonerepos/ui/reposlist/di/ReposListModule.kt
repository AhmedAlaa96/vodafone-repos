package com.ahmed.vodafonerepos.ui.reposlist.di

import com.ahmed.vodafonerepos.data.local.ILocalDataSource
import com.ahmed.vodafonerepos.data.remote.IRemoteDataSource
import com.ahmed.vodafonerepos.data.repositories.getrepositorieslist.GetRepositoriesListRepository
import com.ahmed.vodafonerepos.data.repositories.getrepositorieslist.IGetRepositoriesListRepository
import com.ahmed.vodafonerepos.data.sharedprefrences.IPreferencesDataSource
import com.ahmed.vodafonerepos.domain.usecases.getrepositorieslist.GetRepositoriesListUseCase
import com.ahmed.vodafonerepos.domain.usecases.getrepositorieslist.IGetRepositoriesListUseCase
import com.ahmed.vodafonerepos.retrofit.RetrofitModule
import com.ahmed.vodafonerepos.utils.connection_utils.IConnectionUtils
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class])
@InstallIn(SingletonComponent::class)
abstract class ReposListModule {
    companion object {
        @Singleton
        @Provides
        fun provideGetReposListRepository(
            connectionUtils: IConnectionUtils,
            mIRemoteDataSource: IRemoteDataSource,
            mILocalDataSource: ILocalDataSource,
            mIPreferencesDataSource: IPreferencesDataSource
        ): IGetRepositoriesListRepository {
            return GetRepositoriesListRepository(
                connectionUtils,
                mIRemoteDataSource,
                mILocalDataSource,
                mIPreferencesDataSource
            )
        }
    }

    @Singleton
    @Binds
    abstract fun bindIGetRepositoriesListUseCase(useCase: GetRepositoriesListUseCase): IGetRepositoriesListUseCase


}