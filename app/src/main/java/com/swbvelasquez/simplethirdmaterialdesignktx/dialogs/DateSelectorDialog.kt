package com.swbvelasquez.simplethirdmaterialdesignktx.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class DateSelectorDialog : DialogFragment() {
    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance(Locale.ROOT)
        val args = this.arguments

        if (args != null) {
            val date = args.getLong(DATE_PARAM)
            calendar.timeInMillis = date
        }

        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        return DatePickerDialog(requireActivity(), listener, year, month, day)
    }

    companion object {
        const val DATE_PARAM = "dateParam"
        const val SELECTED_DATE = "selectedDate"
    }
}