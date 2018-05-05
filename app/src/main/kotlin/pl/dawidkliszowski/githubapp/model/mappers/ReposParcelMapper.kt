package pl.dawidkliszowski.githubapp.model.mappers

import pl.dawidkliszowski.githubapp.model.domain.GithubRepo
import pl.dawidkliszowski.githubapp.model.parcel.GithubRepoParcel
import javax.inject.Inject

class ReposParcelMapper @Inject constructor() {

    fun toParcel(repo: GithubRepo): GithubRepoParcel{
        return with (repo) {
            GithubRepoParcel(id, name, description, ownerAvatarUrl, ownerName)
        }
    }

    fun fromParcel(repoParcel: GithubRepoParcel): GithubRepo {
        return with (repoParcel) {
            GithubRepo(id, name, description, ownerAvatarUrl, ownerName)
        }
    }
}