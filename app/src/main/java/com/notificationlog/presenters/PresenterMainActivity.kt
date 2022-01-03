package com.notificationlog.presenters

import android.content.pm.PackageManager
import com.notificationlog.contracts.ContractMainActivity
import com.notificationlog.ui.activities.MainActivity
import com.openclassrooms.data.repository.RepositoryAccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Presenter for [MainActivity].
 */
class PresenterMainActivity
@Inject constructor(private val repositoryAccess: RepositoryAccess): ContractMainActivity.Presenter {

    var view: ContractMainActivity.View? = null

    override fun updateListAppsInstalled(packageManager: PackageManager) {
        CoroutineScope(Dispatchers.IO).launch {
            repositoryAccess.updateListAppsInstalled(packageManager)
        }
    }

    override fun clickOnFloatingActionButton() {
        view?.let {
            it.updateFloatingActionButtonStatus()
            it.updateFloatingActionButtonIcon()
            it.updateSharedPreferencesFile()
            it.displayToastListenerStatus()
        }
    }
}