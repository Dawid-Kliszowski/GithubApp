package pl.dawidkliszowski.githubapp.data

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import pl.dawidkliszowski.githubapp.model.ApiResponseModel
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection

private const val REQUEST_ITEMS_PER_PAGE = 30
private const val API_PAGE_OFFSET = 1 //API pages start from 1 instead of 0

abstract class BaseRemoteRepository<T, A: ApiResponseModel> {

    protected abstract fun queryItems(queryText: String, page: Int, perPage: Int): Single<Response<A>>

    protected abstract fun mapToDomainModel(apiResponse: A): List<T>

    fun query(query: String, fromItem: Int): Single<List<T>> {
        return if (query.isBlank() || !canLoadNextPage(fromItem)) {
            Single.just(emptyList())
        } else {
            val page = calculateNextPage(fromItem)
            queryItems(query, page, REQUEST_ITEMS_PER_PAGE)
                    .map(::extractQueryResponse)
                    .map(::mapToDomainModel)
                    .onErrorResumeNext { throwCustomException(it) }
                    .subscribeOn(Schedulers.io())
        }
    }

    private fun extractQueryResponse(response: Response<A>): A {
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            throw when (response.code()) {
                HttpURLConnection.HTTP_FORBIDDEN -> RemoteRepositoryLimitsReachedException()
                else -> UnknownRemoteRepositoryException()
            }
        }
    }

    private fun <T> throwCustomException(throwable: Throwable): Single<T> {
        return if (throwable.isNetworkException) {
            Single.error(RemoteRepositoryUnavailableException())
        } else {
            Single.error(throwable)
        }
    }

    private val Throwable.isNetworkException
        get() = this is IOException


    private fun canLoadNextPage(fromItem: Int): Boolean {
        return (fromItem % REQUEST_ITEMS_PER_PAGE) == 0
    }

    private fun calculateNextPage(fromItem: Int): Int {
        return (fromItem / REQUEST_ITEMS_PER_PAGE) + API_PAGE_OFFSET
    }
}