package com.openclassrooms.data.di

import android.content.Context
import androidx.room.Room
import com.openclassrooms.data.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : Database {
        return Room.databaseBuilder(context, Database::class.java, "application_database")
            .fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideAppInfoDao(db: Database) = db.appInfoDao

    @Singleton
    @Provides
    fun provideNotificationInfoDao(db: Database) = db.notificationInfoDao
}