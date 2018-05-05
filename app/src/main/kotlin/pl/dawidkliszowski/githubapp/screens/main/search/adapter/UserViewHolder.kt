package pl.dawidkliszowski.githubapp.screens.main.search.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_view_item_user.view.*
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.screens.model.ui.SearchUiItem

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val avatarImageView: ImageView = itemView.avatarImageView
    private val usernameTextView: TextView = itemView.usernameTextView
    private val scoreTextView: TextView = itemView.scoreTextView

    fun bindView(
            userItem: SearchUiItem.UserUiItem,
            picasso: Picasso,
            onUserItemClickListener: ((userId: Long, avatarImageView: View, usernameTextView: View, scoreTextView: View) -> Unit)?,
            onAttachListener: () -> Unit
    ) {
        picasso.load(userItem.avatarUrl)
                .placeholder(R.drawable.user_avatar_placeholder)
                .into(avatarImageView)

        usernameTextView.text = userItem.login
        scoreTextView.text = userItem.score

        itemView.setOnClickListener {
            onUserItemClickListener?.invoke(userItem.id, avatarImageView, usernameTextView, scoreTextView)
        }

        addOnAttachListener(onAttachListener)
    }

    private fun addOnAttachListener(onViewAttached: () -> Unit) {
        if (itemView.tag != null) {
            itemView.removeOnAttachStateChangeListener(itemView.tag as View.OnAttachStateChangeListener)
        }

        val onAttachListener = object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(view: View?) = Unit
            override fun onViewAttachedToWindow(view: View?) = onViewAttached.invoke()
        }

        itemView.addOnAttachStateChangeListener(onAttachListener)
        itemView.tag = onAttachListener
    }
}