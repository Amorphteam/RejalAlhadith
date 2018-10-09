package com.papyrus.mehdok.rejalalhadith.database

import android.os.Parcel
import android.os.Parcelable
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = RejalDatabase::class, name = "rejal")
class RejalLink : BaseModel, Parcelable {
    @Column
    @PrimaryKey(autoincrement = true)
    var ID: Int = 0

    @Column
    var name: String = ""

    @Column
    var name2: String = ""

    @Column
    var det: String = ""

    @Column
    var joz: Int = 0

    @Column
    var page: Int = 0

    @Column
    var harf: String = ""

    constructor()

    constructor(id: Int, name: String, name2: String, det: String, joz: Int, page: Int, harf: String) {
        this.ID = id
        this.name = name
        this.name2 = name2
        this.det = det
        this.joz = joz
        this.page = page
        this.harf = harf
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ID)
        parcel.writeString(name)
        parcel.writeString(name2)
        parcel.writeString(det)
        parcel.writeInt(joz)
        parcel.writeInt(page)
        parcel.writeString(harf)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RejalLink> {
        override fun createFromParcel(parcel: Parcel): RejalLink {
            return RejalLink(parcel)
        }

        override fun newArray(size: Int): Array<RejalLink?> {
            return arrayOfNulls(size)
        }
    }
}