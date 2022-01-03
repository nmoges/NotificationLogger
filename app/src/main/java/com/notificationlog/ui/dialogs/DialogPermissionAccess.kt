package com.notificationlog.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.notificationlog.R
import com.notificationlog.permission.PermissionAccess

/**
 * Defines a dialog displaying a permission access message.
 */
class DialogPermissionAccess: DialogFragment() {

    companion object {
        const val TAG = "DIALOG_PERMISSION_ACCESS"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
                .setTitle(R.string.title_dialog_permission)
                .setMessage(R.string.message_dialog_permission)
                .setPositiveButton(R.string.btn_yes_dialog_permission) { _, _ ->
                    PermissionAccess.accessSettingsInDevice(it)
                }
                .setNegativeButton(R.string.btn_no_dialog_permission) { _, _ ->
                    it.finishAffinity()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}