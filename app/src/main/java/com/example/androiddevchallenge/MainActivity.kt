/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.viewmodel.CountDownViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.androiddevchallenge.ui.theme.Lavender7
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.Ocean10
import com.example.androiddevchallenge.ui.theme.customFontFamily
import androidx.compose.foundation.layout.*

class MainActivity : AppCompatActivity() {
    private val countDownViewModel by viewModels<CountDownViewModel>()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp(countdownViewModel = countDownViewModel)
            }
        }
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp(countdownViewModel: CountDownViewModel) {
    val isStarted by countdownViewModel.isStarted.observeAsState(false)
    val progress by countdownViewModel.countdownProgress.observeAsState(0f)
    val label by countdownViewModel.labelTimer.observeAsState(initial = "")
    val seconds by countdownViewModel.timeInMinutes.observeAsState()

    Surface(color = MaterialTheme.colors.background) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(Ocean10, Lavender7)
                    )
                )
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box {

                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp,
                    modifier = Modifier,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedVisibility(visible = isStarted) {
                            CountDownView(progress, label)
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        AnimatedVisibility(visible = !isStarted) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .requiredWidth(240.dp)
                                    .padding(top = 24.dp),
                                value = "$seconds",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions {
                                    countdownViewModel.startTimer()
                                },
                                onValueChange = {
                                    countdownViewModel.setSeconds(it.toLongOrNull() ?: 0)
                                },
                                label = {
                                    Text(
                                        text = "Seconds",
                                        style = TextStyle(
                                            fontFamily = customFontFamily,
                                            color = Color.DarkGray,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Thin,
                                        )
                                    )
                                }
                            )
                        }
                        IconButton(
                            enabled = isStarted || !isStarted,
                            onClick = {
                                if (isStarted) countdownViewModel.stopTimer()
                                else countdownViewModel.startTimer()
                            },
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                .size(50.dp)
                                .background(
                                    color = Lavender7,
                                    shape = CircleShape
                                )
                        ) {
                            val drawable = if (isStarted)
                                R.drawable.ic_baseline_stop_24
                            else R.drawable.ic_baseline_play_arrow_24
                            Icon(
                                imageVector = ImageVector.vectorResource(id = drawable),
                                tint = Color.White,
                                contentDescription = "Action",
                                modifier = Modifier
                                    .size(42.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CountDownView(progress: Float, label: String) {
    Box(modifier = Modifier
        .aspectRatio(ratio = 1f)
        .padding(16.dp),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = progress,
            color = Lavender7,
            strokeWidth = Dp(value = 20F),
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = customFontFamily,
                    color = Color.DarkGray,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Medium,
                )
            )
        }
    }
}

@Preview
@Composable
fun CountPrev(){
    CountDownView(progress = 0.4f, label = "00.20")
}
