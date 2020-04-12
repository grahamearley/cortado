package xyz.egie.cortado

import android.content.Context
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class CortadoPreferences(private val context: Context) {

    companion object {
        private const val KEY_PREVIOUS_DIM_TIMEOUT = "KEY_PREVIOUS_DIM_TIMEOUT"

        // TODO: Add preferences UI for setting fallback time?
        private const val DEFAULT_DIM_TIMEOUT_MS = 15_000L
    }

    private val preferences = context.getSharedPreferences("Cortado", Context.MODE_PRIVATE)

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
