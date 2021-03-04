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
package com.example.androiddevchallenge.viewmodel

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CountDownViewModel : ViewModel() {
    private var timer: CountDownTimer? = null

    private val _isStarted = MutableLiveData(false)
    val isStarted: LiveData<Boolean> = _isStarted

    private val _countdownProgress = MutableLiveData(1f)
    val countdownProgress: LiveData<Float> = _countdownProgress

    private val _labelTimer = MutableLiveData("01:00")
    val labelTimer: MutableLiveData<String>
        get() = _labelTimer

    private val _timeInSeconds = MutableLiveData(0L)
    val timeInMinutes: MutableLiveData<Long>
        get() = _timeInSeconds

    fun startTimer() {
        val interval = TimeUnit.SECONDS.toMillis(_timeInSeconds.value ?: 0)
        timer = object : CountDownTimer(interval, 1000) {
            @SuppressLint("SimpleDateFormat")
            override fun onTick(millisUntilFinished: Long) {
                val formatter = SimpleDateFormat("mm:ss")
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = millisUntilFinished
                _labelTimer.postValue(formatter.format(calendar.time))
                _countdownProgress.value =
                    millisUntilFinished.toFloat() / 1000 / _timeInSeconds.value!!
                _isStarted.value = true
            }
            override fun onFinish() {
                _labelTimer.postValue("Finished!")
                _countdownProgress.value = 0F
                Handler().postDelayed(
                    {
                        stopTimer()
                    },
                    1200
                )
            }
        }
        timer?.start()
    }

    fun stopTimer() {
        timer?.apply {
            cancel()
            timer = null
        }
        _countdownProgress.value = 0f
        _labelTimer.value = ""
        _isStarted.value = false
        _timeInSeconds.value = 0
    }

    fun setSeconds(minutes: Long) {
        _timeInSeconds.postValue(minutes)
    }
}
