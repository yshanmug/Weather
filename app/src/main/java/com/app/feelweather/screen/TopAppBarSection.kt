package com.app.feelweather.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopAppBarSection(
    isDay: Int?, locationName: String, onBackPress: () -> Unit, isFromFutureScreen: Boolean,
) {
    val currentDate = LocalDate.now()
    val month = currentDate.month.name.lowercase().replaceFirstChar(Char::titlecase)
    val dayOfMonth = currentDate.dayOfMonth
    val year = currentDate.year
    val currentDateValue: String = "$month $dayOfMonth, $year"
    val iconAndTextColor = if (isFromFutureScreen) {
        Color.Unspecified // No tint, uses default icon/text color (black by default)
    } else {
        if (isSystemInDarkTheme() || isDay == 0) Color.White else Color.Black
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onBackPress()
        }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                tint = iconAndTextColor.takeIf { it != Color.Unspecified } ?: Color.Black,
                contentDescription = "Back Button "
            )
        }


        if (isFromFutureScreen) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location Icon",
                )

                Text(
                    text = locationName,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                )
            }

            Spacer(modifier = Modifier.width(48.dp))

        } else {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location Icon",
                    tint = if (isSystemInDarkTheme() || isDay == 0) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
                Text(
                    text = locationName,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp,
                    color = if (isSystemInDarkTheme() || isDay == 0) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }
            Spacer(modifier = Modifier.width(48.dp))
        }
    }
}

//}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun TopAppBarSectionPreview() {
    TopAppBarSection(
        1,
        "",
        onBackPress = {},
        false
    )
}
