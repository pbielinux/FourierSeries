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
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
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
            FourierScreen()
        }
    }
}

@Composable
fun FourierScreen() {
    var speed by remember {
        mutableStateOf(0.01f)
    }
    var baseRadius by remember {
        mutableStateOf(180f)
    }
    var complexity by remember {
        mutableStateOf(10)
    }
    var time by remember {
        mutableStateOf(0f)
    }
    var centerX by remember {
        mutableStateOf(0f)
    }
    var centerY by remember {
        mutableStateOf(150f)
    }

    LaunchedEffect(key1 = time) {
        time += speed
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
            .fillMaxSize()
            .onGloballyPositioned {
                val bounds = it.boundsInWindow()
                centerX = bounds.size.width / 2
            }
    ) {
        Canvas(
            modifier = Modifier
                .shadow(1.dp)
                .background(Color.Black)
                .fillMaxWidth()
                .height(350.dp)
        ) {
            fourierSeries(
                center = Offset(centerX, centerY),
                baseRadius = baseRadius,
                complexity = complexity,
                time = time
            )
        }

        FourierSlider(
            text = "Speed",
            value = speed,
            valueRange = 0.001f..0.02f
        ) { speed = it }
        FourierSlider(
            text = "Zoom",
            value = baseRadius,
            valueRange = 50f..1000f,
            steps = 9
        ) { baseRadius = it }
        FourierSlider(
            text = "Complexity",
            value = complexity.toFloat(),
            valueRange = 0f..100f
        ) { complexity = it.toInt() }
        FourierSlider(
            text = "Center X",
            value = centerX,
            valueRange = 0f..1000f
        ) { centerX = it }
        FourierSlider(
            text = "Center Y",
            value = centerY,
            valueRange = 0f..1000f
        ) { centerY = it }

    }
}

fun DrawScope.fourierSeries(
    center: Offset,
    baseRadius: Float,
    complexity: Int,
    time: Float
) : Offset {
    var centerX = center.x
    var centerY = center.y
    for (i in 0..complexity) {
        var prevX = centerX
        var prevY = centerY
        var n = i * 2 + 1
        var radius = (baseRadius * (4 / (n * PI))).toFloat()
        centerX += radius * cos(n * time)
        centerY += radius * sin(n * time)
        fouriercircle(
            radius = radius,
            center = Offset(x = prevX, y = prevY),
            end = Offset(centerX, centerY)
        )
        if (i == complexity) {
            return Offset(centerX, centerY)
        }
    }
    return Offset(0f,0f)
}

fun DrawScope.fouriercircle(
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
        listOf(center),
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

@Composable
fun FourierSlider(
    text: String = "Feature",
    value: Float = 0f,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            text = text
        )
        Text(
            fontSize = 10.sp,
            text = value.toString()
        )
        Slider(
            modifier = Modifier.padding(horizontal = 10.dp),
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps
        )
    }
}
