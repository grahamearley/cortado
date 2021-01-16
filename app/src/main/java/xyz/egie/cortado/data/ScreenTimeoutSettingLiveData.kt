package xyz.egie.cortado.data

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import androidx.lifecycle.LiveData
import xyz.egie.cortado.SettingsManager
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * A [LiveData] instance whose value reflects the value of the system's
 *  setting for time before the screen dims. This value will automatically
 *  adjust when the system setting changes.
 */
@ExperimentalTime
class ScreenTimeoutSettingLiveData(
    private val context: Context
) : LiveData<Duration>() {

    init {
        updateValueFromSettings()
    }

    private val settingsContentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            updateValueFromSettings()
        }
    }

    override fun onActive() {
        super.onActive()
        context.contentResolver.registerContentObserver(
            android.provider.Settings.System.CONTENT_URI,
            true,
            settingsContentObserver
        )
        updateValueFromSettings()
    }

    override fun onInactive() {
        super.onInactive()
        context.contentResolver.unregisterContentObserver(settingsContentObserver)
    }

    private fun updateValueFromSettings() {
        value = SettingsManager.getScreenOffTimeout(context.contentResolver)
    }
}