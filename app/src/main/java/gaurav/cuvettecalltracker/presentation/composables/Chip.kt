package gaurav.cuvettecalltracker.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Chip(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.sp,
        modifier = modifier
            .border(1.dp, shape = RoundedCornerShape(7.dp), color = Color(0xFFDCDCDC))
            .background(
                color = Color(0xFFEAEAEA),
                shape = RoundedCornerShape(7.dp)
            )
            .padding(vertical = 3.dp, horizontal = 7.dp)
    )
}