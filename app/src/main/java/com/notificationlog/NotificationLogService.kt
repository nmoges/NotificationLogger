package com.notificationlog

import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.notificationlog.utils.DateFormatConverter
import com.openclassrooms.data.model.NotificationInfo
import com.openclassrooms.data.repository.RepositoryAccess
import com.openclassrooms.data.utils.NotificationInfoComparator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationLogService : NotificationListenerService() {

    @Inject
    lateinit var repositoryAccess: RepositoryAccess

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("SERVICE_STATE", "onStartCommand")
        return START_STICKY
    }

    override fun onDestroy() {
        Log.i("SERVICE_STATE", "onDestroy")
        super.onDestroy()
    }

    override fun onCreate() {
        Log.i("SERVICE_STATE", "onCreate")
        super.onCreate()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.i("SERVICE_STATE", "onListenerConnected")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.i("SERVICE_STATE", "onListenerDisconnected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.let { notifyBroadcastReceiver(it) }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    // TODO: To optimize
    private fun notifyBroadcastReceiver(sbn: StatusBarNotification) {
        val title = sbn.notification.extras.getCharSequence("android.title").toString()
        val message = sbn.notification.extras.getCharSequence("android.text").toString()
        val time = DateFormatConverter.convertTimeInReadableDate(sbn.postTime)

     //   Log.i("ACTION_INTENT", "creatorPackage : ${sbn.notification.contentIntent.creatorPackage}")

        val notificationInfo = NotificationInfo(
            title = title,
            message = message,
            packageName = sbn.packageName,
            date = time
        )
        if (checkIfNotificationStorageIsEnabled()) {
            CoroutineScope(Dispatchers.IO).launch {
                val list = repositoryAccess.test()
                if (list.isNotEmpty()) {
                    if (!checkIfNotificationAlreadyExistInDB(list, notificationInfo)
                        && !checkIfNotificationContainsWrongStringValues(notificationInfo))
                        repositoryAccess.sendNewNotificationInfoToDB(notificationInfo)
                }
                else {
                    repositoryAccess.sendNewNotificationInfoToDB(notificationInfo)
                }
            }
        }
    }

    /**
     * Checks if current notification contains information which is already stored in DB.
     * @param list : list of notifications from DB
     * @param newNotif : new notification to store in DB
     */
    private fun checkIfNotificationAlreadyExistInDB(list: List<NotificationInfo>,
                                                    newNotif: NotificationInfo): Boolean {
        var found = false
        list.forEach {
            if (NotificationInfoComparator.compare(newNotif, it) == 0) {
                found = true
            }
        }
        return found
    }

    /**
     * Checks if notification contains useful information before storing it.
     * @param newNotif : notification to store
     */
    private fun checkIfNotificationContainsWrongStringValues(newNotif: NotificationInfo): Boolean {
        return newNotif.title == "null" || newNotif.message == "null"
               || newNotif.title == "" || newNotif.message == ""
    }

    /**
     * Checks if user has deactivated or not the storage of notifications
     * (value in SharedPreferences file).
     */
    private fun checkIfNotificationStorageIsEnabled(): Boolean {
        return getSharedPreferences(AppInfo.SHARED_PREF_FILE, Context.MODE_PRIVATE)
               .getBoolean(AppInfo.FAB_KEY, false)
    }
}