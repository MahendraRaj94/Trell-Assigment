package com.mahendra.trellsample.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class VideoItem(
    val id : Long,
    val contentUri: Uri?,
    val name: String?,
    val duration: Int,
    val size: Int,
    val fav: Boolean
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeParcelable(contentUri, flags)
        parcel.writeString(name)
        parcel.writeInt(duration)
        parcel.writeInt(size)
        parcel.writeByte(if (fav) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoItem> {
        override fun createFromParcel(parcel: Parcel): VideoItem {
            return VideoItem(parcel)
        }

        override fun newArray(size: Int): Array<VideoItem?> {
            return arrayOfNulls(size)
        }
    }

}