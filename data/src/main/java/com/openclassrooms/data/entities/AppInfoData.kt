package com.openclassrooms.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class AppInfoData(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var packageName: String,
    var category: Int,
    var filterStatus: Int
)