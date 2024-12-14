package gaurav.cuvettecalltracker.presentation.home

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
import gaurav.cuvettecalltracker.presentation.util.PermissionHelper.Companion.getSimplePermissionTitle
import gaurav.cuvettecalltracker.presentation.util.PermissionHelper.Companion.permissionsGrantMap
import gaurav.cuvettecalltracker.presentation.util.findActivity
import gaurav.cuvettecalltracker.service.AdminReceiver

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCallLogClick: (String) -> Unit
) {

    val context = LocalContext.current
    val activity = context.findActivity()
    val lifeCycleOwner = LocalLifecycleOwner.current

    val devicePolicyManager = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val adminReceiver = ComponentName(context, AdminReceiver::class.java)
    var isDeviceAdmin by remember { mutableStateOf(false) }

    val deviceAdminRequestLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        isDeviceAdmin = devicePolicyManager.isAdminActive(adminReceiver)
    }

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

    fun openSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts(
                "package",
                context.applicationContext.packageName,
                null
            )
        ).also { context.startActivity(it) }
    }

    fun openDeviceAdminActivationPage() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver)
            putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Device admin privilege is required to record phone call"
            )
        }
        deviceAdminRequestLauncher.launch(intent)
    }

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

    fun checkIfAppIsActiveAdmin() {
        isDeviceAdmin = devicePolicyManager.isAdminActive(adminReceiver)
        if (isDeviceAdmin) return
        openDeviceAdminActivationPage()
    }

    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                // If any permission still remain denied
                if (permissionsGrantMap.any { (_, granted) -> !granted })
                    permissionLauncher.launch(permissionsGrantMap.keys.toTypedArray())

            } else if (event == Lifecycle.Event.ON_RESUME) {
                checkPermissions()
                // Check for device admin only after permissions flow settled
                if (permissionsGrantMap.all{ it.value }) checkIfAppIsActiveAdmin()
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose { lifeCycleOwner.lifecycle.removeObserver(observer) }
    }

    if (!isDeviceAdmin) {
        SimplePopup(
            label = "Message",
            message = "The app need to be Admin app.",
            onAction = { openDeviceAdminActivationPage() },
            onDismissRequest = {}
        )
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
                callLog = it,
                onClick = onCallLogClick
            )
        }
    }
}