package com.notificationlog.presenters

import android.content.pm.PackageManager
import android.view.View
import com.notificationlog.contracts.ContractListApps
import com.openclassrooms.data.model.AppInfo
import com.openclassrooms.data.repository.RepositoryAccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.notificationlog.ui.fragments.FragmentListApps

/**
 * Presenter for [FragmentListApps].
 */
class PresenterFragmentListApps
@Inject constructor(private val repositoryAccess: RepositoryAccess) : ContractListApps.Presenter {

    var view: ContractListApps.View? = null
    private var list: List<AppInfo> = listOf()

    /**
     * Retrieves the list of all installed apps information from DB.
     */
    override fun getListOfAppsFromDB(packageManager: PackageManager) {
        CoroutineScope(Dispatchers.IO).launch {
            list = repositoryAccess.getAllAppInfo(packageManager)
        }.invokeOnCompletion {
            updateRecyclerViewWithNewList()
        }
    }

    /**
     * Updates the RecyclerView with the list of all installed aps information.
     */
    override fun updateRecyclerViewWithNewList() {
        CoroutineScope(Dispatchers.Main).launch {
            view?.let {
                it.updateProgressBarVisibilityStatus(View.INVISIBLE)
                it.displayListOfApps(list)
            }
        }
    }
}