package com.islam.otgtask.project_base.utils

import android.text.format.DateFormat
import android.text.format.DateUtils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateManager {

    fun getTimeDiff(startTime: Long): String {
        return DateUtils.getRelativeTimeSpanString(startTime) as String
    }

    fun convertTimestampToString(timestamp: Long, format: String): String {
        return DateFormat.format(format, timestamp).toString()
    }

    fun convertStringToDateObj(dateString: String, format: String): Date? {
        try {
            val sd1 = SimpleDateFormat(format, Locale.getDefault())
            return sd1.parse(dateString)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(ParseException::class)
    fun isTimeInBetween(openTime: Long, closeTime: Long): Boolean {
        val initialTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(openTime * 1000))
        val finalTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(closeTime * 1000))
        var valid = false
        val reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$"
        if (initialTime.matches(reg.toRegex()) && finalTime.matches(reg.toRegex())) {

            val inTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(initialTime)
            val calendar1 = Calendar.getInstance()
            calendar1.time = inTime

            val calendar3 = Calendar.getInstance()
            calendar3.time = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                    .parse(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()))

            //End Time
            val finTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(finalTime)
            val calendar2 = Calendar.getInstance()
            calendar2.time = finTime

            if (finalTime.compareTo(initialTime) < 0) {
                calendar2.add(Calendar.DATE, 1)
            }

            val actualTime = calendar3.time
            if ((actualTime.after(calendar1.time) || actualTime.compareTo(calendar1.time) == 0) && actualTime.before(calendar2.time)) {
                valid = true

            }
        }
        return valid
    }

}
