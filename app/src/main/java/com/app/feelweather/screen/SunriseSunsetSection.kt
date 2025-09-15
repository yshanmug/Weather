package com.app.feelweather.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.feelweather.R
import com.app.feelweather.model.weatherdata.WeatherDailyData

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SunriseSunsetSection(todayDailyData: WeatherDailyData?) {

    val context = LocalContext.current
    val sunrise = formatTimeBasedOnSystem(context, todayDailyData?.sunrise)
    val sunset = formatTimeBasedOnSystem(context, todayDailyData?.sunset)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(140.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.elevatedCardColors(Color(0xFFF0F0F0)),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Icon(
                    painter = painterResource(R.drawable.sunrise_sunset),
                    contentDescription = "Sunrise Sunset",
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Sunrise & Sunset",
                    fontWeight = FontWeight.Bold

                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    modifier = Modifier.wrapContentHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(BlobShape(cornerRadius = 20.dp, indent = 7.dp))
                            .background(Color(0xFFE0E0E0))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Sunrise",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = sunrise,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
                Icon(
                    painter = painterResource(R.drawable.sunrise_11),
                    contentDescription = "sunrise",
                    modifier = Modifier.size(80.dp)
                )

                Icon(
                    painter = painterResource(R.drawable.sunset_11),
                    contentDescription = "sunset",
                    modifier = Modifier.size(80.dp)
                )


                Column(
                    modifier = Modifier.wrapContentHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(BlobShape(cornerRadius = 20.dp, indent = 7.dp))
                            .background(Color(0xFFE0E0E0))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Sunset",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = sunset,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

            }


        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun SunriseSunsetSectionPreview() {
    val mockDailyData = WeatherDailyData(
        time = "",
        weatherType = "",
        weatherCode = 0,
        sunrise = "5:45 AM",
        sunset = "9:00 PM",
        maxTemp = 0f,
        minTemp = 0f

    )
    SunriseSunsetSection(mockDailyData)

}


class BlobShape(private val cornerRadius: Dp, private val indent: Dp) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        return Outline.Generic(Path().apply {
            val width = size.width
            val height = size.height
            val cornerRadiusPx = with(density) { cornerRadius.toPx() }
            val indentPx = with(density) { indent.toPx() }

            // Start drawing the path from the top-left corner, after the curve
            // This ensures a smooth start for the first quadratic Bezier curve
            moveTo(cornerRadiusPx, 0f)

            // Top edge: Draws a quadratic Bezier curve from the current point
            // to the point before the top-right corner, using `(width / 2, indentPx)`
            // as the control point to create an inward curve.
            quadraticTo(width / 2, indentPx, width - cornerRadiusPx, 0f)

            // Top-right corner curve: Draws a quadratic Bezier curve to round the top-right corner.
            // The control point `(width, 0f)` is the actual top-right corner, pulling the curve.
            quadraticTo(width, 0f, width, cornerRadiusPx)

            // Right edge: Draws a quadratic Bezier curve from the current point
            // to the point before the bottom-right corner, using `(width - indentPx, height / 2)`
            // as the control point to create an inward curve.
            quadraticTo(width - indentPx, height / 2, width, height - cornerRadiusPx)

            // Bottom-right corner curve: Draws a quadratic Bezier curve to round the bottom-right corner.
            // The control point `(width, height)` is the actual bottom-right corner.
            quadraticTo(width, height, width - cornerRadiusPx, height)

            // Bottom edge: Draws a quadratic Bezier curve from the current point
            // to the point before the bottom-left corner, using `(width / 2, height - indentPx)`
            // as the control point to create an inward curve.
            quadraticTo(width / 2, height - indentPx, cornerRadiusPx, height)

            // Bottom-left corner curve: Draws a quadratic Bezier curve to round the bottom-left corner.
            // The control point `(0f, height)` is the actual bottom-left corner.
            quadraticTo(0f, height, 0f, height - cornerRadiusPx)

            // Left edge: Draws a quadratic Bezier curve from the current point
            // to the point before the top-left corner, using `(indentPx, height / 2)`
            // as the control point to create an inward curve.
            quadraticTo(indentPx, height / 2, 0f, cornerRadiusPx)

            // Top-left corner curve (completing the path): Draws a quadratic Bezier curve to
            // round the top-left corner and close the shape.
            // The control point `(0f, 0f)` is the actual top-left corner.
            quadraticTo(0f, 0f, cornerRadiusPx, 0f)

            // Close the path to complete the shape outline.
            close()
        })
    }
}