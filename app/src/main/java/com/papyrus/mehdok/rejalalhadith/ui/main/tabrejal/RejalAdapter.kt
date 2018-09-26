package com.papyrus.mehdok.rejalalhadith.ui.main.tabrejal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.papyrus.mehdok.rejalalhadith.R
import com.papyrus.mehdok.rejalalhadith.database.RejalLink

class RejalAdapter(private val rejals: MutableList<RejalLink>) : RecyclerView.Adapter<RejalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rejal, parent, false)
        return RejalAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return rejals.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rejalTitle.text = rejals[position].name
    }

    public class ViewHolder : RecyclerView.ViewHolder {
        val rejalTitle: TextView

        constructor(itemView: View) : super(itemView) {
            rejalTitle = itemView.findViewById(R.id.rejalName)
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