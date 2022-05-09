package pt.mferreira.classicauctions.presentation.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import pt.mferreira.classicauctions.R

class SettingsFragment : PreferenceFragmentCompat(),
	SharedPreferences.OnSharedPreferenceChangeListener {

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.fragment_settings, rootKey)
	}

	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
		val preferences = PreferenceManager.getDefaultSharedPreferences(activity)

		when (key) {
			getString(R.string.night_mode_key) -> setNightMode(preferences.getBoolean(key, false))
		}
	}

	private fun setNightMode(isNightModeActivated: Boolean) {
		if (isNightModeActivated)
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
		else
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
	}

	override fun onResume() {
		super.onResume()
		preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
	}

	override fun onPause() {
		preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
		super.onPause()
	}
}