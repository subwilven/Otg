package com.islam.otgtask.project_base.utils

import android.app.Dialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.listItems
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.materialdialogs.list.listItemsSingleChoice

import com.islam.basepropject.project_base.base.dialogs.BaseDefaultDialog
import com.islam.otgtask.R
import com.islam.otgtask.project_base.POJO.Message

interface DialogManager {

    val _fragmentManager: FragmentManager?
    val _context: Context

    private fun buildDialog(@StringRes title: Int,
                            @StringRes positiveButton: Int = -1,
                            @StringRes negativeButton: Int = -1,
                            cancelable: Boolean = true,
                            cancelOnTouchOutside: Boolean = true,
                            onPositiveClick: (() -> Unit)? = null,
                            onNegativeClick: (() -> Unit)? = null): MaterialDialog {
        val dialog = MaterialDialog(_context)
                .title(title)

                .cancelOnTouchOutside(cancelOnTouchOutside)
                .cancelable(cancelable)
        if (positiveButton != -1)
            dialog.positiveButton(positiveButton) { onPositiveClick?.invoke() }

        if (negativeButton != -1)
            dialog.negativeButton(negativeButton) { onNegativeClick?.invoke() }

        return dialog
    }

    private fun show(dialog: Dialog, fragmentManager: FragmentManager) {
        BaseDefaultDialog(dialog).show(fragmentManager)
    }


    fun showDialog(@StringRes title: Int,
                   message: Message,
                   @StringRes positiveButton: Int = R.string.ok,
                   @StringRes negativeButton: Int = -1,
                   cancelable: Boolean = true,
                   cancelOnTouchOutside: Boolean = true,
                   onNegativeClick: (() -> Unit)? = null,
                   onPositiveClick: (() -> Unit)? = null) {
        val dialog = buildDialog(title, positiveButton, negativeButton, cancelable, cancelOnTouchOutside, onPositiveClick, onNegativeClick)
        dialog.message(text = message.getValue(_context))
        show(dialog, _fragmentManager!!)
    }

    fun showDialogList(@StringRes title: Int,
                       items: List<String>,
                       cancelable: Boolean = true,
                       cancelOnTouchOutside: Boolean = true,
                       onItemSelectedListener: ((dialog: MaterialDialog, index: Int, text: String) -> Unit)? = null) {
        var dialog = buildDialog(title, cancelable = cancelable, cancelOnTouchOutside = cancelOnTouchOutside)
        dialog = dialog.listItems(
                waitForPositiveButton = false,
                items = items,
                selection = onItemSelectedListener
        )
        show(dialog, _fragmentManager!!)
    }

    fun showDialogListSingleChoice(@StringRes title: Int,
                                   items: List<String>,
                                   @StringRes positiveButton: Int = R.string.ok,
                                   @StringRes negativeButton: Int = -1,
                                   cancelable: Boolean = true,
                                   cancelOnTouchOutside: Boolean = true,
                                   initialSelection: Int = -1,
                                   onNegativeClick: (() -> Unit)? = null,
                                   onPositiveClick: ((dialog: MaterialDialog, index: Int, text: String) -> Unit)? = null) {
        var dialog = buildDialog(title, positiveButton, negativeButton, cancelable, cancelOnTouchOutside, onNegativeClick = onNegativeClick)

        dialog = dialog.listItemsSingleChoice(
                items = items,
                initialSelection = initialSelection,
                selection = onPositiveClick)

        show(dialog, _fragmentManager!!)
    }

    fun showDialogListMultiChoice(@StringRes title: Int,
                                  items: List<String>,
                                  @StringRes positiveButton: Int = R.string.ok,
                                  @StringRes negativeButton: Int = -1,
                                  cancelable: Boolean = true,
                                  cancelOnTouchOutside: Boolean = true,
                                  initialSelection: IntArray = IntArray(0),
                                  onNegativeClick: (() -> Unit)? = null,
                                  onPositiveClick: ((dialog: MaterialDialog, indices: IntArray, items: List<String>) -> Unit)? = null) {
        var dialog = buildDialog(title, positiveButton, negativeButton, cancelable, cancelOnTouchOutside, onNegativeClick = onNegativeClick)

        dialog = dialog.listItemsMultiChoice(
                items = items,
                allowEmptySelection = false,
                initialSelection = initialSelection,
                selection = onPositiveClick)

        show(dialog, _fragmentManager!!)
    }

    fun showDialogListCustomAdapter(@StringRes title: Int,
                                    adapter: RecyclerView.Adapter<*>,
                                    @StringRes positiveButton: Int = R.string.ok,
                                    @StringRes negativeButton: Int = -1,
                                    cancelable: Boolean = true,
                                    cancelOnTouchOutside: Boolean = true,
                                    onNegativeClick: (() -> Unit)? = null,
                                    onPositiveClick: ((dialog: MaterialDialog, indices: IntArray, items: List<String>) -> Unit)? = null) {
        var dialog = buildDialog(title, positiveButton, negativeButton, cancelable, cancelOnTouchOutside, onNegativeClick = onNegativeClick)

        dialog = dialog.customListAdapter(adapter)

        show(dialog, _fragmentManager!!)
    }

}