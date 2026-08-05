package org.masaha.rejalalhadith.ui.main.tabrejal

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.papyrus.mehdok.rejalalhadith.R
import org.masaha.rejalalhadith.database.RejalLink
import org.masaha.rejalalhadith.ui.main.OnTabItemClickListener
import org.masaha.rejalalhadith.utils.SearchMode

class RejalAdapter(private val rejals: MutableList<RejalLink>, private var listener: OnTabItemClickListener?) : RecyclerView.Adapter<RejalAdapter.ViewHolder>() {
    var searchQuery = ""
    var searchMode: SearchMode = SearchMode.NAME_CONTAINS

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_rejal, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return rejals.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rejal = rejals[position]
        val highlightColor = holder.itemView.context.resources.getColor(R.color.search_highlight)

        if (searchMode == SearchMode.DESCRIPTION && searchQuery.isNotEmpty()) {
            holder.rejalTitle.text = rejal.name
            val snippet = buildDescriptionSnippet(rejal.det, searchQuery)
            holder.rejalDescription.text = highlightText(snippet, searchQuery, highlightColor)
            holder.rejalDescription.visibility = View.VISIBLE
        } else {
            holder.rejalTitle.text = rejal.name
            holder.rejalDescription.text = ""
            holder.rejalDescription.visibility = View.GONE
        }
    }

    inner class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        val rejalTitle: TextView
        val rejalDescription: TextView

        constructor(itemView: View) : super(itemView) {
            itemView.setOnClickListener(this)
            rejalTitle = itemView.findViewById(R.id.rejalName)
            rejalDescription = itemView.findViewById(R.id.rejalDescription)
        }

        override fun onClick(p0: View?) {
            listener?.onItemClicked(rejals[adapterPosition], searchQuery, searchMode)
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

    companion object {
        private const val SNIPPET_RADIUS = 60

        fun highlightText(text: String, query: String, highlightColor: Int): CharSequence {
            if (query.isEmpty() || text.isEmpty()) {
                return text
            }

            val spannable = SpannableString(text)
            var start = text.indexOf(query, ignoreCase = true)
            while (start >= 0) {
                val end = start + query.length
                spannable.setSpan(
                        BackgroundColorSpan(highlightColor),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                        StyleSpan(Typeface.BOLD),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                start = text.indexOf(query, end, ignoreCase = true)
            }
            return spannable
        }

        fun buildDescriptionSnippet(description: String, query: String): String {
            val plain = description
                    .replace("&اختلاف النسخ&", " اختلاف النسخ ")
                    .replace("&اختلاف الكتب&", " اختلاف الكتب ")
                    .replace("&طبقته في الحديث&", " طبقته في الحديث ")
                    .replace("&", " ")
                    .replace(Regex("\\s+"), " ")
                    .trim()

            if (plain.isEmpty()) {
                return ""
            }

            val matchIndex = plain.indexOf(query, ignoreCase = true)
            if (matchIndex < 0) {
                return if (plain.length <= SNIPPET_RADIUS * 2) {
                    plain
                } else {
                    plain.take(SNIPPET_RADIUS * 2) + "…"
                }
            }

            val start = maxOf(0, matchIndex - SNIPPET_RADIUS)
            val end = minOf(plain.length, matchIndex + query.length + SNIPPET_RADIUS)
            val prefix = if (start > 0) "…" else ""
            val suffix = if (end < plain.length) "…" else ""
            return prefix + plain.substring(start, end).trim() + suffix
        }
    }
}
