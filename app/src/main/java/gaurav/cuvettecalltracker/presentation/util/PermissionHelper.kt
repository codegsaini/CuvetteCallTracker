package gaurav.cuvettecalltracker.presentation.util

import android.Manifest

class PermissionHelper {
    companion object {
        var permissionsGrantMap = mutableMapOf(
            Manifest.permission.READ_PHONE_STATE to false,
            Manifest.permission.READ_CALL_LOG to false,
            Manifest.permission.RECORD_AUDIO to false,
            Manifest.permission.READ_EXTERNAL_STORAGE to false,
            Manifest.permission.WRITE_EXTERNAL_STORAGE to false
        )

        private val simplePermissionTitle = mapOf(
            Manifest.permission.READ_CALL_LOG to "READ CALL LOG",
            Manifest.permission.READ_PHONE_STATE to "READ PHONE STATE",
            Manifest.permission.RECORD_AUDIO to "READ AUDIO",
            Manifest.permission.READ_EXTERNAL_STORAGE to "READ EXTERNAL STORAGE",
            Manifest.permission.WRITE_EXTERNAL_STORAGE to "WRITE EXTERNAL STORAGE"
        )

        fun getSimplePermissionTitle(list: List<String>) =
            list.map { simplePermissionTitle[it] }.joinToString(", ")

    }
}