package com.notificationlog.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.notificationlog.AppInfo
import com.notificationlog.R
import com.notificationlog.contracts.ContractMainActivity
import com.notificationlog.databinding.ActivityMainBinding
import com.notificationlog.permission.PermissionAccess
import com.notificationlog.presenters.PresenterMainActivity
import com.notificationlog.ui.dialogs.DialogPermissionAccess
import com.notificationlog.ui.fragments.FragmentListApps
import com.notificationlog.ui.fragments.FragmentSearch
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ContractMainActivity.View {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    @Inject lateinit var presenter: PresenterMainActivity
    private var fabStatus: Boolean = false
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeSharedPreferences()
        initializeFloatingButtonStatus()
        initializeToolbar()
        initializeNavigationProperties()
        presenter.view = this
        handleFabClickListener()
    }

    override fun onResume() {
        super.onResume()
        presenter.updateListAppsInstalled(packageManager)
        displayDialogIfServiceIsDisabled()
    }

    private fun initializeToolbar() = setSupportActionBar(binding.toolbar)

    @SuppressLint("CommitPrefEdits")
    private fun initializeSharedPreferences() {
        sharedPreferences = getSharedPreferences(AppInfo.SHARED_PREF_FILE, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    private fun initializeFloatingButtonStatus() {
        fabStatus = sharedPreferences.getBoolean(AppInfo.FAB_KEY, false)
        updateFloatingActionButtonIcon()
    }

    fun updateToolbarTitle(@StringRes title: Int, backIconDisplay: Boolean) {
        binding.toolbar.setTitle(title)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(backIconDisplay)
            if (backIconDisplay) {
                setHomeAsUpIndicator(ResourcesCompat
                    .getDrawable(resources, R.drawable.ic_baseline_arrow_back_24dp_white, null))
            }
        }
    }

    private fun updateSearchBarDisplay(visibility: Int) {
        binding.searchBar.visibility = visibility
    }

    private fun handleFabClickListener() {
        binding.fab.setOnClickListener {
            presenter.clickOnFloatingActionButton()
        }
    }

    private fun initializeNavigationProperties() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                          as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun displayDialogIfServiceIsDisabled() {
        if (!PermissionAccess.checkIfNotificationListenerAccessIsEnabled(applicationContext))
            displayDialogPermissionAccess()
    }

    private fun displayDialogPermissionAccess() {
        val dialog = DialogPermissionAccess()
        dialog.show(supportFragmentManager, DialogPermissionAccess.TAG)
    }

    override fun displayToastListenerStatus() {
        if (fabStatus)
            Toast.makeText(this, R.string.listener_activated, Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, R.string.listener_deactivated, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        updateMainActivityViews()
    }

    /**
     * Restores MainActivity toolbar and floating action button default values if current
     * destination in navigation graph is FragmentListNotif.
     */
    private fun updateMainActivityViews() {
        if (navController.currentDestination?.label.toString()
            == resources.getString(R.string.fragment_list_notif_dest_label)) {
            updateToolbarTitle(R.string.app_name, false)
            updateSearchBarDisplay(View.INVISIBLE)
            updateFloatingActionButtonVisibility(View.VISIBLE)
        }
    }

    override fun updateFloatingActionButtonStatus() {
        fabStatus = !fabStatus
    }

    override fun updateFloatingActionButtonIcon() {
        if (fabStatus) {
            binding.fab.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                                            R.drawable.ic_baseline_notifications_active_24dp_white,
                                      null))
        }
        else {
            binding.fab.setImageDrawable(
                ResourcesCompat.getDrawable(resources,
                    R.drawable.ic_baseline_notifications_off_24dp_white,
                    null))
        }
    }

    override fun updateSharedPreferencesFile() {
        editor.putBoolean(AppInfo.FAB_KEY, fabStatus).apply()
    }

    fun updateFloatingActionButtonVisibility(visibility: Int) {
        binding.fab.visibility = visibility
    }

    /**
     * Navigates in the navigation graph to the fragment associated with the tag value.
     * @param tag : tag fragment
     */
    fun displayFragment(tag: String) {
       when(tag) {
           FragmentListApps.TAG -> {
               navController.navigate(R.id.action_fragment_list_notif_to_fragment_list_apps)
           }
           FragmentSearch.TAG -> {
               navController.navigate(R.id.action_fragment_list_notif_to_fragment_search)
               updateSearchBarDisplay(View.VISIBLE)
           }
           else -> {
               //TODO() : To update with new fragments
           }
       }
   }
}