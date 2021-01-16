package xyz.egie.cortado.data

import android.content.Context
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class CortadoPreferences(context: Context) {

    companion object {
        private const val KEY_PREVIOUS_DIM_TIMEOUT = "KEY_PREVIOUS_DIM_TIMEOUT"
        private const val KEY_IS_ADDED = "KEY_IS_ADDED"

        private const val DEFAULT_DIM_TIMEOUT_MS = 15_000L
    }

    private val preferences = context.getSharedPreferences("Cortado", Context.MODE_PRIVATE)

    /**
     * Whether the Cortado quick settings tile is added to the user's list of tiles.
     */
    val isTileAdded = PreferencesBooleanLiveData(preferences, KEY_IS_ADDED)

    /**
     * The dim timeout time to use when Cortado is not active.
     */
    var previousDimTimeout: Duration
        get() {
            val savedMs = preferences.getLong(KEY_PREVIOUS_DIM_TIMEOUT, DEFAULT_DIM_TIMEOUT_MS)
            return savedMs.toDuration(TimeUnit.MILLISECONDS)
        }
        set(value) {
            preferences.edit().putLong(KEY_PREVIOUS_DIM_TIMEOUT, value.toLong(TimeUnit.MILLISECONDS)).apply()
        }
}
