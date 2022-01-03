package com.openclassrooms.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationInfoData(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var message: String,
    var packageName: String,
    var date: String
)