package gaurav.cuvettecalltracker.presentation.log_detail.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gaurav.cuvettecalltracker.R
import gaurav.cuvettecalltracker.presentation.composables.Chip

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.baseline_person_24),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .aspectRatio(1f)
                .border(
                    width = 1.dp,
                    color = Color(0xFFEAEAEA),
                    shape = CircleShape
                )
                .background(
                    color = Color(0xFFEFEFEF),
                    shape = CircleShape
                )
                .padding(40.dp),
            colorFilter = ColorFilter.tint(Color.Gray)
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Gaurav Saini",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = "+919460110818",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp
        )
        Spacer(Modifier.height(15.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Chip("24 Incomings, 45 mins 4 secs")
            Chip("24 Outgoings, 45 mins 4 secs")
            Chip("24 Missed calls")
            Chip("Total calls: 153")
            Chip("Total duration: 4 hours 45 mins 3 secs")
        }
    }
}