package pt.mferreira.classicauctions.presentation.container

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.forEachIndexed
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import androidx.preference.PreferenceManager
import pt.mferreira.classicauctions.R
import pt.mferreira.classicauctions.databinding.ActivityContainerBinding
import pt.mferreira.classicauctions.presentation.common.BaseActivity
import pt.mferreira.classicauctions.utils.extensions.hideKeyboard

class ContainerActivity : BaseActivity() {
	private lateinit var binding: ActivityContainerBinding
	private lateinit var viewModel: ContainerViewModel

	private lateinit var navController: NavController
	private lateinit var appBarConfiguration: AppBarConfiguration

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val preferences = PreferenceManager.getDefaultSharedPreferences(this)
		setNightMode(preferences.getBoolean(getString(R.string.night_mode_key), false))

		binding = ActivityContainerBinding.inflate(layoutInflater)
		setContentView(binding.root)
		viewModel = ViewModelProvider(this).get(ContainerViewModel::class.java)

		setupUI()
		setupButtons()
		setupObservers()
	}

	override fun setupUI() {
		val navHostFragment =
			supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
		navController = navHostFragment.findNavController()

		appBarConfiguration = AppBarConfiguration(
			setOf(R.id.welcomeFragment), binding.drawerLayout
		)

		setSupportActionBar(binding.toolbar)
		setupActionBarWithNavController(navController, appBarConfiguration)

		binding.navView.setupWithNavController(navController)

		binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
	}

	override fun setupButtons() {}

	override fun setupObservers() {
		viewModel.welcomeViewState.observe(this) {
			render(it)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.options_menu, menu)

		menu?.forEachIndexed { index, item ->
			val item = menu?.getItem(index)
			val spanString = SpannableString(menu?.getItem(index).title.toString())
			spanString.setSpan(
				ForegroundColorSpan(Color.parseColor("#808080")),
				0,
				spanString.length,
				0
			)
			item.title = spanString
		}

		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
	}

	override fun onSupportNavigateUp(): Boolean {
		binding.navView.hideKeyboard()
		return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
	}

	private fun render(viewState: ContainerViewModel.ContainerViewState) {
		binding.welcomeLoading.visibility = viewState.loading

		if (viewState.loading != View.GONE)
			window.setFlags(
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
			)
		else
			window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
	}

	private fun setNightMode(isNightModeActivated: Boolean) {
		if (isNightModeActivated)
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
		else
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
	}
}