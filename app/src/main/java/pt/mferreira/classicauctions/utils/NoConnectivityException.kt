package pt.mferreira.classicauctions.utils

import java.io.IOException

class NoConnectivityException : IOException() {
	override val message: String = "NoConnectivityException"
}