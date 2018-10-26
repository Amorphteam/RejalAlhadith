package org.masaha.rejalalhadith.database

import android.os.Parcel
import android.os.Parcelable
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = BookmarkDatabase::class, name = "bookmark")
class Bookmark : BaseModel, Parcelable {
    @Column
    @PrimaryKey(autoincrement = true)
    var _id: Int = 0

    @Column
    var bookmarkTitle: String = ""

    @Column
    var bookmarkText: String = ""

    @Column
    var bookmarkId: Int = 0 // id in rejal table

    @Column
    var joz: Int = 0

    @Column
    var page: Int = 0

    @Column
    var harf: String = ""

    constructor()

    constructor(_id: Int, bookmarkTitle: String, bookmarkText: String, bookmarkId: Int, joz: Int, page: Int, harf: String) : super() {
        this._id = _id
        this.bookmarkTitle = bookmarkTitle
        this.bookmarkText = bookmarkText
        this.bookmarkId = bookmarkId
        this.joz = joz
        this.page = page
        this.harf = harf
    }

    constructor(bookmarkTitle: String, bookmarkText: String, bookmarkId: Int, joz: Int, page: Int, harf: String) : super() {
        this.bookmarkTitle = bookmarkTitle
        this.bookmarkText = bookmarkText
        this.bookmarkId = bookmarkId
        this.joz = joz
        this.page = page
        this.harf = harf
    }

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(_id)
        parcel.writeString(bookmarkTitle)
        parcel.writeString(bookmarkText)
        parcel.writeInt(bookmarkId)
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

        other as Bookmark

        if (_id != other._id) return false
        if (bookmarkTitle != other.bookmarkTitle) return false
        if (bookmarkText != other.bookmarkText) return false
        if (bookmarkId != other.bookmarkId) return false
        if (joz != other.joz) return false
        if (page != other.page) return false
        if (harf != other.harf) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id
        result = 31 * result + bookmarkTitle.hashCode()
        result = 31 * result + bookmarkText.hashCode()
        result = 31 * result + bookmarkId
        result = 31 * result + joz
        result = 31 * result + page
        result = 31 * result + harf.hashCode()
        return result
    }


    companion object CREATOR : Parcelable.Creator<Bookmark> {
        override fun createFromParcel(parcel: Parcel): Bookmark {
            return Bookmark(parcel)
        }

        override fun newArray(size: Int): Array<Bookmark?> {
            return arrayOfNulls(size)
        }
    }


}