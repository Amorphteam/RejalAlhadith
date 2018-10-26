package org.masaha.rejalalhadith.ui.main.tabghavaed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.papyrus.mehdok.rejalalhadith.R
import org.masaha.rejalalhadith.database.RejalGhavaed
import org.masaha.rejalalhadith.ui.main.OnTabItemClickListener

class GhavaedAdapter(private val ghavaed: MutableList<RejalGhavaed>, private var listener: OnTabItemClickListener?) : RecyclerView.Adapter<GhavaedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rejal, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return ghavaed.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rejalTitle.text = ghavaed[position].title
    }

    inner class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        val rejalTitle: TextView

        constructor(itemView: View) : super(itemView) {
            itemView.setOnClickListener(this)
            rejalTitle = itemView.findViewById(R.id.rejalName)
        }

        override fun onClick(p0: View?) {
            listener?.onItemClicked(ghavaed[adapterPosition])
        }
    }

    fun addItems(items: List<RejalGhavaed>) {
        this.ghavaed.addAll(items)
        notifyDataSetChanged()
    }

    fun removeAllItem() {
        this.ghavaed.clear()
        notifyDataSetChanged()
    }
}