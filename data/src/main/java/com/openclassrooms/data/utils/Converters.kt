package com.openclassrooms.data.utils

import android.content.pm.PackageManager
import com.openclassrooms.data.entities.AppInfoData
import com.openclassrooms.data.entities.NotificationInfoData
import com.openclassrooms.data.model.AppInfo
import com.openclassrooms.data.model.NotificationInfo

fun AppInfo.toAppInfoData() = AppInfoData(
    name = this.name,
    packageName = this.packageName,
    category = this.category,
    filterStatus = this.filterStatus
)

fun AppInfoData.toAppInfo(packageManager: PackageManager): AppInfo {
    val drawable = packageManager.getApplicationIcon(this.packageName)
    return AppInfo(
        name = name,
        packageName = packageName,
        category = category,
        icon = drawable,
        filterStatus = filterStatus
    )
}

fun NotificationInfo.toNotificationInfoData() = NotificationInfoData(
    title = this.title,
    message = this.message,
    packageName = this.packageName,
    date = this.date
)

fun NotificationInfoData.toNotificationInfo() = NotificationInfo(
        title = this.title,
        message = this.message,
        packageName = this.packageName,
        date = this.date)

