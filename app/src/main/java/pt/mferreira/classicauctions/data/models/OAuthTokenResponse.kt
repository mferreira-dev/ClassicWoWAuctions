package pt.mferreira.classicauctions.data.models

import com.google.gson.annotations.SerializedName

data class OAuthTokenResponse(
	@SerializedName("access_token") var accessToken: String,
	@SerializedName("token_type") var tokenType: String,
	@SerializedName("expires_in") var expiresIn: Int,
	var sub: String
)