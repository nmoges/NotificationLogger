package com.notificationlog.ui.adapters

import android.content.Context
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.notificationlog.R
import com.notificationlog.ui.fragments.FragmentListApps
import com.openclassrooms.data.model.AppInfo

/**
 * Adapter class to display list of application on [FragmentListApps] recycler view.
 */
class ListAppsAdapter(private val context: Context, private val onItemClicked: (Int) -> Unit) :
    RecyclerView.Adapter<ListAppsAdapter.ListAppsViewHolder>() {

    var listApps: MutableList<AppInfo> = mutableListOf()

    inner class ListAppsViewHolder(view: View, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        var nameApp: MaterialTextView = view.findViewById(R.id.name_application)
        var categoryApp: MaterialTextView = view.findViewById(R.id.category_application)
        var switch: SwitchCompat = view.findViewById(R.id.switch_selector)
        var iconApp: AppCompatImageView = view.findViewById(R.id.icon_notification)
        var constraintLayout: ConstraintLayout = view.findViewById(R.id.constraint_layout_item)
        init {
            constraintLayout.setOnClickListener { onItemClicked(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAppsViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.layout_list_apps_item,
                parent,
                false)
        return ListAppsViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ListAppsViewHolder, position: Int) {
        holder.apply {
            nameApp.text = listApps[position].name
            categoryApp.text = getCategoryName(listApps[position].category)
            iconApp.setImageDrawable(listApps[position].icon)
            switch.isChecked = listApps[position].filterStatus == 1
        }
    }

    override fun getItemCount(): Int = listApps.size

    /**
     * Provides the application "Category" to display.
     * @param categoryNumber : integer value associated an application "Category"
     */
    private fun getCategoryName(categoryNumber: Int): String = when(categoryNumber) {
        ApplicationInfo.CATEGORY_GAME -> { context.resources.getString(R.string.category_game) }
        ApplicationInfo.CATEGORY_AUDIO -> { context.resources.getString(R.string.category_audio) }
        ApplicationInfo.CATEGORY_VIDEO -> { context.resources.getString(R.string.category_video) }
        ApplicationInfo.CATEGORY_IMAGE -> { context.resources.getString(R.string.category_image) }
        ApplicationInfo.CATEGORY_SOCIAL -> { context.resources.getString(R.string.category_social) }
        ApplicationInfo.CATEGORY_NEWS -> { context.resources.getString(R.string.category_news) }
        ApplicationInfo.CATEGORY_MAPS -> { context.resources.getString(R.string.category_maps) }
        ApplicationInfo.CATEGORY_PRODUCTIVITY -> { context.resources.getString(R.string.category_productivity) }
        ApplicationInfo.CATEGORY_ACCESSIBILITY -> { context.resources.getString(R.string.category_accessibility) }
        else -> { context.resources.getString(R.string.category_undefined) }
    }
}