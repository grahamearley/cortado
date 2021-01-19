package xyz.egie.cortado.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import xyz.egie.cortado.R
import xyz.egie.cortado.data.CortadoPreferences
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val preferences by lazy {
        CortadoPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences.isTileAdded.observe(this) { isTileAdded ->
            onTileAddedChanged(isTileAdded)
        }
    }

    private fun onTileAddedChanged(isAdded: Boolean) {
        if (isAdded) showSettings() else showIntroduction()
    }

    private fun showIntroduction() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragmentContainer, IntroductionFragment())
            .commit()
    }

    private fun showSettings() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.fragmentContainer, SettingsFragment())
            .commit()
    }
}
