package gaurav.cuvettecalltracker.presentation.home

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import gaurav.cuvettecalltracker.presentation.composables.CallLogCard
import gaurav.cuvettecalltracker.presentation.composables.Label
import gaurav.cuvettecalltracker.presentation.composables.SimplePopup
import gaurav.cuvettecalltracker.presentation.home.composables.AnalyticsRow
import gaurav.cuvettecalltracker.presentation.util.CallType
import gaurav.cuvettecalltracker.presentation.util.findActivity

var permissionsGrantMap = mutableMapOf(
    Manifest.permission.READ_PHONE_STATE to false,
    Manifest.permission.READ_CALL_LOG to false
)

val simplePermissionTitle = mapOf(
    Manifest.permission.READ_CALL_LOG to "READ CALL LOG",
    Manifest.permission.READ_PHONE_STATE to "READ PHONE STATE"
)

fun getSimplePermissionTitle(list: List<String>) =
    list.map { simplePermissionTitle[it] }.joinToString(", ")


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCallLogClick: (String) -> Unit
) {

    val context = LocalContext.current
    val activity = context.findActivity()
    val shouldShowPermissionRationalePermissions = remember { mutableStateListOf<String>() }
    val permanentlyDeniedPermissions = remember { mutableStateListOf<String>() }
    var showPermissionRationalePopup by remember { mutableStateOf(false) }
    var showGoToSettingsPopup by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.forEach { (permission, granted) ->
            permissionsGrantMap[permission] = granted
            if (!granted) {
                activity?.let {
                    if (shouldShowRequestPermissionRationale(it, permission)) {
                        shouldShowPermissionRationalePermissions.add(permission)
                        permanentlyDeniedPermissions.remove(permission)
                    }
                    else {
                        permanentlyDeniedPermissions.add(permission)
                        shouldShowPermissionRationalePermissions.remove(permission)
                    }
                }
            }
        }
        showPermissionRationalePopup = shouldShowPermissionRationalePermissions.toList().isNotEmpty()
        showGoToSettingsPopup = permanentlyDeniedPermissions.toList().isNotEmpty()
    }

    val recentLogs = viewModel.state.value.recentLogs
    val totalCallLogs = recentLogs.size
    val totalIncomingCallLogs = recentLogs.filter { it.callType == CallType.INCOMING }.size
    val totalOutgoingCallLogs = recentLogs.filter { it.callType == CallType.OUTGOING }.size
    val totalMissedCallLogs = recentLogs.filter { it.callType == CallType.MISSED }.size
    val totalDuration = recentLogs.sumOf { it.duration }

    val lifeCycleOwner = LocalLifecycleOwner.current

    fun checkPermissions() {
        val updatedMap = mutableMapOf<String, Boolean>()
        permissionsGrantMap.forEach { (permission, _) ->
            val granted = ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            updatedMap[permission] = granted
            if (granted) {
                shouldShowPermissionRationalePermissions.remove(permission)
                permanentlyDeniedPermissions.remove(permission)
            }
        }
        showPermissionRationalePopup = shouldShowPermissionRationalePermissions.toList().isNotEmpty()
        showGoToSettingsPopup = permanentlyDeniedPermissions.toList().isNotEmpty()
        permissionsGrantMap = updatedMap
    }

    LaunchedEffect(Unit) {
        checkPermissions()
    }

    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                if (permissionsGrantMap.any { (_, granted) -> !granted })
                    permissionLauncher.launch(permissionsGrantMap.keys.toTypedArray())
            } else if (event == Lifecycle.Event.ON_RESUME) checkPermissions()
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }

    fun openSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.applicationContext.packageName, null)
        ).also {
            context.startActivity(it)
        }
    }

    if (showPermissionRationalePopup) {
        SimplePopup(
            label = "Message",
            message = "The app need " +
                    getSimplePermissionTitle(shouldShowPermissionRationalePermissions.toList()) +
                    " permissions to function normally",
            onAction = { permissionLauncher.launch(shouldShowPermissionRationalePermissions.toTypedArray()) },
            onDismissRequest = { showGoToSettingsPopup = false }
        )
    }
    if (showGoToSettingsPopup) {
        SimplePopup(
            label = "Message",
            message = "You have denied " +
                    getSimplePermissionTitle(permanentlyDeniedPermissions.toList()) +
                    " permission. Please allow permissions for app to function normally",
            onAction = { openSettings() },
            onDismissRequest = { showGoToSettingsPopup = false }
        )
    }

    LazyColumn(
        Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        contentPadding = PaddingValues(
            vertical = 15.dp,
            horizontal = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        item {
            AnalyticsRow(
                modifier = Modifier.padding(horizontal = 5.dp),
                totalCalls = totalCallLogs,
                totalIncomingCalls = totalIncomingCallLogs,
                totalOutgoingCalls = totalOutgoingCallLogs,
                totalMissedCalls = totalMissedCallLogs,
                totalDuration = totalDuration
            )
        }
        item {
            Label(
                text = "Recent logs",
                modifier = Modifier.padding(
                    top = 20.dp,
                    bottom = 10.dp,
                    start = 5.dp
                )
            )
        }
        items(recentLogs) {
            CallLogCard(
                it,
                onClick = onCallLogClick
            )
        }
    }
}