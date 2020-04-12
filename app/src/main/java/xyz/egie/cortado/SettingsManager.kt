package xyz.egie.cortado

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.annotation.IntegerRes
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class SettingsManager(
    private val context: Context,
    private val contentResolver: ContentResolver
) {

    companion object {
        private const val MAX_SCREEN_DIM_TIME = Int.MAX_VALUE
    }

    var isScreenDimAtMaxTime: Boolean
        get() {
            val maxTime = MAX_SCREEN_DIM_TIME.toDuration(TimeUnit.SECONDS)
            val currentTime = screenOffTimeout

            return maxTime == currentTime
        }
        set(value) {
            val maxTime = MAX_SCREEN_DIM_TIME.toDuration(TimeUnit.SECONDS)

            screenOffTimeout = if (value) {
                maxTime
            } else {
                preferences.previousDimTimeout
            }
        }

    private var screenOffTimeout: Duration
        get() {
            return Settings.System.getString(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
                ?.toInt()?.toDuration(TimeUnit.SECONDS) ?: preferences.previousDimTimeout
        }
        set(value) {
            Settings.System.putString(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, value.toString())
        }

    private val preferences by lazy {
        CortadoPreferences(context)
    }
}
