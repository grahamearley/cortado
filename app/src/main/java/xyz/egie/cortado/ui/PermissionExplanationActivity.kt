package xyz.egie.cortado.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_permission_explanation.*
import xyz.egie.cortado.R

class PermissionExplanationActivity : AppCompatActivity(R.layout.activity_permission_explanation) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsButton.setOnClickListener {
            val permissionIntent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(permissionIntent)
        }
    }

    override fun onStart() {
        super.onStart()

        // If permission has been granted, close this page.
        val canWrite = Settings.System.canWrite(this)
        if (canWrite) finish()
    }
}