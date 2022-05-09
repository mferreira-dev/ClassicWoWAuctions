package pt.mferreira.classicauctions.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pt.mferreira.classicauctions.ClassicAuctions
import pt.mferreira.classicauctions.data.network.interceptors.AuthorizationInterceptor
import pt.mferreira.classicauctions.data.network.interceptors.ConnectivityInterceptor
import pt.mferreira.classicauctions.data.network.interceptors.TokenInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
	val getBattleNetClient: Endpoints
		get() {
			val logging = HttpLoggingInterceptor()
			logging.setLevel(HttpLoggingInterceptor.Level.BODY)

			val client: OkHttpClient = OkHttpClient.Builder()
				.connectTimeout(1, TimeUnit.MINUTES)
				.addInterceptor(AuthorizationInterceptor())
				.addInterceptor(logging)
				.addInterceptor(ConnectivityInterceptor(ClassicAuctions.applicationContext()))
				.readTimeout(30, TimeUnit.SECONDS)
				.build()

			val gson = GsonBuilder()
				.setLenient()
				.create()

			return Retrofit.Builder()
				.baseUrl("https://eu.battle.net/")
				.client(client)
				.addConverterFactory(ScalarsConverterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build()
				.create(Endpoints::class.java)
		}

	val getWoWClient: Endpoints
		get() {
			val logging = HttpLoggingInterceptor()
			logging.setLevel(HttpLoggingInterceptor.Level.BODY)

			val client: OkHttpClient = OkHttpClient.Builder()
				.connectTimeout(1, TimeUnit.MINUTES)
				.addInterceptor(TokenInterceptor())
				.addInterceptor(logging)
				.addInterceptor(ConnectivityInterceptor(ClassicAuctions.applicationContext()))
				.readTimeout(30, TimeUnit.SECONDS)
				.build()

			val gson = GsonBuilder()
				.setLenient()
				.create()

			return Retrofit.Builder()
				.baseUrl("https://eu.api.blizzard.com/data/wow/")
				.client(client)
				.addConverterFactory(ScalarsConverterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build()
				.create(Endpoints::class.java)
		}
}