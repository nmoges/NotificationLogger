package com.notificationlog.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.notificationlog.R
import com.notificationlog.contracts.ContractListApps
import com.notificationlog.databinding.FragmentListAppsBinding
import com.notificationlog.presenters.PresenterFragmentListApps
import com.notificationlog.ui.activities.MainActivity
import com.notificationlog.ui.adapters.ListAppsAdapter
import com.openclassrooms.data.model.AppInfo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Defines a Fragment displaying the list of all installed applications.
 */
@AndroidEntryPoint
class FragmentListApps : Fragment(), ContractListApps.View {

    private lateinit var binding: FragmentListAppsBinding
    @Inject lateinit var presenter: PresenterFragmentListApps

    companion object {
        const val TAG: String = "FRAGMENT_LIST_APPS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentListAppsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragmentToolbar()
        initializeRecyclerView()
        presenter.view = this
        presenter.getListOfAppsFromDB((activity as MainActivity).packageManager)
    }

    private fun initializeFragmentToolbar() {
        (activity as MainActivity).updateToolbarTitle(R.string.toolbar_title_list_apps, true)
    }

    private fun initializeRecyclerView() {
        binding.recyclerViewListApps.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = ListAppsAdapter(context) {
                Log.i("POSITION_ITEM", "$it")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            (activity as MainActivity).onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
    /**
     * Displays list of installed apps information in RecyclerView
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun displayListOfApps(list: List<AppInfo>) {
        (binding.recyclerViewListApps.adapter as ListAppsAdapter).apply {
            listApps.addAll(list)
            notifyDataSetChanged()
        }
    }

    override fun updateProgressBarVisibilityStatus(visibility: Int) {
        binding.circularProgressBarApps.visibility = visibility
    }
}