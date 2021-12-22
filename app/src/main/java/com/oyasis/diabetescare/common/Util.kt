package com.oyasis.diabetescare.common

import android.icu.util.Calendar
import java.sql.Time
import java.sql.Timestamp
import java.time.Month
import java.util.*

object Util {

    fun Long.toDate(): String {
        val timeStamp = Timestamp(this)
        val date = Date(timeStamp.time)
        val calendar = Calendar.getInstance()
        calendar.time = date

        return "${calendar.get(Calendar.DAY_OF_MONTH).toTime()}-" +
                "${calendar.get(Calendar.MONTH).toTime()}-" +
                "${calendar.get(Calendar.YEAR)}"
    }

    fun Long.toDateTime(): String {
        val timeStamp = Timestamp(this)
        val date = Date(timeStamp.time)
        val calendar = Calendar.getInstance()
        calendar.time = date

        return "${calendar.get(Calendar.DAY_OF_MONTH).toTime()}-" +
                "${calendar.get(Calendar.MONTH).toTime()}-" +
                "${calendar.get(Calendar.YEAR)} at " +
                "${calendar.get(Calendar.HOUR_OF_DAY).toTime()}:" +
                calendar.get(Calendar.MINUTE).toTime()
    }

    fun Int.toTime(): String {
        return if(this<10) "0$this" else "$this"
    }

    class TimeValidator() {

        private var hour: Int = 0
        private var minutes: Int = 0
        private var day: Int = 0
        private var month: Int = 0
        private var year: Int = 0
        val calendar: Calendar = Calendar.getInstance()

        fun setTime(hour: Int, minutes: Int) {
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minutes)
        }

        fun setDate(year: Int, month: Int, day: Int) {
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
        }

        fun validateDateTime(): Boolean {
            val currentTime = Date()
            return calendar.time.after(currentTime)
        }
    }
}
