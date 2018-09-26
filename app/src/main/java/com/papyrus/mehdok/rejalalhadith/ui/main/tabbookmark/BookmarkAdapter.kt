package com.papyrus.mehdok.rejalalhadith.ui.main.tabbookmark

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.database.Bookmark
import com.papyrus.mehdok.rejalalhadith.ui.main.OnTabItemClickListener

class BookmarkAdapter(private val bookmarks: MutableList<Bookmark>, private var listener: OnTabItemClickListener?) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rejal, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return bookmarks.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rejalTitle.text = bookmarks[position].bookmarkTitle
    }

    inner class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        val rejalTitle: TextView

        constructor(itemView: View) : super(itemView) {
            itemView.setOnClickListener(this)
            rejalTitle = itemView.findViewById(R.id.rejalName)
        }

        override fun onClick(p0: View?) {
            listener?.onItemClicked(bookmarks[adapterPosition])
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