package com.openclassrooms.data.services

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.openclassrooms.data.model.AppInfo
import com.openclassrooms.data.utils.NameApplicationComparator
import java.util.*

object ListApplicationsProvider {

    @SuppressLint("QueryPermissionsNeeded")
    fun getAllInstalledApplications(packageManager: PackageManager): MutableList<AppInfo> {
        val listInstalledApps: MutableList<AppInfo> = mutableListOf()
        packageManager.let {
            it.getInstalledApplications(PackageManager.GET_META_DATA).forEach { item ->
                if (item.flags and ApplicationInfo.FLAG_SYSTEM != ApplicationInfo.FLAG_SYSTEM) {
                    val appInfo = AppInfo(
                        name = it.getApplicationLabel(item).toString(),
                        packageName = item.packageName,
                        category = item.category,
                        icon = item.loadIcon(it),
                        filterStatus = 1
                    )
                    listInstalledApps.add(appInfo)
                }
            }
            Collections.sort(listInstalledApps, NameApplicationComparator)
        }
        return listInstalledApps
    }
}