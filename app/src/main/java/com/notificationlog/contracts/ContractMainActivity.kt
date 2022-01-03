package com.notificationlog.contracts

import android.content.pm.PackageManager

interface ContractMainActivity {

    interface View {
        fun updateFloatingActionButtonStatus()
        fun updateFloatingActionButtonIcon()
        fun updateSharedPreferencesFile()
        fun displayToastListenerStatus()
    }

    interface Presenter {
        fun updateListAppsInstalled(packageManager: PackageManager)
        fun clickOnFloatingActionButton()
    }
}