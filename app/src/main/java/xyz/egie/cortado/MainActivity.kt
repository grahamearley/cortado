package xyz.egie.cortado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
class MainActivity : AppCompatActivity() {

    private val preferences by lazy {
        CortadoPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences.isTileAdded.observe(this,
            Observer { isTileAdded ->
                mainTextView.text = "Tile added? $isTileAdded"
            }
        )
    }
}
