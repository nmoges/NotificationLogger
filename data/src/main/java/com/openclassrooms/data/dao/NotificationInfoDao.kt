package com.openclassrooms.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.openclassrooms.data.entities.NotificationInfoData

/**
 * DAO interface to access [Database] "notifications" table.
 */
@Dao
interface NotificationInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotificationInfoDataInDB(notificationInfoData: NotificationInfoData): Long

    @Delete
    suspend fun removeNotificationInfoDataInDB(notificationInfoData: NotificationInfoData)

    @Query("SELECT * FROM notifications")
    fun getAllStoredNotificationsInDB(): LiveData<List<NotificationInfoData>>

    @Query("SELECT * FROM notifications")
    fun test(): List<NotificationInfoData>
}