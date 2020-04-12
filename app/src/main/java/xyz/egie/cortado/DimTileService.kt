package xyz.egie.cortado

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS
import android.service.quicksettings.TileService
import android.util.Log
import android.widget.Toast
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class DimTileService : TileService() {

    private val settingsManager by lazy {
        SettingsManager(this, contentResolver)
    }

    override fun onStartListening() {
        super.onStartListening()

    }

    override fun onStopListening() {
        super.onStopListening()
    }

    override fun onClick() {
        super.onClick()

        val canWrite = Settings.System.canWrite(this)
        val

        if (canWrite) {
            Toast.makeText(this, "Go drink a cortado", Toast.LENGTH_LONG).show()
            settingsManager.screenOffTimeout = Int.MAX_VALUE
            Settings.System.putString(contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, Int.MAX_VALUE.toString())
        } else {
            // TODO: Add timer
            startActivity(Intent(ACTION_MANAGE_WRITE_SETTINGS).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
            })
        }
    }
}
