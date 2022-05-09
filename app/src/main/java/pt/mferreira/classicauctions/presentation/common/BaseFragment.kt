package pt.mferreira.classicauctions.presentation.common

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
	abstract fun setupUI()
	abstract fun setupButtons()
	abstract fun setupObservers()
	abstract fun setupStrings()
}