package com.ahmed.vodafonerepos.ui.repodetails.di

import com.ahmed.vodafonerepos.data.local.ILocalDataSource
import com.ahmed.vodafonerepos.data.remote.IRemoteDataSource
import com.ahmed.vodafonerepos.data.repositories.getrepodetails.GetRepoDetailsRepository
import com.ahmed.vodafonerepos.data.repositories.getrepodetails.IGetRepoDetailsRepository
import com.ahmed.vodafonerepos.data.sharedprefrences.IPreferencesDataSource
import com.ahmed.vodafonerepos.domain.usecases.getrepodetails.GetRepoDetailsUseCase
import com.ahmed.vodafonerepos.domain.usecases.getrepodetails.IGetRepoDetailsUseCase
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
abstract class RepoDetailsModule {
    companion object {
        @Singleton
        @Provides
        fun provideGetRepoDetailsRepository(
            connectionUtils: IConnectionUtils,
            mIRemoteDataSource: IRemoteDataSource,
            mILocalDataSource: ILocalDataSource,
            mIPreferencesDataSource: IPreferencesDataSource
        ): IGetRepoDetailsRepository {
            return GetRepoDetailsRepository(
                connectionUtils,
                mIRemoteDataSource,
                mILocalDataSource,
                mIPreferencesDataSource
            )
        }
    }

    @Singleton
    @Binds
    abstract fun bindIGetRepoDetailsUseCase(useCase: GetRepoDetailsUseCase): IGetRepoDetailsUseCase


}