package com.papyrus.mehdok.rejalalhadith.database

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = BookmarkDatabase.NAME, version = BookmarkDatabase.VERSION, backupEnabled = true, consistencyCheckEnabled = true)
class BookmarkDatabase {
    companion object {
        const val NAME = "Bookmark"
        const val VERSION = 1
    }
}