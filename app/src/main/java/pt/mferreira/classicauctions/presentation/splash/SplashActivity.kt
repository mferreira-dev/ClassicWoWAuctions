package pt.mferreira.classicauctions.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.presentation.container.ContainerActivity

class SplashActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		Handler().postDelayed({
			val intent = Intent(this@SplashActivity, ContainerActivity::class.java)
			startActivity(intent)
			finish()
		}, 1000)
	}
}