package xyz.egie.cortado

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import xyz.egie.cortado.data.CortadoPreferences
import xyz.egie.cortado.ui.PermissionExplanationActivity
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class SettingsManager(
    private val context: Context,
    private val preferences: CortadoPreferences = CortadoPreferences(context)
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
            return getScreenOffTimeout(context.contentResolver) ?: preferences.previousDimTimeout
        }
        set(value) {
            try {
                Settings.System.putString(
                        context.contentResolver,
                        Settings.System.SCREEN_OFF_TIMEOUT,
                        value.toLongMilliseconds().toInt().toString()
                )
            } catch (exception: SecurityException) {
                launchSettingsExplanation()
            }
        }

    private fun launchSettingsExplanation() {
        val explanationIntent = Intent(context, PermissionExplanationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(explanationIntent)
    }
}
