package com.openclassrooms.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.openclassrooms.data.dao.AppInfoDao
import com.openclassrooms.data.dao.NotificationInfoDao
import com.openclassrooms.data.entities.AppInfoData
import com.openclassrooms.data.entities.NotificationInfoData

/**
 * Defines the application database.
 * Contains two tables :
 * - "apps" : see [AppInfoData] class
 * - "notifications" : see [NotificationInfoData] class
 */
@Database(entities = [AppInfoData::class, NotificationInfoData::class],
    version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {

    abstract val appInfoDao: AppInfoDao
    abstract val notificationInfoDao: NotificationInfoDao
}