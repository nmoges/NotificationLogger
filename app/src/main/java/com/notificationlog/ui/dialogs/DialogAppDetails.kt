package com.notificationlog.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment

/**
 * Defines a dialog displaying an application details.
 */
class DialogAppDetails(private val viewDialog: View): DialogFragment() {

    companion object {
        const val TAG = "DIALOG_APP_DETAILS"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
                .setView(viewDialog)
           builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}