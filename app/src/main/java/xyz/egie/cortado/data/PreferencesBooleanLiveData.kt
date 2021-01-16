package xyz.egie.cortado.data

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData

class PreferencesBooleanLiveData(
    private val preferences: SharedPreferences,
    private val key: String
) : MutableLiveData<Boolean>() {

    private val currentPreferencesValue: Boolean
        get() = preferences.getBoolean(key, false)

    private val preferenceListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == this@PreferencesBooleanLiveData.key) {
                value = currentPreferencesValue
            }
        }

    init {
        value = currentPreferencesValue
    }

    override fun getValue(): Boolean {
        return super.getValue() ?: currentPreferencesValue
    }

    override fun setValue(value: Boolean?) {
        preferences.edit().putBoolean(key, value ?: false).apply()
        super.setValue(value)
    }

    override fun onActive() {
        super.onActive()
        preferences.registerOnSharedPreferenceChangeListener(preferenceListener)
    }

    override fun onInactive() {
        super.onInactive()
        preferences.unregisterOnSharedPreferenceChangeListener(preferenceListener)
    }
}