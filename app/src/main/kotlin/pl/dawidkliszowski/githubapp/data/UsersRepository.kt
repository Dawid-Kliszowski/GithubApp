package pl.dawidkliszowski.githubapp.data

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pl.dawidkliszowski.githubapp.api.UsersApiService
import pl.dawidkliszowski.githubapp.model.api.SearchUsersResponse
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.mappers.UsersApiResponseMapper
import retrofit2.Response
import javax.inject.Inject

class UsersRepository @Inject constructor(
        private val usersApiService: UsersApiService,
        private val usersApiResponseMapper: UsersApiResponseMapper
) : BaseRemoteRepository<GithubUser, SearchUsersResponse>() {

    override fun queryItems(queryText: String, page: Int, perPage: Int): Single<Response<SearchUsersResponse>> {
        return usersApiService.queryUsers(queryText, page, perPage, UserSortOptions.JOINED.apiVal)
    }

    override fun mapToDomainModel(apiResponse: SearchUsersResponse): List<GithubUser> {
        return usersApiResponseMapper.mapApiResponseToDomainUsers(apiResponse)
    }

    fun fetchFollowersCount(fullUrl: String): Single<Int> {
        return usersApiService
                .getFollowers(fullUrl)
                .map{ response -> extractQueryResponse(response) }
                .map { it.size }
                .onErrorResumeNext { throwCustomException(it) }
                .subscribeOn(Schedulers.io())
    }
}

enum class UserSortOptions(val apiVal: String) {
    FOLLOWERS("followers"),
    REPOSITORIES("repositories"),
    JOINED("joined")
}