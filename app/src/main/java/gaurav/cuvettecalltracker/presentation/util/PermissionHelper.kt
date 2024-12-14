package gaurav.cuvettecalltracker.presentation.util

import android.Manifest

class PermissionHelper {
    companion object {
        var permissionsGrantMap = mutableMapOf(
            Manifest.permission.READ_PHONE_STATE to false,
            Manifest.permission.READ_CALL_LOG to false
        )

        private val simplePermissionTitle = mapOf(
            Manifest.permission.READ_CALL_LOG to "READ CALL LOG",
            Manifest.permission.READ_PHONE_STATE to "READ PHONE STATE"
        )

        fun getSimplePermissionTitle(list: List<String>) =
            list.map { simplePermissionTitle[it] }.joinToString(", ")

    }
}