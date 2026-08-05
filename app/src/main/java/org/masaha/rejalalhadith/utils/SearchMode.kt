package org.masaha.rejalalhadith.utils

enum class SearchMode {
    NAME_STARTS_WITH,
    NAME_CONTAINS,
    DESCRIPTION;

    companion object {
        fun fromOrdinal(ordinal: Int): SearchMode {
            return values().getOrElse(ordinal) { NAME_CONTAINS }
        }
    }
}
