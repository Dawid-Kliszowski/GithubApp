package pl.dawidkliszowski.githubapp.screens.main.search.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_item_repo.view.*
import pl.dawidkliszowski.githubapp.R
import pl.dawidkliszowski.githubapp.model.ui.SearchUiItem

class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val avatarImageView: ImageView = itemView.avatarImageView
    private val repoNameTextView: TextView = itemView.repoNameTextView
    private val descriptionTextView: TextView = itemView.descriptionTextView
    private val ownerNameTextView: TextView = itemView.ownerNameTextView

    fun bindView(
            repoItem: SearchUiItem.RepoUiItem,
            picasso: Picasso,
            onAttachListener: () -> Unit
    ) {
        picasso.load(repoItem.ownerAvatarUrl)
                .placeholder(R.drawable.user_avatar_placeholder)
                .into(avatarImageView)

        repoNameTextView.text = repoItem.name
        descriptionTextView.text = repoItem.description
        ownerNameTextView.text = repoItem.ownerName

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