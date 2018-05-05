package pl.dawidkliszowski.githubapp.screens.model.mappers

import pl.dawidkliszowski.githubapp.data.model.GithubRepo
import pl.dawidkliszowski.githubapp.screens.model.ui.SearchUiItem
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