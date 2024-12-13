package gaurav.cuvettecalltracker.presentation.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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