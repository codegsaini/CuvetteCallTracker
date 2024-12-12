package gaurav.cuvettecalltracker.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gaurav.cuvettecalltracker.R
import gaurav.cuvettecalltracker.domain.model.CallLog
import gaurav.cuvettecalltracker.presentation.composables.CallLogCard
import gaurav.cuvettecalltracker.presentation.composables.Label
import gaurav.cuvettecalltracker.presentation.home.composables.AnalyticsRow
import gaurav.cuvettecalltracker.presentation.util.CallType

@Composable
fun HomeScreen() {
    LazyColumn(
        Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        contentPadding = PaddingValues(
            vertical = 15.dp,
            horizontal = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            AnalyticsRow(Modifier.padding(horizontal = 5.dp))
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
        items(10) {
            CallLogCard(
                CallLog(
                    id = "",
                    contactId = "",
                    timestamp = 1734024741,
                    callType = CallType.OUTGOING,
                    duration = 3534,
                    sim = 1
                ),
                onClick = {}
            )
        }
    }
}

@Composable
fun AllLogsCardButton(
    iconResourceId: Int = R.drawable.baseline_search_24,
    iconTint: Color = Color.White,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier
            .background(
                color = Color(0xFF0C0C0C),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(iconResourceId),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier
                .size(34.dp)
                .background(
                    color = Color(0x37D9D9D9),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(3.dp),
        )
        Text(
            text = "See\nAll logs",
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}