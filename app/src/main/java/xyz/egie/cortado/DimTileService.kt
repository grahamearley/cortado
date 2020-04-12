package xyz.egie.cortado

import android.content.Intent
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import kotlin.time.ExperimentalTime

@ExperimentalTime
class DimTileService : TileService() {

    private val preferences by lazy {
        CortadoPreferences(this)
    }

    private val settingsManager by lazy {
        SettingsManager(contentResolver, preferences)
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()

        val canWrite = Settings.System.canWrite(this)

        if (canWrite) {
            Toast.makeText(this, "Go drink a cortado", Toast.LENGTH_LONG).show()
            toggleCortado()
        } else {
            // TODO: Add info page for why you need to do this
            startActivityAndCollapse(Intent(ACTION_MANAGE_WRITE_SETTINGS))
        }
    }

    private fun toggleCortado() {
        settingsManager.isScreenDimAtMaxTime = !settingsManager.isScreenDimAtMaxTime
        updateTileState()
    }

    private fun updateTileState() {
        qsTile.state = when (settingsManager.isScreenDimAtMaxTime) {
            true -> Tile.STATE_ACTIVE
            false -> Tile.STATE_INACTIVE
        }

        qsTile.updateTile()
    }
}
