package xyz.egie.cortado

import android.content.ContentResolver
import android.provider.Settings
import xyz.egie.cortado.data.CortadoPreferences
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class SettingsManager(
    private val contentResolver: ContentResolver,
    private val preferences: CortadoPreferences
) {

    companion object {
        private const val MAX_SCREEN_DIM_TIME = Int.MAX_VALUE

        fun getScreenOffTimeout(contentResolver: ContentResolver): Duration? {
            return Settings.System.getString(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
                ?.toInt()?.toDuration(TimeUnit.MILLISECONDS)
        }
    }

    var isScreenDimAtMaxTime: Boolean
        get() {
            val maxTimeout = MAX_SCREEN_DIM_TIME.toDuration(TimeUnit.MILLISECONDS)
            val currentTimeout = screenOffTimeout

            return maxTimeout == currentTimeout
        }
        set(value) {
            val maxTimeout = MAX_SCREEN_DIM_TIME.toDuration(TimeUnit.MILLISECONDS)
            if (screenOffTimeout != maxTimeout) {
                preferences.previousDimTimeout = screenOffTimeout
            }

            screenOffTimeout = if (value) {
                maxTimeout
            } else {
                preferences.previousDimTimeout
            }
        }

    private var screenOffTimeout: Duration
        get() {
            return getScreenOffTimeout(contentResolver) ?: preferences.previousDimTimeout
        }
        set(value) {
            Settings.System.putString(
                contentResolver,
                Settings.System.SCREEN_OFF_TIMEOUT,
                value.toLongMilliseconds().toInt().toString()
            )
        }
}
