package pl.dawidkliszowski.githubapp.data.api.model

import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(
        @SerializedName("total_count") val totalCount: Int,
        @SerializedName("incomplete_results") val incompleteResults: Boolean,
        @SerializedName("items") val users: List<SearchUsersItem>
) : ApiResponseModel

data class SearchUsersItem(
        @SerializedName("login") val login: String,
        @SerializedName("id") val id: Long,
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("gravatar_id") val gravatarId: String,
        @SerializedName("url") val url: String,
        @SerializedName("html_url") val htmlUrl: String,
        @SerializedName("followers_url") val followersUrl: String,
        @SerializedName("subscriptions_url") val subscriptionsUrl: String,
        @SerializedName("organizations_url") val organisationsUrl: String,
        @SerializedName("repos_url") val reposUrl: String,
        @SerializedName("received_events_url") val receivedEventsUrl: String,
        @SerializedName("type") val type: String,
        @SerializedName("score") val score: Double
)