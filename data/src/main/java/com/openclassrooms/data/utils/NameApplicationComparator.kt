package com.openclassrooms.data.utils

import com.openclassrooms.data.model.AppInfo

object NameApplicationComparator: Comparator<AppInfo> {
    override fun compare(firstApp: AppInfo?, secondApp: AppInfo?): Int {
        var compare = 0
        if (firstApp != null && secondApp != null) {
            compare = firstApp.name.uppercase().compareTo(secondApp.name.uppercase())
        }
        return compare
    }
}

