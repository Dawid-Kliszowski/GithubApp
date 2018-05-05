package pl.dawidkliszowski.githubapp.screens.main.search.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.screens.model.ui.SearchUiItem
import javax.inject.Inject

private const val VIEW_TYPE_USER = 1
private const val VIEW_TYPE_REPO = 2
private const val VIEW_TYPE_NEXT_PAGE_PROGRESS = 3

class UsersAdapter @Inject constructor(
        private val picasso: Picasso
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<SearchUiItem> = emptyList()

    var onUserItemClickListener: ((userId: Long, avatarImageView: View, usernameTextView: View, scoreTextView: View) -> Unit)? = null
    var onScrollToLastNonProgressItem: (() -> Unit)? = null

    var isNextPageProgressVisible: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_USER -> createUserViewHolder(inflater, parent)
            VIEW_TYPE_REPO -> createRepoViewHolder(inflater, parent)
            VIEW_TYPE_NEXT_PAGE_PROGRESS -> createProgressViewHolder(inflater, parent)
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    private fun createProgressViewHolder(inflater: LayoutInflater, parent: ViewGroup): ProgressViewHolder {
        val view = inflater.inflate(R.layout.recycler_view_item_progress, parent, false)
        return ProgressViewHolder(view)
    }

    private fun createUserViewHolder(inflater: LayoutInflater, parent: ViewGroup): UserViewHolder {
        val view = inflater.inflate(R.layout.recycler_view_item_user, parent, false)
        return UserViewHolder(view)
    }

    private fun createRepoViewHolder(inflater: LayoutInflater, parent: ViewGroup): RepoViewHolder {
        val view = inflater.inflate(R.layout.recycler_view_item_repo, parent, false)
        return RepoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (isNextPageProgressVisible) items.size + 1 else items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= items.size) {
            VIEW_TYPE_NEXT_PAGE_PROGRESS
        } else {
            when (items[position]) {
                is SearchUiItem.UserUiItem -> VIEW_TYPE_USER
                is SearchUiItem.RepoUiItem -> VIEW_TYPE_REPO
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_USER -> bindUserViewHolder(holder as UserViewHolder, position)
            VIEW_TYPE_REPO -> bindRepoViewHolder(holder as RepoViewHolder, position)
            VIEW_TYPE_NEXT_PAGE_PROGRESS -> Unit //no binding needed
        }
    }

    private fun bindUserViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.bindView(items[position] as SearchUiItem.UserUiItem, picasso, onUserItemClickListener) {
            if (isLast(position)) {
                onScrollToLastNonProgressItem?.invoke()
            }
        }
    }

    private fun bindRepoViewHolder(repoViewHolder: RepoViewHolder, position: Int) {
        repoViewHolder.bindView(items[position] as SearchUiItem.RepoUiItem, picasso) {
            if (isLast(position)) {
                onScrollToLastNonProgressItem?.invoke()
            }
        }
    }

    private fun isLast(position: Int) = position == itemCount - 1
}