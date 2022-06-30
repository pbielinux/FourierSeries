package com.ctw.fourier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FourierCircle(200f, Offset(0f, 0f), 10)
        }
    }
}

@Composable
fun FourierCircle(
    baseRadius: Float,
    center: Offset,
    complexity: Int
) {
    var time by remember {
        mutableStateOf(0f)
    }
    var angleIncrement by remember {
        mutableStateOf(0.01f)
    }
    var size by remember {
        mutableStateOf(180f)
    }
    var complex by remember {
        mutableStateOf(complexity)
    }
    var centerX = remember { center.x }
    var centerY = remember { center.y }

    LaunchedEffect(key1 = time) {
        time += angleIncrement
    }


    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
            .fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier
                .shadow(1.dp)
                .background(Color.Black)
                .fillMaxWidth()
                .height(350.dp)
                .offset(350.dp / 2, 350.dp / 2)
        ) {
            for (i in 0..complex) {
                var prevX = centerX
                var prevY = centerY
                var n = i * 2 + 1
                var radius = (size * (4 / (n * PI))).toFloat()
                centerX += radius * cos(n * time)
                centerY += radius * sin(n * time)
                circle(radius, Offset(x = prevX, y = prevY), Offset(centerX, centerY))
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    text = "Speed"
                )
                Slider(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    value = angleIncrement,
                    onValueChange = {
                        angleIncrement = it
                    },
                    valueRange = 0.001f..0.02f,
                    steps = 10
                )
                Text(
                    fontSize = 10.sp,
                    text = angleIncrement.toString()
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    text = "Zoom"
                )
                Slider(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    value = size,
                    onValueChange = {
                        size = it
                    },
                    valueRange = 0f..360f,
                    steps = 9
                )
                Text(
                    fontSize = 10.sp,
                    text = size.toString()
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    text = "Complexity"
                )
                Slider(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    value = complex.toFloat(),
                    onValueChange = {
                        complex = it.toInt()
                    },
                    valueRange = 0f..100f
                )
                Text(
                    fontSize = 10.sp,
                    text = complex.toString()
                )
            }
        }
    }
}

fun DrawScope.circle(
    radius: Float,
    center: Offset,
    end: Offset
) {
    drawCircle(
        color = Color.White,
        radius = radius,
        center = Offset(center.x, center.y),
        style = Stroke(width = 1.5f)
    )
    drawPoints(
        listOf(
            center
        ),
        pointMode = PointMode.Points,
        color = Color.White,
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )
    drawLine(
        start = center,
        end = end,
        color = Color.White,
        strokeWidth = 1.5f
    )
}