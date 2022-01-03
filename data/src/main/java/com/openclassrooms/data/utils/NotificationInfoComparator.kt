package com.openclassrooms.data.utils

import com.openclassrooms.data.model.NotificationInfo

object NotificationInfoComparator: Comparator<NotificationInfo> {
    override fun compare(firstNotif: NotificationInfo?, secondNotif: NotificationInfo?): Int {
        var compare = 0
        if (firstNotif != null && secondNotif != null) {
            compare = if (firstNotif.packageName == secondNotif.packageName
                            && firstNotif.title == secondNotif.title
                            && firstNotif.message == secondNotif.message
                            && firstNotif.date == secondNotif.date) {
                        0
                        } else 1
        }
        return compare
    }
}