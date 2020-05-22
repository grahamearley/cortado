package xyz.egie.cortado

import android.content.Context
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

sealed class Setting {

    abstract fun getName(context: Context): String

    @ExperimentalTime
    sealed class TimeoutWhenEnabledSetting(val duration: Duration) : Setting() {
        object MaxTime : TimeoutWhenEnabledSetting(Int.MAX_VALUE.toDuration(TimeUnit.MILLISECONDS))
        class FixedTime(duration: Duration, val displayTimeUnit: DurationUnit) : TimeoutWhenEnabledSetting(duration)

        override fun getName(context: Context) = when (this) {
            MaxTime -> context.getString(R.string.setting_max_time)
            is FixedTime -> {
                val durationInt = duration.toInt(displayTimeUnit)

                val formatRes = when (this.displayTimeUnit) {
                    TimeUnit.NANOSECONDS -> R.string.setting_fixed_time_nanos
                    TimeUnit.MICROSECONDS -> R.string.setting_fixed_time_micros
                    TimeUnit.MILLISECONDS -> R.string.setting_fixed_time_millis
                    TimeUnit.SECONDS -> R.string.setting_fixed_time_sec
                    TimeUnit.MINUTES -> R.string.setting_fixed_time_mins
                    TimeUnit.HOURS -> R.string.setting_fixed_time_hours
                    TimeUnit.DAYS -> R.string.setting_fixed_time_days
                }

                context.getString(formatRes, durationInt)
            }
        }
    }
}