package org.masaha.rejalalhadith.utils

import android.content.Context
import org.json.JSONObject

class RejalHierarchy private constructor(
        private val childrenByParent: Map<Int, List<Int>>,
        private val parentsByChild: Map<Int, List<Int>>
) {
    fun childrenOf(id: Int): List<Int> = childrenByParent[id].orEmpty()

    fun parentsOf(id: Int): List<Int> = parentsByChild[id].orEmpty()

    companion object {
        @Volatile
        private var instance: RejalHierarchy? = null

        fun get(context: Context): RejalHierarchy {
            instance?.let { return it }
            return synchronized(this) {
                instance ?: load(context.applicationContext).also { instance = it }
            }
        }

        private fun load(context: Context): RejalHierarchy {
            val json = context.assets.open("maplist.json").bufferedReader().use { it.readText() }
            val records = JSONObject(json)
                    .getJSONObject("data")
                    .getJSONArray("records")
            val childrenByParent = linkedMapOf<Int, MutableSet<Int>>()
            val parentsByChild = linkedMapOf<Int, MutableSet<Int>>()

            for (recordIndex in 0 until records.length()) {
                val record = records.getJSONObject(recordIndex)
                val parentId = record.getInt("parent_id")
                val children = record.getJSONArray("childs")
                val parentChildren = childrenByParent.getOrPut(parentId) { linkedSetOf() }

                for (childIndex in 0 until children.length()) {
                    val childId = children.getInt(childIndex)
                    parentChildren.add(childId)
                    parentsByChild.getOrPut(childId) { linkedSetOf() }.add(parentId)
                }
            }

            return RejalHierarchy(
                    childrenByParent.mapValues { it.value.toList() },
                    parentsByChild.mapValues { it.value.toList() }
            )
        }
    }
}
