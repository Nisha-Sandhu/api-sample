package com.example.wisdomleafassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.wisdomleafassignment.listItems.view.TechnologiesActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000 // 1 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.Default).launch {
            delay(SPLASH_TIME_OUT)
            withContext(Dispatchers.Main) {
                TechnologiesActivity.startActivity(this@SplashActivity)
            }
        }
    }

}