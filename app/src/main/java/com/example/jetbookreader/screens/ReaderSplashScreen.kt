package com.example.jetbookreader.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ReaderSplashScreen(navController: NavController) {
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(0f) }

    // Create a coroutine scope to handle the animations
    val scope = rememberCoroutineScope()

    // Launch the animations in parallel inside LaunchedEffect
    LaunchedEffect(Unit) {
        scope.launch {
            // Start the rotation animation
            launch {
                rotation.animateTo(
                    targetValue = 360f,
                    animationSpec = tween(durationMillis = 2000) // 2-second rotation
                )
            }

            // Start the scale animation with overshoot interpolator
            launch {
                scale.animateTo(
                    targetValue = 0.9f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = {
                            OvershootInterpolator(8f).getInterpolation(it)
                        }
                    )
                )
                // Optional delay after scaling
                delay(2000L)
            }
        }
    }
    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .rotate(rotation.value)
            .scale(scale.value),
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = Color.LightGray
        )
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Jet Book Reader",
                style = MaterialTheme.typography.displaySmall,
                color = Color.Red.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "\"Read. Change. Yourself\"",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.LightGray
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun ReaderSplashScreenPreview() {
    ReaderSplashScreen(navController = rememberNavController())
}