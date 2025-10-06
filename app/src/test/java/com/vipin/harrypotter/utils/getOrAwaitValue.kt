package com.vipin.harrypotter.utils

import app.cash.turbine.test
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration

suspend fun <T> StateFlow<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val timeout = time.toDuration(timeUnit.toDurationUnit())
    this.test(timeout = timeout) {
        data = awaitItem()
        cancelAndIgnoreRemainingEvents()
    }
    @Suppress("UNCHECKED_CAST")
    return data as T
}

@OptIn(ExperimentalStdlibApi::class)
private fun TimeUnit.toDurationUnit(): DurationUnit = when (this) {
    TimeUnit.NANOSECONDS -> DurationUnit.NANOSECONDS
    TimeUnit.MICROSECONDS -> DurationUnit.MICROSECONDS
    TimeUnit.MILLISECONDS -> DurationUnit.MILLISECONDS
    TimeUnit.SECONDS -> DurationUnit.SECONDS
    TimeUnit.MINUTES -> DurationUnit.MINUTES
    TimeUnit.HOURS -> DurationUnit.HOURS
    TimeUnit.DAYS -> DurationUnit.DAYS
}
