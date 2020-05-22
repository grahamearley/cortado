package xyz.egie.cortado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val preferences by lazy {
        CortadoPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences.isTileAdded.observe(this,
            Observer { isTileAdded -> onTileAddedChanged(isTileAdded) }
        )
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
