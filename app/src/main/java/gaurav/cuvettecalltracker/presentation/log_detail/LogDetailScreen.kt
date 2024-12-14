package gaurav.cuvettecalltracker.presentation.log_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gaurav.cuvettecalltracker.domain.model.CallLog
import gaurav.cuvettecalltracker.presentation.composables.Label
import gaurav.cuvettecalltracker.presentation.log_detail.composables.LogDetailCard
import gaurav.cuvettecalltracker.presentation.log_detail.composables.ProfileCard
import gaurav.cuvettecalltracker.presentation.log_detail.composables.TitleRow
import gaurav.cuvettecalltracker.presentation.util.CallType

@Composable
fun LogDetailScreen(
    onBackClick: () -> Unit
) {
    Column(
        Modifier.statusBarsPadding()
    ) {
        TitleRow(onBackClick = onBackClick)
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 15.dp,
                horizontal = 10.dp
            ),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            item {
                ProfileCard()
            }
            item {
                Label(
                    text = "Call history",
                    modifier = Modifier.padding(
                        top = 30.dp,
                        bottom = 10.dp,
                        start = 5.dp
                    )
                )
            }
            items(10) {
                LogDetailCard (
                    CallLog(
                        number = "",
                        timestamp = 1734024741,
                        callType = CallType.OUTGOING,
                        duration = 3534,
                    ),
                    onListenClick = {}
                )
            }
        }
    }
}