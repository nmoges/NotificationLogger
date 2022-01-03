package com.openclassrooms.data.di

import com.openclassrooms.data.dao.AppInfoDao
import com.openclassrooms.data.dao.NotificationInfoDao
import com.openclassrooms.data.repository.Repository
import com.openclassrooms.data.repository.RepositoryAccess
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(appInfoDao: AppInfoDao,
                          notificationInfoDao: NotificationInfoDao
    ): RepositoryAccess {
        return Repository(appInfoDao, notificationInfoDao)
    }
}