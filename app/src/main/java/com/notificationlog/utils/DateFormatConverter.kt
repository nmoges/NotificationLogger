package com.notificationlog.utils

import java.util.*

object DateFormatConverter {

    fun convertTimeInReadableDate(time: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = time
        }
        val day = if (calendar.get(Calendar.DAY_OF_MONTH) < 10) "0${calendar.get(Calendar.DAY_OF_MONTH)}"
                  else "${calendar.get(Calendar.DAY_OF_MONTH)}"
        val month = if (calendar.get(Calendar.MONTH) + 1 < 10) "0${calendar.get(Calendar.MONTH) + 1}"
                    else "${calendar.get(Calendar.MONTH) + 1}"
        val year = calendar.get(Calendar.YEAR)
        val hour = if (calendar.get(Calendar.HOUR_OF_DAY) < 10) "0${calendar.get(Calendar.HOUR_OF_DAY)}"
                   else "${calendar.get(Calendar.HOUR_OF_DAY)}"
        val minutes = if (calendar.get(Calendar.MINUTE) < 10) "0${calendar.get(Calendar.MINUTE)}"
                      else "${calendar.get(Calendar.MINUTE)}"
        return "$month/$day/$year $hour:$minutes"
    }
}