package ru.spiritblog.crazyeights

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class CrazyEightActivity : AppCompatActivity() {

    private lateinit var gameView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = CrazyEightView(this)
        gameView.keepScreenOn = true
        setContentView(gameView)
    }


    private fun setToFullScreen() {
        gameView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)

    }

    override fun onResume() {
        super.onResume()
        setToFullScreen()
    }
}