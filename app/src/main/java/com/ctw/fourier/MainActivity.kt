package com.ctw.fourier

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.input.key.Key.Companion.D
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

var list = mutableListOf<Offset>()

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
        mutableStateOf(500f)
    }

    LaunchedEffect(key1 = time) {
        time += speed
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .onGloballyPositioned {
                val bounds = it.boundsInWindow()
                centerX = bounds.size.width / 5
            }
    ) {
        Canvas(
            modifier = Modifier
                .shadow(1.dp)
                .background(Color.Black)
                .fillMaxWidth()
                .height(350.dp)
        ) {
            val end = fourierSeries(
                center = Offset(centerX, centerY),
                baseRadius = baseRadius,
                complexity = complexity,
                time = time
            )
            list.add(0, end)
            if (list.size >= 350) {
                list = list.dropLast(1).toMutableList()
            }
            for (i in 0..list.size - 1) {
                val off = 700f
                val element = list.elementAt(i)
                drawPoints(
                    listOf(Offset(i.toFloat() + off, element.y)),
                    pointMode = PointMode.Points,
                    color = Color.Red,
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
                if (i == 0) {
                    drawLine(
                        start = end,
                        end = Offset(i.toFloat() + off, element.y),
                        color = Color.White,
                        strokeWidth = 1.5f
                    )
                }
            }

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
        val prevX = centerX
        val prevY = centerY
        val n = i * 2 + 1
        val radius = (baseRadius * (4 / (n * PI))).toFloat()
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
