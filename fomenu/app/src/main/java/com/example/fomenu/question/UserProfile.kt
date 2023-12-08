package com.example.fomenu.question

import android.os.Parcel
import android.os.Parcelable


data class UserProfile(
    var weight: String,
    var height: String,
    var age: String,
    var sex: String,
    var bodyweight_goal: String,
    var fitness_goal: String,
    var skills: List<String>,
    var maxPull: Int,
    var maxPush: Int,
    var daysToTrain: Int

) : Parcelable {

    constructor() : this(
        "", "", "", "", "", "", emptyList(), 0, 0, 0
    )

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(weight)
        parcel.writeString(height)
        parcel.writeString(age)
        parcel.writeString(sex)
        parcel.writeString(bodyweight_goal)
        parcel.writeString(fitness_goal)
        parcel.writeStringList(skills)
        parcel.writeInt(maxPull)
        parcel.writeInt(maxPush)
        parcel.writeInt(daysToTrain)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserProfile> {
        override fun createFromParcel(parcel: Parcel): UserProfile {
            return UserProfile(parcel)
        }

        override fun newArray(size: Int): Array<UserProfile?> {
            return arrayOfNulls(size)
        }
    }

}

