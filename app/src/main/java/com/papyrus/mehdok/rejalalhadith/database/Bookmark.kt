package com.papyrus.mehdok.rejalalhadith.database

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = BookmarkDatabase::class, name = "bookmark")
class Bookmark : BaseModel() {
    @Column
    @PrimaryKey(autoincrement = true)
    var _id: Int = 0

    @Column
    var bookmarkTitle: String = ""

    @Column
    var bookmarkText: String = ""

    @Column
    var bookmarkId: Int = 0 // id in rejal table
}