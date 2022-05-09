package pt.mferreira.classicauctions.utils.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Returns an empty string.
 */
fun String.Companion.empty() = ""

/**
 * Returns "eu".
 */
fun String.Companion.eu() = "eu"

/**
 * Returns "us".
 */
fun String.Companion.us() = "us"


/**
 * Hides keyboard.
 */
fun View.hideKeyboard() {
	val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	imm.hideSoftInputFromWindow(windowToken, 0)
}