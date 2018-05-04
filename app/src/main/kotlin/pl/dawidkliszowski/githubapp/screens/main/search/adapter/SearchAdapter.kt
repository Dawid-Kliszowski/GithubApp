package pl.dawidkliszowski.githubapp.screens.main.search.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.model.ui.UserUiItem
import javax.inject.Inject

private const val VIEW_TYPE_USER = 1
private const val VIEW_TYPE_NEXT_PAGE_PROGRESS = 2

class UsersAdapter @Inject constructor(
        private val picasso: Picasso
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var userItems: List<UserUiItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onUserItemClickListener: ((userId: Long, avatarImageView: View, usernameTextView: View, scoreTextView: View) -> Unit)? = null
    var onScrollToLastNonProgressItem: (() -> Unit)? = null

    var isNextPageProgressVisible: Boolean = false
        set(value) {
            field = value
            currentRecyclerView?.post {
                notifyDataSetChanged()
            }
        }

    private var currentRecyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_USER -> {
                val view = inflater.inflate(R.layout.recycler_view_item_user, parent, false)
                UserViewHolder(view)
            }
            VIEW_TYPE_NEXT_PAGE_PROGRESS -> {
                val view = inflater.inflate(R.layout.recycler_view_item_progress, parent, false)
                ProgressViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun getItemCount(): Int {
        return if (isNextPageProgressVisible) userItems.size + 1 else userItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < userItems.size) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_NEXT_PAGE_PROGRESS
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_USER -> bindUserViewHolder(holder as UserViewHolder, position)
            VIEW_TYPE_NEXT_PAGE_PROGRESS -> Unit //no binding needed
        }
    }

    private fun bindUserViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.bindView(userItems[position], picasso, onUserItemClickListener) {
            if (isLast(position)) {
                onScrollToLastNonProgressItem?.invoke()
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        currentRecyclerView = recyclerView

    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        currentRecyclerView = null
        super.onDetachedFromRecyclerView(recyclerView)
    }

    private fun isLast(position: Int) = position == itemCount - 1
}