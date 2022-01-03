package com.openclassrooms.data.repository

import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import com.openclassrooms.data.dao.AppInfoDao
import com.openclassrooms.data.dao.NotificationInfoDao
import com.openclassrooms.data.entities.AppInfoData
import com.openclassrooms.data.entities.NotificationInfoData
import com.openclassrooms.data.model.AppInfo
import com.openclassrooms.data.model.NotificationInfo
import com.openclassrooms.data.services.ListApplicationsProvider
import com.openclassrooms.data.utils.toAppInfo
import com.openclassrooms.data.utils.toAppInfoData
import com.openclassrooms.data.utils.toNotificationInfo
import com.openclassrooms.data.utils.toNotificationInfoData

class Repository(
    private val appInfoDao: AppInfoDao,
    private val notificationInfoDao: NotificationInfoDao
): RepositoryAccess {

    private var addedApps = mutableListOf<AppInfoData>()
    private var removedApps = mutableListOf<AppInfoData>()

    // ------------------------------ table "apps" -------------------------------------------------
    override suspend fun insertAppInfoData(appInfoData: AppInfoData) {
        appInfoDao.insertAppInfoDataInDB(appInfoData)
    }

    override suspend fun removeAppInfoData(appInfoData: AppInfoData) {
        appInfoDao.removeAppInfoDataFromDB(appInfoData)
    }

    override suspend fun getAllAppInfo(packageManager: PackageManager): List<AppInfo> {
        val convertedList = mutableListOf<AppInfo>()
        appInfoDao.getAllAppsFromDB().forEach {
            convertedList.add(it.toAppInfo(packageManager))
        }
        return convertedList
    }

    /**
     * Updates "apps" table from database with current list of installed applications.
     */
    override suspend fun updateListAppsInstalled(packageManager: PackageManager) {
        // Get list of installed applications
        val listOfInstalledApps: MutableList<AppInfoData> = mutableListOf()
        ListApplicationsProvider.getAllInstalledApplications(packageManager).forEach {
            listOfInstalledApps.add(it.toAppInfoData())
        }

        // Get list from database
        val listInDb = appInfoDao.getAllAppsFromDB()

        if (listInDb.isEmpty()) {
            loadListInstalledAppsInDB(listOfInstalledApps)
        }
        else {
            compareListsApps(listOfInstalledApps, listInDb)
            refreshListOfAppsInDB()
        }
    }

    override suspend fun loadListInstalledAppsInDB(list: MutableList<AppInfoData>) {
        list.forEach {
            insertAppInfoData(it)
        }
    }

    override fun compareListsApps(newList: List<AppInfoData>, oldList: List<AppInfoData>) {
        val sum = newList + oldList
        // Get list of difference between the two lists
        val diff = sum.groupBy { it.packageName }
            .filter { it.value.size == 1 }
            .flatMap { it.value }
        // Updates list of added apps and removed apps
        diff.forEach {
            if (newList.contains(it)) { addedApps.add(it) }
            else { removedApps.add(it) }
        }
    }

    /**
     * Refreshes "apps" table with new updates (apps removed or added between previous and current
     * user session.
     */
    override suspend fun refreshListOfAppsInDB() {
            if (addedApps.isNotEmpty()) {
                addedApps.forEach { insertAppInfoData(it) }
                addedApps.clear()
            }
            if (removedApps.isNotEmpty()) {
                removedApps.forEach { removeAppInfoData(it) }
                removedApps.clear()
            }
    }

    // ------------------------------ table "notifications" ----------------------------------------
    override suspend fun insertNotificationInfoData(notificationInfoData: NotificationInfoData) {
        notificationInfoDao.insertNotificationInfoDataInDB(notificationInfoData)
    }

    override suspend fun removeNotificationInfoData(notificationInfoData: NotificationInfoData) {
        notificationInfoDao.removeNotificationInfoDataInDB(notificationInfoData)
    }

    override fun getAllStoredNotificationsData(): LiveData<List<NotificationInfoData>> {
        return notificationInfoDao.getAllStoredNotificationsInDB()
    }

    // ------------------------------ From NotificationBroadcastReceiver ---------------------------
    override suspend fun sendNewNotificationInfoToDB(notificationInfo: NotificationInfo) {
        val notificationInfoData = notificationInfo.toNotificationInfoData()
        notificationInfoDao.insertNotificationInfoDataInDB(notificationInfoData)
    }

    override fun test(): List<NotificationInfo> {
        var list: MutableList<NotificationInfo> = mutableListOf()
        val listToConvert = notificationInfoDao.test()
        listToConvert.forEach {
            list.add(it.toNotificationInfo())
        }
        return list
    }
}