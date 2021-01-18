package xyz.egie.cortado.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_settings.*
import xyz.egie.cortado.R
import xyz.egie.cortado.SettingsManager
import xyz.egie.cortado.data.CortadoPreferences
import xyz.egie.cortado.data.ScreenTimeoutSettingLiveData
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val preferences by lazy {
        CortadoPreferences(requireContext())
    }

    private val settingsManager by lazy {
        SettingsManager(requireContext().contentResolver, preferences)
    }

    private val screenTimeoutSettingLiveData by lazy {
        ScreenTimeoutSettingLiveData(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cortadoSwitch.setOnToggleListener { wasChecked ->
            settingsManager.isScreenDimAtMaxTime = !wasChecked
        }

        screenTimeoutSettingLiveData.observe(this, Observer { onTimeoutChanged(it) })
    }

    private fun onTimeoutChanged(timeout: Duration) {
        val isAtMaxTime = settingsManager.isScreenDimAtMaxTime
        cortadoSwitch.setChecked(isAtMaxTime, timeout)
    }
}