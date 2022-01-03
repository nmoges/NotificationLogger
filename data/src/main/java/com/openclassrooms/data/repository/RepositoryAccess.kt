package com.openclassrooms.data.repository

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import com.openclassrooms.data.entities.AppInfoData
import com.openclassrooms.data.entities.NotificationInfoData
import com.openclassrooms.data.model.AppInfo
import com.openclassrooms.data.model.NotificationInfo

interface RepositoryAccess {
    suspend fun insertAppInfoData(appInfoData: AppInfoData)

    suspend fun removeAppInfoData(appInfoData: AppInfoData)

    suspend fun getAllAppInfo(packageManager: PackageManager): List<AppInfo>

    suspend fun updateListAppsInstalled(packageManager: PackageManager)

    suspend fun loadListInstalledAppsInDB(list: MutableList<AppInfoData>)

    fun compareListsApps(newList: List<AppInfoData>, oldList: List<AppInfoData>)

    suspend fun refreshListOfAppsInDB()

    suspend fun insertNotificationInfoData(notificationInfoData: NotificationInfoData)

    suspend fun removeNotificationInfoData(notificationInfoData: NotificationInfoData)

    fun getAllStoredNotificationsData(): LiveData<List<NotificationInfoData>>

    fun test(): List<NotificationInfo>

    suspend fun sendNewNotificationInfoToDB(notificationInfo: NotificationInfo)
}