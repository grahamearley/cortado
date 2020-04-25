package xyz.egie.cortado

import android.content.Intent
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
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

    override fun onTileAdded() {
        super.onTileAdded()
        preferences.isTileAdded.value = true
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        preferences.isTileAdded.value = false
    }

    override fun onClick() {
        super.onClick()

        val canWrite = Settings.System.canWrite(this)

        if (canWrite) {
            toggleCortado()
        } else {
            // TODO: Add info page for why you need to do this
            val permissionIntent = Intent(ACTION_MANAGE_WRITE_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivityAndCollapse(permissionIntent)
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
