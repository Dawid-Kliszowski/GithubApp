package pl.dawidkliszowski.githubapp.api

import pl.dawidkliszowski.githubapp.model.api.SearchUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/users")
    fun getUsers(
            @Query("q") searchQuery: String
    ): SearchUserResponse
}