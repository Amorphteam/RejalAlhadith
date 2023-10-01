package org.masaha.rejalalhadith.database

import android.os.Parcel
import android.os.Parcelable
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = RejalDatabase::class, name = "ghavaed")
class RejalGhavaed : BaseModel, Parcelable {
    @Column
    @PrimaryKey(autoincrement = true)
    var _id: Int = 0

    @Column
    var title: String = ""

    @Column
    var text: String = ""

    @Column
    var joz: Int = 0

    @Column
    var page: Int = 0

    @Column
    var harf: String = ""

    constructor()

    constructor(_id: Int, title: String, text: String, joz: Int, page: Int, harf: String) {
        this._id = _id
        this.title = title
        this.text = text
        this.joz = joz
        this.page = page
        this.harf = harf
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(_id)
        parcel.writeString(title)
        parcel.writeString(text)
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

        other as RejalGhavaed

        if (_id != other._id) return false
        if (title != other.title) return false
        if (text != other.text) return false
        if (joz != other.joz) return false
        if (page != other.page) return false
        if (harf != other.harf) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id
        result = 31 * result + title.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + joz
        result = 31 * result + page
        result = 31 * result + harf.hashCode()
        return result
    }


    companion object CREATOR : Parcelable.Creator<RejalGhavaed> {
        override fun createFromParcel(parcel: Parcel): RejalGhavaed {
            return RejalGhavaed(parcel)
        }

        override fun newArray(size: Int): Array<RejalGhavaed?> {
            return arrayOfNulls(size)
        }
    }


}