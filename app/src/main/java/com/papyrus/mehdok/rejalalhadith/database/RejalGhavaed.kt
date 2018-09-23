package com.papyrus.mehdok.rejalalhadith.database

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = RejalDatabase::class, name = "ghavaed")
class RejalGhavaed : BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    var _id: Int = 0

    @Column
    var title: String = ""

    @Column
    var text: String = ""

    constructor()

    constructor(_id: Int, title: String, text: String) {
        this._id = _id
        this.title = title
        this.text = text
    }
}