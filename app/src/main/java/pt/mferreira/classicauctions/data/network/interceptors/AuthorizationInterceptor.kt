package pt.mferreira.classicauctions.data.network.interceptors

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response
import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.R

class AuthorizationInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val cid = ClassicAuctions.applicationContext().getString(R.string.CID)
		val cs = ClassicAuctions.applicationContext().getString(R.string.CS)

		val auth = "Basic " + Base64.encodeToString("$cid:$cs".toByteArray(), Base64.NO_WRAP)

		var request = chain.request()

		request = request.newBuilder()
			.addHeader("Authorization", auth)
			.build()

		return chain.proceed(request)
	}
}