package pt.mferreira.classicauctions

import android.content.Context
import android.support.multidex.MultiDexApplication
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp

class ClassicAuctions : MultiDexApplication() {
	companion object {
		var instance: ClassicAuctions? = null

		fun applicationContext(): Context {
			return instance!!.applicationContext
		}
	}

	override fun onCreate() {
		super.onCreate()
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		FirebaseApp.initializeApp(this)
		instance = this
	}
}