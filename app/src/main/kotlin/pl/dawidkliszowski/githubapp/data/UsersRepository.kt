package pl.dawidkliszowski.githubapp.data

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pl.dawidkliszowski.githubapp.api.GithubApiService
import pl.dawidkliszowski.githubapp.model.domain.GithubUser
import pl.dawidkliszowski.githubapp.model.mappers.UsersApiResponseMapper
import java.io.IOException
import javax.inject.Inject

private const val USERS_REQUEST_ITEMS_PER_PAGE = 30
private const val API_PAGE_OFFSET = 1 //API pages start from 1 instead of 0

class UsersRepository @Inject constructor(
        private val githubApiService: GithubApiService,
        private val usersApiResponseMapper: UsersApiResponseMapper
) {

    fun searchUsers(query: String, fromItem: Int): Single<List<GithubUser>> {
        return if (query.isBlank() || !canLoadNextPage(fromItem)) {
            Single.just(emptyList())
        } else {
            val page = calculateNextPage(fromItem)
            githubApiService.getUsers(query, page, USERS_REQUEST_ITEMS_PER_PAGE)
                    .map(usersApiResponseMapper::mapApiResponseToDomainUsers)
                    .onErrorResumeNext { throwCustomException(it) }
                    .subscribeOn(Schedulers.io())
        }
    }

    private fun <T> throwCustomException(throwable: Throwable): T {
        if (throwable.isNetworkException) {
            throw RemoteRepositoryUnavailableException()
        } else {
            throw throwable
        }
    }

    private val Throwable.isNetworkException
        get() = this is IOException


    private fun canLoadNextPage(fromItem: Int): Boolean {
        return (fromItem % USERS_REQUEST_ITEMS_PER_PAGE) == 0
    }

    private fun calculateNextPage(fromItem: Int): Int {
        return (fromItem / USERS_REQUEST_ITEMS_PER_PAGE) + API_PAGE_OFFSET
    }
}