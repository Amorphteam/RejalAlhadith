package com.papyrus.mehdok.rejalalhadith.database

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = RejalDatabase::class, name = "rejal")
class RejalLink : BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    var ID: Int = 0

    @Column
    var name: String = ""

    @Column
    var det: String = ""

    @Column
    var joz: Int = 0

    @Column
    var page: Int = 0

    @Column
    var harf: String = ""

    constructor() {

    }

    constructor(id: Int, name: String, det: String, joz: Int, page: Int, harf: String) {
        this.ID = id
        this.name = name
        this.det = det
        this.joz = joz
        this.page = page
        this.harf = harf
    }
}