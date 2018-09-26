package com.papyrus.mehdok.rejalalhadith.ui.main.tabghavaed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.database.RejalGhavaed

class GhavaedAdapter(private val ghavaed: MutableList<RejalGhavaed>) : RecyclerView.Adapter<GhavaedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rejal, parent, false)
        return GhavaedAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return ghavaed.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rejalTitle.text = ghavaed[position].title
    }

    public class ViewHolder : RecyclerView.ViewHolder {
        val rejalTitle: TextView

        constructor(itemView: View) : super(itemView) {
            rejalTitle = itemView.findViewById(R.id.rejalName)
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