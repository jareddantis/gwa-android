package gq.jared.pisaygwa.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gq.jared.pisaygwa.R

class Splash: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        val intent = Intent(this, Main::class.java)
        startActivity(intent)
        finish()
    }

}