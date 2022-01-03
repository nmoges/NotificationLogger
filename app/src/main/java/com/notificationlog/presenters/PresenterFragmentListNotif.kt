package com.notificationlog.presenters

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.LiveData
import com.google.android.material.textview.MaterialTextView
import com.notificationlog.R
import com.notificationlog.contracts.ContractListNotif
import com.notificationlog.ui.fragments.FragmentListApps
import com.notificationlog.ui.fragments.FragmentListNotif
import com.notificationlog.ui.fragments.FragmentSearch
import com.notificationlog.utils.LayoutInflaterProvider
import com.openclassrooms.data.entities.NotificationInfoData
import com.openclassrooms.data.model.NotificationInfo
import com.openclassrooms.data.repository.RepositoryAccess
import com.openclassrooms.data.utils.toNotificationInfo
import javax.inject.Inject

/**
 * Presenter for [FragmentListNotif].
 */
class PresenterFragmentListNotif
@Inject constructor(private val repositoryAccess: RepositoryAccess): ContractListNotif.Presenter {

    var view: ContractListNotif.View? = null
    var context: Context? = null
    private val convertedList: MutableList<NotificationInfo> = mutableListOf()

    override fun clickOnAppsMenuItem() {
        view?.displayNewFragment(FragmentListApps.TAG)
    }

    override fun clickOnSearchMenuItem() {
        view?.displayNewFragment(FragmentSearch.TAG)
    }

    override fun getListOfNotificationsDataFromDB(): LiveData<List<NotificationInfoData>> {
           return repositoryAccess.getAllStoredNotificationsData()
    }

    override fun getListOfNotificationsToDisplay(list : List<NotificationInfoData>) {
        convertedList.clear()
        list.forEach {
            convertedList.add(it.toNotificationInfo())
        }
        convertedList.reverse()
        view?.let {
            it.updateProgressBarVisibilityStatus(View.INVISIBLE)
            it.updateRecyclerViewWithNewList(convertedList)
        }
    }

    override fun clickOnRecyclerViewItem(position: Int) {
        Log.i("ITEM", "$position")
    }

    override fun clickOnRecyclerViewIconItem(position: Int,
                                             packageManager: PackageManager,
                                             context: Context) {
        // Calculate values to display in Dialog for a specific app
        val packageName = convertedList[position].packageName
        val numberNotifs = calculateNumberOfNotifications(packageName)
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        val nameApp = packageManager.getApplicationLabel(applicationInfo)
        val imageDrawable = packageManager.getApplicationIcon(applicationInfo)
        // Define Dialog view
        val viewDialog = LayoutInflaterProvider.getViewFromLayoutInflater(R.layout.dialog_app_details, context)
        viewDialog?.apply {
            this.findViewById<AppCompatImageView>(R.id.dialog_app_icon).setImageDrawable(imageDrawable)
            this.findViewById<MaterialTextView>(R.id.dialog_app_name).text = nameApp.toString()
            this.findViewById<MaterialTextView>(R.id.dialog_app_package).text = packageName
            this.findViewById<MaterialTextView>(R.id.dialog_app_nb_notifications).text =
                                              getTextToDisplayForNumberOfNotifications(numberNotifs)
            view?.displayAppDetailsDialog(viewDialog)
        }
    }

    private fun calculateNumberOfNotifications(namePackage: String): Int {
        var number = 0
        convertedList.forEach {
            if (it.packageName == namePackage) number++
        }
        return number
    }

    private fun getTextToDisplayForNumberOfNotifications(numberNotifs: Int): String {
        var text = ""
        context?.let {
            text = if (numberNotifs == 1)  it.resources.getString(R.string.one_notif_received)
                   else it.resources.getString(R.string.several_notif_received, numberNotifs)
        }
        return text
    }
}