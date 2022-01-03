package com.openclassrooms.data.dao;

import androidx.room.*
import com.openclassrooms.data.entities.AppInfoData

/**
 * DAO interface to access [Database] "apps" table.
 */
@Dao
interface AppInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAppInfoDataInDB(appInfoData: AppInfoData): Long

    @Delete
    suspend fun removeAppInfoDataFromDB(appInfoData: AppInfoData)

    @Query("SELECT * FROM apps")
    suspend fun getAllAppsFromDB(): List<AppInfoData>
}