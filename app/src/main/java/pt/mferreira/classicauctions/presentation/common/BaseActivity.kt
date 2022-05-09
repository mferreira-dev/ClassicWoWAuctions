package pt.mferreira.classicauctions.presentation.common

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
	abstract fun setupUI()
	abstract fun setupButtons()
	abstract fun setupObservers()
}