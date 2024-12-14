package gaurav.cuvettecalltracker.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimestampHelper {
    companion object {
        fun getSimpleTimeFormat(millis: Long) : String {
            val dateFormat = SimpleDateFormat("d MMM yyyy h:mm a", Locale.ENGLISH)
            val date = Date(millis)
            return dateFormat.format(date)
        }
        fun getSimpleDurationFormat(seconds: Int) : String {
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val remainingSeconds = seconds % 60

            val duration = mutableListOf<String>()

            if (hours > 0) duration.add("$hours hr")
            if (minutes > 0) duration.add("$minutes min")
            duration.add("$remainingSeconds sec")

            return duration.joinToString(" ")
        }
    }
}