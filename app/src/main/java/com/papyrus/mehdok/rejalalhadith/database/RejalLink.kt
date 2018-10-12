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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RejalLink

        if (ID != other.ID) return false
        if (name != other.name) return false
        if (name2 != other.name2) return false
        if (det != other.det) return false
        if (joz != other.joz) return false
        if (page != other.page) return false
        if (harf != other.harf) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ID
        result = 31 * result + name.hashCode()
        result = 31 * result + name2.hashCode()
        result = 31 * result + det.hashCode()
        result = 31 * result + joz
        result = 31 * result + page
        result = 31 * result + harf.hashCode()
        return result
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