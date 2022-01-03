package com.notificationlog.contracts

import android.content.pm.PackageManager
import com.openclassrooms.data.model.AppInfo

/**
 * Interface defining interactions between [Fragment]
 */
interface ContractListApps {

    // For updates of FragmentListApps UI
    interface View {
        fun displayListOfApps(list: List<AppInfo>)
        fun updateProgressBarVisibilityStatus(visibility: Int)
    }

    // Actions from FragmentListApps UI
    interface Presenter {
        fun getListOfAppsFromDB(packageManager: PackageManager)
        fun updateRecyclerViewWithNewList()
    }
}