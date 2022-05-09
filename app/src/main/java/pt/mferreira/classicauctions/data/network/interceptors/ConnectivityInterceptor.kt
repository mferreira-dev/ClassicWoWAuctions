package pt.mferreira.classicauctions.data.network.interceptors

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import pt.mferreira.classicauctions.utils.NoConnectivityException

class ConnectivityInterceptor(private val context: Context) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		if (!isOnline()) throw NoConnectivityException()
		return chain.proceed(chain.request())
	}

	private fun isOnline(): Boolean {
		val connectivityManager =
			context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val networkInfo = connectivityManager.activeNetworkInfo
		return networkInfo != null && networkInfo.isConnected
	}
}