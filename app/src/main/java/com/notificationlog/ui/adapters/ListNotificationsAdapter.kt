package com.notificationlog.ui.adapters

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.notificationlog.R
import com.notificationlog.ui.fragments.FragmentListNotif
import com.openclassrooms.data.model.NotificationInfo

/**
 * Adapter class to display list of notifications on [FragmentListNotif] recycler view.
 */
class ListNotificationsAdapter(private val packageManager: PackageManager,
                               private val onIconItemClicked: (Int) -> Unit,
                               private val onItemClicked: (Int) -> Unit):
    RecyclerView.Adapter<ListNotificationsAdapter.ListNotifViewHolder>() {

    var listNotifications: MutableList<NotificationInfo> = mutableListOf()

    inner class ListNotifViewHolder(view: View, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        var title: MaterialTextView = view.findViewById(R.id.title_notification)
        var message: MaterialTextView = view.findViewById(R.id.message_notification)
        var date: MaterialTextView = view.findViewById(R.id.date_notification)
        var layoutNotification: ConstraintLayout = view.findViewById(R.id.layout_notification)
        var icon: AppCompatImageView = view.findViewById(R.id.icon_notification)

        init {
            layoutNotification.setOnClickListener { onItemClicked(adapterPosition) }
            icon.setOnClickListener { onIconItemClicked(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNotifViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.layout_list_notification_item,
                parent,
                false)
        return ListNotifViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ListNotifViewHolder, position: Int) {
        holder.apply {
            title.text = listNotifications[position].title
            message.text = listNotifications[position].message
            date.text = listNotifications[position].date
            val drawable = packageManager.getApplicationIcon(listNotifications[position].packageName)
            icon.setImageDrawable(drawable)
        }
    }

    override fun getItemCount(): Int = listNotifications.size
}