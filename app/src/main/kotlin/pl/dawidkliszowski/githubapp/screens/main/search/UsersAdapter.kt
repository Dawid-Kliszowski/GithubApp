package pl.dawidkliszowski.githubapp.screens.main.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view_item_user.view.*
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.model.ui.UserUiItem
import javax.inject.Inject

class UsersAdapter @Inject constructor(
        private val picasso: Picasso
) : RecyclerView.Adapter<UsersViewHolder>() {

    var userItems: List<UserUiItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onUserItemClickListener: ((Long) -> Unit)? = null
    var onPaginateListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_view_item_user, parent, false)
        return UsersViewHolder(view)
    }

    override fun getItemCount(): Int = userItems.size

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bindView(userItems[position], picasso, onUserItemClickListener)
    }

    fun showPaginateProgress() {
        //todo implement
    }

    fun hidePaginateProgress() {
        //todo implement
    }
}

class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val avatarImageView: ImageView = itemView.avatarImageView
    private val usernameTextView: TextView = itemView.usernameTextView
    private val ratingTextView: TextView = itemView.ratingTextView

    fun bindView(userItem: UserUiItem, picasso: Picasso, onUserItemClickListener: ((Long) -> Unit)?) {
        picasso.load(userItem.avatarUrl)
                .placeholder(R.drawable.user_avatar_placeholder)
                .into(avatarImageView)

        usernameTextView.text = userItem.login
        ratingTextView.text = userItem.score

        itemView.setOnClickListener {
            onUserItemClickListener?.invoke(userItem.id)
        }
    }
}