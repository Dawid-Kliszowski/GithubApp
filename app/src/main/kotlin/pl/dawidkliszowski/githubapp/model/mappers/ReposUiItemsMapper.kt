package pl.dawidkliszowski.githubapp.model.mappers

import pl.dawidkliszowski.githubapp.model.domain.GithubRepo
import pl.dawidkliszowski.githubapp.model.ui.SearchUiItem
import javax.inject.Inject

class ReposUiItemsMapper @Inject constructor() {

    fun mapToUiItems(domainRepos: List<GithubRepo>): List<SearchUiItem.RepoUiItem> {
        return domainRepos.map { githubRepo ->
            with (githubRepo) {
                SearchUiItem.RepoUiItem(id, name, description, ownerAvatarUrl, ownerName)
            }
        }
    }
}