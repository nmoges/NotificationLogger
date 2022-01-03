package com.notificationlog.contracts

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import com.openclassrooms.data.entities.NotificationInfoData
import com.openclassrooms.data.model.NotificationInfo

interface ContractListNotif {

    // For updates of FragmentListNotif UI
    interface View {
        fun displayNewFragment(tag: String)
        fun updateRecyclerViewWithNewList(list: List<NotificationInfo>)
        fun displayAppDetailsDialog(view: android.view.View)
        fun updateProgressBarVisibilityStatus(visibility: Int)
    }

    // Actions from FragmentListNotif UI
    interface Presenter {
        fun clickOnAppsMenuItem()
        fun clickOnSearchMenuItem()
        fun getListOfNotificationsDataFromDB(): LiveData<List<NotificationInfoData>>
        fun getListOfNotificationsToDisplay(list : List<NotificationInfoData>)
        fun clickOnRecyclerViewItem(position: Int)
        fun clickOnRecyclerViewIconItem(position: Int, packageManager: PackageManager, context: Context)
    }
}