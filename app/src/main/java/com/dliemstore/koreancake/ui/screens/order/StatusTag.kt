import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dliemstore.koreancake.R

@Composable
fun StatusTag(status: String) {
    var borderColor = R.color.black_200
    var backgroundColor = R.color.black_100
    var textColor = R.color.black_900
    if (status == "Selesai") {
        borderColor = R.color.green_200
        backgroundColor = R.color.green_100
        textColor = R.color.green_900
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .border(1.dp, colorResource(borderColor), MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .background(colorResource(backgroundColor))
    ) {
        Text(
            text = status,
            fontSize = 10.sp,
            color = colorResource(textColor),
            modifier = Modifier.padding(6.dp, 0.dp)
        )
    }
}
