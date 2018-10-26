package org.masaha.rejalalhadith.ui.main.tabrejal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.papyrus.mehdok.rejalalhadith.R
import org.masaha.rejalalhadith.database.RejalLink
import org.masaha.rejalalhadith.ui.main.OnTabItemClickListener

class RejalAdapter(private val rejals: MutableList<RejalLink>, private var listener: OnTabItemClickListener?) : RecyclerView.Adapter<RejalAdapter.ViewHolder>() {
    public var searchQuery = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rejal, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return rejals.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rejalTitle.text = rejals[position].name
    }

    inner class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        val rejalTitle: TextView

        constructor(itemView: View) : super(itemView) {
            itemView.setOnClickListener(this)
            rejalTitle = itemView.findViewById(R.id.rejalName)
        }

        override fun onClick(p0: View?) {
            listener?.onItemClicked(rejals[adapterPosition], searchQuery)
        }
    }

    fun addItems(items: List<RejalLink>) {
        this.rejals.addAll(items)
        notifyDataSetChanged()
    }

    fun removeAllItem() {
        this.rejals.clear()
        notifyDataSetChanged()
    }
}