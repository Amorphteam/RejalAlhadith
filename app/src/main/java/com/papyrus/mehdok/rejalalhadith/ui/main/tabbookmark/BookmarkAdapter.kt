package com.papyrus.mehdok.rejalalhadith.ui.main.tabbookmark

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.database.Bookmark

class BookmarkAdapter(private val bookmarks: MutableList<Bookmark>) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rejal, parent, false)
        return BookmarkAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return bookmarks.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rejalTitle.text = bookmarks[position].bookmarkTitle
    }

    public class ViewHolder : RecyclerView.ViewHolder {
        val rejalTitle: TextView

        constructor(itemView: View) : super(itemView) {
            rejalTitle = itemView.findViewById(R.id.rejalName)
        }
    }

    fun addItems(items: List<Bookmark>) {
        this.bookmarks.addAll(items)
        notifyDataSetChanged()
    }

    fun removeAllItem() {
        this.bookmarks.clear()
        notifyDataSetChanged()
    }
}