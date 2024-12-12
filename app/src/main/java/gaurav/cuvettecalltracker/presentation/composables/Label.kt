package gaurav.cuvettecalltracker.presentation.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun Label(
    text: String = "Unlabeled",
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 0.sp,
    )
}