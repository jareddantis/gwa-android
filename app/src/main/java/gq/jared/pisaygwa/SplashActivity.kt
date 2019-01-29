package gq.jared.pisaygwa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import gq.jared.pisaygwa.main.MainActivity

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}