package org.masaha.rejalalhadith.ui.main.tabbookmark

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.papyrus.mehdok.rejalalhadith.R
import org.masaha.rejalalhadith.database.Bookmark
import org.masaha.rejalalhadith.ui.main.OnTabItemClickListener

class BookmarkAdapter(private val bookmarks: MutableList<Bookmark>, private var listener: OnTabItemClickListener?) : RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark, parent, false)
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
        val delete: ImageButton

        constructor(itemView: View) : super(itemView) {
            itemView.setOnClickListener(this)
            rejalTitle = itemView.findViewById(R.id.rejalName)
            delete = itemView.findViewById(R.id.deleteBtn)
            delete.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if (p0?.id == R.id.deleteBtn) {
                listener?.onDeleteBookmark(bookmarks[adapterPosition])
                bookmarks.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
                return
            }

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