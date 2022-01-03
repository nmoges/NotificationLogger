package com.notificationlog.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.notificationlog.R
import com.notificationlog.contracts.ContractListNotif
import com.notificationlog.databinding.FragmentListNotifBinding
import com.notificationlog.presenters.PresenterFragmentListNotif
import com.notificationlog.ui.activities.MainActivity
import com.notificationlog.ui.adapters.ListNotificationsAdapter
import com.notificationlog.ui.dialogs.DialogAppDetails
import com.openclassrooms.data.model.NotificationInfo
import dagger.hilt.android.AndroidEntryPoint
import java.util.Collections.reverse
import javax.inject.Inject

/**
 * Defines a Fragment displaying the list of notifications.
 */
@AndroidEntryPoint
class FragmentListNotif : Fragment(), ContractListNotif.View {

    private lateinit var binding: FragmentListNotifBinding
    @Inject lateinit var presenter: PresenterFragmentListNotif

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentListNotifBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        initializePresenter()
        addObserverToDatabase()
    }

    private fun initializePresenter() {
        presenter.context = context
        presenter.view = this
    }

    private fun addObserverToDatabase() {
        presenter.getListOfNotificationsDataFromDB().observe(viewLifecycleOwner, {
            presenter.getListOfNotificationsToDisplay(it)
        })
    }

    private fun initializeRecyclerView() {
        binding.recyclerViewListNotifs.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ListNotificationsAdapter((activity as MainActivity).packageManager,
                                              { position -> onIconItemClicked(position) },
                                              { position -> onItemClicked(position)})
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_fragment_list_notif, menu)
        displayItemsIconInMenu(menu)
    }

    @SuppressLint("RestrictedApi")
    private fun displayItemsIconInMenu(menu: Menu) {
        if (menu is MenuBuilder) {
            val menuBuilder: MenuBuilder = menu
            menuBuilder.setOptionalIconsVisible(true)
        }
    }

    // TODO() : Toast to remove after functionalities implementations
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.search -> { presenter.clickOnSearchMenuItem() }
            R.id.filter -> {
                Toast.makeText((activity as MainActivity), "Not available yet", Toast.LENGTH_SHORT).show()
            }
            R.id.settings -> {
                Toast.makeText((activity as MainActivity), "Not available yet", Toast.LENGTH_SHORT).show()
            }
            R.id.apps -> { presenter.clickOnAppsMenuItem() }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displayNewFragment(tag: String) {
        (activity as MainActivity).let {
            it.updateFloatingActionButtonVisibility(View.INVISIBLE)
            it.displayFragment(tag)
        }
    }

    /**
     * Refresh RecyclerView display everytime a new notification has been caught and added to
     * the "notifications" table database.
     * @param list : New list of notifications
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun updateRecyclerViewWithNewList(list: List<NotificationInfo>) {
        (binding.recyclerViewListNotifs.adapter as ListNotificationsAdapter).apply {
            // Update "Empty" background text
            if (list.isNotEmpty()) updateBackgroundMaterialTextViewDisplay(View.INVISIBLE)
            // Update list to display
            this.listNotifications.clear()
           // reverse(list)
            this.listNotifications.addAll(list)
            notifyDataSetChanged()
        }
    }

    override fun displayAppDetailsDialog(view: View) {
        val dialog = DialogAppDetails(view)
        dialog.show((activity as MainActivity).supportFragmentManager, DialogAppDetails.TAG)
    }

    override fun updateProgressBarVisibilityStatus(visibility: Int) {
        binding.circularProgressBarNotif.visibility = visibility
    }

    private fun updateBackgroundMaterialTextViewDisplay(visibility: Int) {
        binding.backgroundText.visibility = visibility
    }

    private fun onItemClicked(position: Int) {
        presenter.clickOnRecyclerViewItem(position)
    }

    private fun onIconItemClicked(position: Int) {
        context?.let {
            presenter.clickOnRecyclerViewIconItem(position, (activity as MainActivity).packageManager, it)
        }
    }
}