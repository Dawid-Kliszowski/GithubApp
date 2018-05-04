package pl.dawidkliszowski.githubapp.model.api

import com.google.gson.annotations.SerializedName
import pl.dawidkliszowski.githubapp.model.ApiResponseModel

class SearchReposApiResponse(
        @SerializedName("total_count") val totalCount: Int,
        @SerializedName("incomplete_results") val incompleteResults: Boolean,
        @SerializedName("items") val items: List<SearchGuthubRepoItem>
) : ApiResponseModel

class SearchGuthubRepoItem(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
        @SerializedName("full_name") val fullName: String,
        @SerializedName("private") val private: Boolean,
        @SerializedName("html_url") val htmlUrl: String,
        @SerializedName("description") val description: String?,
        @SerializedName("fork") val fork: Boolean,
        @SerializedName("url") val url: String,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("updated_at") val updatedAt: String,
        @SerializedName("pushed_at") val pushedAt: String,
        @SerializedName("homepage") val homepage: String,
        @SerializedName("size") val size: Long,
        @SerializedName("stargazers_count") val stargazersCount: Long,
        @SerializedName("watchers_count") val watchersCount: Long,
        @SerializedName("language") val language: String,
        @SerializedName("forks_count") val forksCount: Long,
        @SerializedName("open_issues_count") val openIssuesCount: Long,
        @SerializedName("master_branch") val masterBranch: String,
        @SerializedName("default_branch") val defaultBranch: String,
        @SerializedName("score") val score: Double,
        @SerializedName("owner") val owner: GithubRepoOwner
)

class GithubRepoOwner(
        @SerializedName("id") val id: Long,
        @SerializedName("login") val login: String,
        @SerializedName("avatar_url") val avatarUrl: String,
        @SerializedName("gravatar_id") val gravatarUrl: String,
        @SerializedName("url") val url: String,
        @SerializedName("received_events_url") val receivedEventsUrl: String,
        @SerializedName("type") val typr: String
)