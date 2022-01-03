package com.openclassrooms.data.model

import android.graphics.drawable.Drawable

class AppInfo(
    val name: String,
    val packageName: String,
    val category: Int,
    val icon: Drawable,
    val filterStatus: Int
)