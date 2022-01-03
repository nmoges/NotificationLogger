package com.notificationlog.permission

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.notificationlog.AppInfo
import com.notificationlog.NotificationLogService

object PermissionAccess {

    fun accessSettingsInDevice(context: Context) {
        context.startActivity(Intent(AppInfo.NOTIFICATION_PERMISSION))
    }

    fun checkIfNotificationListenerAccessIsEnabled(context: Context): Boolean {
        val service = ComponentName(context, NotificationLogService::class.java)
        val apps = Settings.Secure.getString(context.contentResolver, AppInfo.TABLE_NAME)
        return apps.contains(service.flattenToString())
    }
}