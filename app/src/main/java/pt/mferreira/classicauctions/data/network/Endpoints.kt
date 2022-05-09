package pt.mferreira.classicauctions.data.network

import pt.mferreira.classicauctions.data.models.*
import retrofit2.http.*

interface Endpoints {
	@FormUrlEncoded
	@POST("oauth/token")
	suspend fun getOAuthToken(
		@Field("grant_type") grantType: String = "client_credentials"
	): OAuthTokenResponse

	@GET
	suspend fun getRealmIndex(
		@Url url: String,
		@Query("namespace") namespace: String,
	): RealmIndexResponse

	@GET
	suspend fun getRealmDetails(
		@Url url: String,
		@Query("namespace") namespace: String
	): RealmDetailsResponse

	@GET
	suspend fun getAuctions(
		@Url url: String,
		@Query("namespace") namespace: String
	): AuctionsResponse

	@GET("item-class/index")
	suspend fun getItemCategories(
		@Query("namespace") namespace: String
	): ItemCategoriesResponse

	@GET("item-class/{categoryId}")
	suspend fun getItemSubCategories(
		@Path("categoryId") categoryId: Int,
		@Query("namespace") namespace: String
	): ItemSubCategoriesResponse

	@GET("search/item")
	suspend fun itemSearch(
		@Query("name.en_US") itemName: String,
		@Query("_pageSize") pageSize: Int,
		@Query("namespace") namespace: String
	): ItemSearchResponse

	@GET("media/item/{itemId}")
	suspend fun getItemMedia(
		@Path("itemId") itemId: Int,
		@Query("namespace") namespace: String
	): ItemMediaResponse

	@GET("playable-class/index")
	suspend fun getClassIndex(
		@Query("namespace") namespace: String
	): ClassIndexResponse

	@GET("media/playable-class/{classId}")
	suspend fun getClassMedia(
		@Path("classId") classId: Int,
		@Query("namespace") namespace: String
	): ClassMediaResponse

	@GET("playable-race/index")
	suspend fun getRaceIndex(
		@Query("namespace") namespace: String
	): RaceIndexResponse

	@GET
	suspend fun getPvPRegionIndex(
		@Url url: String,
		@Query("namespace") namespace: String
	): PvPRegionIndexResponse

	@GET
	suspend fun getPvPRegionSeasonIndex(
		@Url url: String,
		@Query("namespace") namespace: String
	): PvPRegionSeasonIndexResponse

	@GET
	suspend fun getPvPRegionSeasonDetails(
		@Url url: String,
		@Query("namespace") namespace: String
	): PvPRegionSeasonDetailsResponse

	@GET
	suspend fun getLeaderboardIndex(
		@Url url: String,
		@Query("namespace") namespace: String
	): LeaderboardIndexResponse

	@GET
	suspend fun getLeaderboardDetails(
		@Url url: String,
		@Query("namespace") namespace: String
	): LeaderboardDetailsResponse

	@GET
	suspend fun getRewardsIndex(
		@Url url: String,
		@Query("namespace") namespace: String
	): RewardsIndexResponse
}