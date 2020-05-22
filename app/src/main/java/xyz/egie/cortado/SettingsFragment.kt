package xyz.egie.cortado

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val dimTimeSettingAdapter by lazy {
        PreferenceSpinnerAdapter(
            listOf(
                Setting.TimeoutWhenEnabledSetting.MaxTime,
                Setting.TimeoutWhenEnabledSetting.FixedTime(
                    45.toDuration(DurationUnit.SECONDS),
                    DurationUnit.SECONDS
                )
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enabledSettingSpinner.adapter = dimTimeSettingAdapter

//        enabledSettingSpinner.onItemSelectedListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            val setting = dimTimeSettingAdapter.getItem(position)
//        }
    }

//    private fun onDimTimeChanged()
}