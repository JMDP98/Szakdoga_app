package com.example.fomenu

import android.os.Parcel
import android.os.Parcelable
import com.example.fomenu.question.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.FieldPosition

data class Workout (
    val name: String,
    val difficulty: String,
    val type: String,
    val rounds: Int,
    val rest: String,
    val position: Int,
    val exercises: List<Exercise>,
    val idetifier: Int
    ) : Parcelable {
        constructor(parcel: Parcel) :this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt(),
            parcel.createTypedArrayList(Exercise)!!,
            parcel.readInt()
        )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(difficulty)
        parcel.writeString(type)
        parcel.writeInt(rounds)
        parcel.writeString(rest)
        parcel.writeInt(position)
        parcel.writeTypedList(exercises)
        parcel.writeInt(idetifier)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Workout> {
        override fun createFromParcel(parcel: Parcel): Workout {
            return Workout(parcel)
        }
        override fun newArray(size: Int): Array<Workout?> {
            return arrayOfNulls(size)
        }
    }
}


    data class Exercise(
        val name: String,
        var repetitionCount: String,
        val picturePath: String,
        val videoPath: String
    ) : Parcelable{
        constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!
        )
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(repetitionCount)
            parcel.writeString(picturePath)
            parcel.writeString(videoPath)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Exercise> {
            override fun createFromParcel(parcel: Parcel): Exercise {
                return Exercise(parcel)
            }

            override fun newArray(size: Int): Array<Exercise?> {
                return arrayOfNulls(size)
            }
        }
    }

fun generateWorkoutPlan(userProfile: UserProfile): List<Workout> {

    val workoutPlansDays3 = listOf(
        Beginner_Full_Body_workout1, Beginner_Full_Body_workout2,Beginner_Full_Body_workout3, Beginner_Full_Body_workout4, Beginner_Full_Body_workout5,
        Beginner_Full_Body_workout6,Beginner_Full_Body_workout7,Beginner_Full_Body_workout8,Beginner_Full_Body_workout9,Beginner_Full_Body_workout10,
        Beginner_Full_Body_workout11,Beginner_Full_Body_workout12,Beginner_Full_Body_workout13,Beginner_Full_Body_workout14,Beginner_Full_Body_workout15,
        Beginner_Full_Body_workout16,Beginner_Full_Body_workout17,Beginner_Full_Body_workout18,Beginner_Full_Body_workout19,Beginner_Full_Body_workout20,

                Inter_Full_Body_workout1, Inter_Full_Body_workout2,Inter_Full_Body_workout3, Inter_Full_Body_workout4, Inter_Full_Body_workout5,
        Inter_Full_Body_workout6,Inter_Full_Body_workout7,Inter_Full_Body_workout8,Inter_Full_Body_workout9,Inter_Full_Body_workout10,
        Inter_Full_Body_workout11,Inter_Full_Body_workout12,Inter_Full_Body_workout13,Inter_Full_Body_workout14,Inter_Full_Body_workout15,
        Inter_Full_Body_workout16,Inter_Full_Body_workout17,Inter_Full_Body_workout18,Inter_Full_Body_workout19,Inter_Full_Body_workout20,

            Advanced_Full_Body_workout1, Advanced_Full_Body_workout2,Advanced_Full_Body_workout3, Advanced_Full_Body_workout4, Advanced_Full_Body_workout5,
        Advanced_Full_Body_workout6,Advanced_Full_Body_workout7,Advanced_Full_Body_workout8,Advanced_Full_Body_workout9,Advanced_Full_Body_workout10,


    )
    val workoutPlansDays4 = listOf(
        Beginner_Full_Body_workout1, Beginner_Full_Body_workout2,Beginner_Full_Body_workout3, Beginner_Full_Body_workout4, Beginner_Full_Body_workout5,
        Beginner_Full_Body_workout6,Beginner_Full_Body_workout7,Beginner_Full_Body_workout8,Beginner_Full_Body_workout9,Beginner_Full_Body_workout10, Beginner_Full_Body_workout11,

        Beginner_Push_workout1, Beginner_Pull_workout1, Beginner_Leg_workout1, Beginner_Ab_workout1,
        Beginner_Push_workout2, Beginner_Pull_workout2, Beginner_Leg_workout1, Beginner_Ab_workout1,
        Beginner_Push_workout3, Beginner_Pull_workout3, Beginner_Leg_workout2, Beginner_Ab_workout2,
        Beginner_Push_workout4, Beginner_Pull_workout4, Beginner_Leg_workout2, Beginner_Ab_workout2,
        Beginner_Push_workout5, Beginner_Pull_workout5, Beginner_Leg_workout3, Beginner_Ab_workout3,
        Beginner_Push_workout6, Beginner_Pull_workout6, Beginner_Leg_workout3, Beginner_Ab_workout3,
        Beginner_Push_workout1N, Beginner_Pull_workout7, Beginner_Leg_workout4,Beginner_Ab_workout1N,
        Beginner_Push_workout7, Beginner_Pull_workout1N, Beginner_Leg_workout4, Beginner_Ab_workout4,
        Beginner_Push_workout2N, Beginner_Pull_workout8, Beginner_Leg_workout5, Beginner_Ab_workout4,
        Beginner_Push_workout8, Beginner_Pull_workout2N, Beginner_Leg_workout5, Beginner_Ab_workout1N,
        Beginner_Push_workout3N, Beginner_Pull_workout9, Beginner_Leg_workout6, Beginner_Ab_workout5,
        Beginner_Push_workout9, Beginner_Pull_workout3N, Beginner_Leg_workout6, Beginner_Ab_workout1N,
        Beginner_Push_workout4N,Beginner_Pull_workout4N, Beginner_Leg_workout7, Beginner_Ab_workout5,
        Beginner_Push_workout10,Beginner_Pull_workout10, Beginner_Leg_workout7,Beginner_Ab_workout2N,
        Beginner_Push_workout5N,Beginner_Pull_workout5N, Beginner_Leg_workout1N,Beginner_Ab_workout2N,
        Beginner_Push_workout6N,Beginner_Pull_workout6N, Beginner_Leg_workout8, Beginner_Ab_workout6,
        Beginner_Push_workout11,Beginner_Pull_workout11, Beginner_Leg_workout1N,Beginner_Ab_workout3N,
        Beginner_Pull_workout7N,Beginner_Pull_workout7N, Beginner_Leg_workout8,Beginner_Ab_workout6,
        Beginner_Push_workout12,Beginner_Pull_workout12, Beginner_Leg_workout2N,Beginner_Ab_workout3N,
        Beginner_Push_workout8N,Beginner_Pull_workout8N, Beginner_Leg_workout9,Beginner_Ab_workout7,
        Beginner_Push_workout13,Beginner_Pull_workout13, Beginner_Leg_workout2N,Beginner_Ab_workout7,
        Beginner_Push_workout9N,Beginner_Pull_workout9N, Beginner_Leg_workout3N,Beginner_Ab_workout3N,
        Beginner_Push_workout14,Beginner_Pull_workout14, Beginner_Leg_workout9,Beginner_Ab_workout7,
        Beginner_Push_workout10N,Beginner_Pull_workout10N,Beginner_Leg_workout3N,Beginner_Ab_workout8,
        Beginner_Push_workout15,Beginner_Pull_workout15,  Beginner_Leg_workout3N,Beginner_Ab_workout8,
        Beginner_Push_workout16,Beginner_Pull_workout11N, Beginner_Leg_workout8,Beginner_Ab_workout4N,
        Beginner_Push_workout17,Beginner_Pull_workout16, Beginner_Leg_workout9,Beginner_Ab_workout8,
        Beginner_Push_workout16,Beginner_Pull_workout12N, Beginner_Leg_workout10,Beginner_Ab_workout4N,
        Beginner_Push_workout17,Beginner_Pull_workout17, Beginner_Leg_workout4N,Beginner_Ab_workout9,
        Beginner_Push_workout18,Beginner_Pull_workout13N, Beginner_Leg_workout10,Beginner_Ab_workout9,
        Beginner_Push_workout17,Beginner_Pull_workout18, Beginner_Leg_workout4N,Beginner_Ab_workout5N,
        Beginner_Push_workout18,Beginner_Pull_workout14N, Beginner_Leg_workout5N,Beginner_Ab_workout10,
        Beginner_Push_workout19,Beginner_Pull_workout19, Beginner_Leg_workout10,Beginner_Ab_workout10,
        Beginner_Push_workout20,Beginner_Pull_workout15N,Beginner_Leg_workout5N,Beginner_Ab_workout5N,
        Beginner_Push_workout20,Beginner_Pull_workout20,Beginner_Leg_workout5N,Beginner_Ab_workout5N,

        Inter_Push_workout1, Inter_Pull_workout1, Inter_Leg_workout1, Inter_Ab_workout1,
        Inter_Push_workout2, Inter_Pull_workout2, Inter_Leg_workout2, Inter_Ab_workout2,
        Inter_Push_workout3, Inter_Pull_workout3, Inter_Leg_workout3, Inter_Ab_workout3,
        Inter_Push_workout4, Inter_Pull_workout1N, Inter_Leg_workout4, Inter_Ab_workout1N,
        Inter_Push_workout5, Inter_Pull_workout4, Inter_Leg_workout5, Inter_Ab_workout4,
        Inter_Push_workout6, Inter_Pull_workout2N, Inter_Leg_workout5, Inter_Ab_workout5,
        Inter_Push_workout1N, Inter_Pull_workout5, Inter_Leg_workout6, Inter_Ab_workout2N,
        Inter_Push_workout7, Inter_Pull_workout3N, Inter_Leg_workout7, Inter_Ab_workout6,
        Inter_Push_workout2N, Inter_Pull_workout6, Inter_Leg_workout7, Inter_Ab_workout7,
        Inter_Push_workout8, Inter_Pull_workout4N, Inter_Leg_workout8, Inter_Ab_workout3N,
        Inter_Push_workout9, Inter_Pull_workout7, Inter_Leg_workout1N, Inter_Ab_workout7,
        Inter_Push_workout3N, Inter_Pull_workout5N, Inter_Leg_workout8, Inter_Ab_workout3N,
        Inter_Push_workout10, Inter_Pull_workout8, Inter_Leg_workout1N, Inter_Ab_workout8,
        Inter_Push_workout11, Inter_Pull_workout9, Inter_Leg_workout9, Inter_Ab_workout8,
        Inter_Push_workout12, Inter_Pull_workout10, Inter_Leg_workout2N, Inter_Ab_workout4N,
        Inter_Push_workout13, Inter_Pull_workout11, Inter_Leg_workout10, Inter_Ab_workout9,
        Inter_Push_workout14, Inter_Pull_workout12, Inter_Leg_workout3N, Inter_Ab_workout4N,
        Inter_Push_workout15, Inter_Pull_workout13, Inter_Leg_workout10, Inter_Ab_workout10,
        Inter_Push_workout16, Inter_Pull_workout14, Inter_Leg_workout10, Inter_Ab_workout4N,
        Inter_Push_workout17, Inter_Pull_workout15, Inter_Leg_workout3N, Inter_Ab_workout5N,
        Inter_Push_workout4N, Inter_Pull_workout16, Inter_Leg_workout4N, Inter_Ab_workout10,
        Inter_Push_workout18, Inter_Pull_workout17, Inter_Leg_workout4N, Inter_Ab_workout5N,
        Inter_Push_workout5N, Inter_Pull_workout18, Inter_Leg_workout5N, Inter_Ab_workout5N,
        Inter_Push_workout19, Inter_Pull_workout19, Inter_Leg_workout5N, Inter_Ab_workout10,
        Inter_Push_workout20, Inter_Pull_workout20, Inter_Leg_workout5N, Inter_Ab_workout5N,

        Advanced_Push_workout1, Advanced_Pull_workout1, Advanced_Leg_workout1, Advanced_Ab_workout1,
        Advanced_Push_workout2, Advanced_Pull_workout1N, Advanced_Leg_workout2, Advanced_Ab_workout2,
        Advanced_Push_workout3, Advanced_Pull_workout2, Advanced_Leg_workout3, Advanced_Ab_workout1N,
        Advanced_Push_workout1N, Advanced_Pull_workout3, Advanced_Leg_workout1, Advanced_Ab_workout3,
        Advanced_Push_workout4, Advanced_Pull_workout2N, Advanced_Leg_workout4, Advanced_Ab_workout4,
        Advanced_Push_workout2N, Advanced_Pull_workout4, Advanced_Leg_workout5, Advanced_Ab_workout2N,
        Advanced_Push_workout5, Advanced_Pull_workout5, Advanced_Leg_workout2N, Advanced_Ab_workout5,
        Advanced_Push_workout3N, Advanced_Pull_workout3N, Advanced_Leg_workout6, Advanced_Ab_workout6,
        Advanced_Push_workout6, Advanced_Pull_workout6, Advanced_Leg_workout3N, Advanced_Ab_workout3N,
        Advanced_Push_workout7, Advanced_Pull_workout7, Advanced_Leg_workout7, Advanced_Ab_workout7,
        Advanced_Push_workout4N, Advanced_Pull_workout8, Advanced_Leg_workout8, Advanced_Ab_workout8,
        Advanced_Push_workout8, Advanced_Pull_workout4N, Advanced_Leg_workout4N, Advanced_Ab_workout9,
        Advanced_Push_workout9, Advanced_Pull_workout9, Advanced_Leg_workout9, Advanced_Ab_workout4N,
        Advanced_Push_workout10, Advanced_Pull_workout10, Advanced_Leg_workout10, Advanced_Ab_workout10,
        Advanced_Push_workout5N, Advanced_Pull_workout5N, Advanced_Leg_workout5N, Advanced_Ab_workout5N,

    )

    val workoutPlansDays5= listOf(
        Beginner_Full_Body_workout1, Beginner_Full_Body_workout2,Beginner_Full_Body_workout3, Beginner_Full_Body_workout4, Beginner_Full_Body_workout5,
        Beginner_Full_Body_workout6,Beginner_Full_Body_workout7,Beginner_Full_Body_workout8,Beginner_Full_Body_workout9,

        Beginner_Push_workout1, Beginner_Pull_workout1, Beginner_Leg_workout1, Beginner_Ab_workout1, Beginner_Full_Body_workout10,
        Beginner_Push_workout2, Beginner_Pull_workout2, Beginner_Leg_workout1, Beginner_Ab_workout1, Beginner_Full_Body_workout10,
        Beginner_Push_workout3, Beginner_Pull_workout3, Beginner_Leg_workout2, Beginner_Ab_workout2, Rest_workout,
        Beginner_Push_workout4, Beginner_Pull_workout4, Beginner_Leg_workout2, Beginner_Ab_workout2, Beginner_Full_Body_workout11,
        Beginner_Push_workout5, Beginner_Pull_workout5, Beginner_Leg_workout3, Beginner_Ab_workout3, Beginner_Full_Body_workout11,
        Beginner_Push_workout6, Beginner_Pull_workout6, Beginner_Leg_workout3, Beginner_Ab_workout3, Rest_workout,
        Beginner_Push_workout1N, Beginner_Pull_workout7, Beginner_Leg_workout4,Beginner_Ab_workout1N, Beginner_Full_Body_workout12,
        Beginner_Push_workout7, Beginner_Pull_workout1N, Beginner_Leg_workout4, Beginner_Ab_workout4, Beginner_Full_Body_workout12,
        Beginner_Push_workout2N, Beginner_Pull_workout8, Beginner_Leg_workout5, Beginner_Ab_workout4, Rest_workout,
        Beginner_Push_workout8, Beginner_Pull_workout2N, Beginner_Leg_workout5, Beginner_Ab_workout1N, Beginner_Full_Body_workout13,
        Beginner_Push_workout3N, Beginner_Pull_workout9, Beginner_Leg_workout6, Beginner_Ab_workout5, Beginner_Full_Body_workout13,
        Beginner_Push_workout9, Beginner_Pull_workout3N, Beginner_Leg_workout6, Beginner_Ab_workout1N, Rest_workout,
        Beginner_Push_workout4N,Beginner_Pull_workout4N, Beginner_Leg_workout7, Beginner_Ab_workout5,Beginner_Full_Body_workout14,
        Beginner_Push_workout10,Beginner_Pull_workout10, Beginner_Leg_workout7,Beginner_Ab_workout2N,Beginner_Full_Body_workout14,
        Beginner_Push_workout5N,Beginner_Pull_workout5N, Beginner_Leg_workout1N,Beginner_Ab_workout2N,Rest_workout,
        Beginner_Push_workout6N,Beginner_Pull_workout6N, Beginner_Leg_workout8, Beginner_Ab_workout6,Beginner_Full_Body_workout15,
        Beginner_Push_workout11,Beginner_Pull_workout11, Beginner_Leg_workout1N,Beginner_Ab_workout3N,Beginner_Full_Body_workout15,
        Beginner_Pull_workout7N,Beginner_Pull_workout7N, Beginner_Leg_workout8,Beginner_Ab_workout6,Rest_workout,
        Beginner_Push_workout12,Beginner_Pull_workout12, Beginner_Leg_workout2N,Beginner_Ab_workout3N,Beginner_Full_Body_workout16,
        Beginner_Push_workout8N,Beginner_Pull_workout8N, Beginner_Leg_workout9,Beginner_Ab_workout7,Beginner_Full_Body_workout16,
        Beginner_Push_workout13,Beginner_Pull_workout13, Beginner_Leg_workout2N,Beginner_Ab_workout7,Rest_workout,
        Beginner_Push_workout9N,Beginner_Pull_workout9N, Beginner_Leg_workout3N,Beginner_Ab_workout3N,Beginner_Full_Body_workout17,
        Beginner_Push_workout14,Beginner_Pull_workout14, Beginner_Leg_workout9,Beginner_Ab_workout7,Beginner_Full_Body_workout17,
        Beginner_Push_workout10N,Beginner_Pull_workout10N,Beginner_Leg_workout3N,Beginner_Ab_workout8,Rest_workout,
        Beginner_Push_workout15,Beginner_Pull_workout15,  Beginner_Leg_workout3N,Beginner_Ab_workout8,Beginner_Full_Body_workout18,
        Beginner_Push_workout16,Beginner_Pull_workout11N, Beginner_Leg_workout8,Beginner_Ab_workout4N,Beginner_Full_Body_workout18,
        Beginner_Push_workout17,Beginner_Pull_workout16, Beginner_Leg_workout9,Beginner_Ab_workout8,    Rest_workout,
        Beginner_Push_workout16,Beginner_Pull_workout12N, Beginner_Leg_workout10,Beginner_Ab_workout4N,Beginner_Full_Body_workout19,
        Beginner_Push_workout17,Beginner_Pull_workout17, Beginner_Leg_workout4N,Beginner_Ab_workout9,Beginner_Full_Body_workout19,
        Beginner_Push_workout18,Beginner_Pull_workout13N, Beginner_Leg_workout10,Beginner_Ab_workout9,Rest_workout,
        Beginner_Push_workout17,Beginner_Pull_workout18, Beginner_Leg_workout4N,Beginner_Ab_workout5N,Beginner_Full_Body_workout19,
        Beginner_Push_workout18,Beginner_Pull_workout14N, Beginner_Leg_workout5N,Beginner_Ab_workout10,Beginner_Full_Body_workout20,
        Beginner_Push_workout19,Beginner_Pull_workout19, Beginner_Leg_workout10,Beginner_Ab_workout10,Rest_workout,
        Beginner_Push_workout20,Beginner_Pull_workout15N,Beginner_Leg_workout5N,Beginner_Ab_workout5N,Beginner_Full_Body_workout20,
        Beginner_Push_workout20,Beginner_Pull_workout20,Beginner_Leg_workout5N,Beginner_Ab_workout5N,Beginner_Full_Body_workout20,

        Inter_Push_workout1, Inter_Pull_workout1, Inter_Leg_workout1, Inter_Ab_workout1, Inter_Full_Body_workout1,
        Inter_Push_workout2, Inter_Pull_workout2, Inter_Leg_workout2, Inter_Ab_workout2, Inter_Full_Body_workout2,
        Inter_Push_workout3, Inter_Pull_workout3, Inter_Leg_workout3, Inter_Ab_workout3, Inter_Full_Body_workout3,
        Inter_Push_workout4, Inter_Pull_workout1N, Inter_Leg_workout4, Inter_Ab_workout1N, Inter_Full_Body_workout4,
        Inter_Push_workout5, Inter_Pull_workout4, Inter_Leg_workout5, Inter_Ab_workout4, Inter_Full_Body_workout5,
        Inter_Push_workout6, Inter_Pull_workout2N, Inter_Leg_workout5, Inter_Ab_workout5,    Rest_workout,
        Inter_Push_workout1N, Inter_Pull_workout5, Inter_Leg_workout6, Inter_Ab_workout2N, Inter_Full_Body_workout6,
        Inter_Push_workout7, Inter_Pull_workout3N, Inter_Leg_workout7, Inter_Ab_workout6, Inter_Full_Body_workout7,
        Inter_Push_workout2N, Inter_Pull_workout6, Rest_workout,        Inter_Ab_workout7, Inter_Full_Body_workout8,
        Inter_Push_workout8, Inter_Pull_workout4N, Inter_Leg_workout8, Inter_Ab_workout3N, Inter_Full_Body_workout9,
        Inter_Push_workout9, Inter_Pull_workout7, Inter_Leg_workout1N, Inter_Ab_workout7, Rest_workout,
        Inter_Push_workout3N, Inter_Pull_workout5N, Inter_Leg_workout8, Inter_Ab_workout3N, Inter_Full_Body_workout10,
        Inter_Push_workout10, Inter_Pull_workout8, Inter_Leg_workout1N, Inter_Ab_workout8, Inter_Full_Body_workout11,
        Inter_Push_workout11, Inter_Pull_workout9, Inter_Leg_workout9, Inter_Ab_workout8, Inter_Full_Body_workout12,
        Inter_Push_workout12, Inter_Pull_workout10, Inter_Leg_workout2N, Inter_Ab_workout4N, Inter_Full_Body_workout12,
        Inter_Push_workout13, Inter_Pull_workout11, Inter_Leg_workout10, Inter_Ab_workout9, Inter_Full_Body_workout13,
        Inter_Push_workout14, Inter_Pull_workout12, Inter_Leg_workout3N, Inter_Ab_workout4N, Inter_Full_Body_workout14,
        Inter_Push_workout15, Inter_Pull_workout13, Rest_workout,        Inter_Ab_workout10, Inter_Full_Body_workout15,
        Inter_Push_workout16, Inter_Pull_workout14, Inter_Leg_workout10, Rest_workout,       Inter_Full_Body_workout16,
        Inter_Push_workout17, Inter_Pull_workout15, Inter_Leg_workout3N, Inter_Ab_workout5N, Rest_workout,
        Inter_Push_workout4N, Inter_Pull_workout16, Inter_Leg_workout4N, Inter_Ab_workout10, Inter_Full_Body_workout17,
        Inter_Push_workout18, Inter_Pull_workout17, Inter_Leg_workout4N, Inter_Ab_workout5N, Inter_Full_Body_workout18,
        Inter_Push_workout5N, Inter_Pull_workout18, Rest_workout,         Inter_Ab_workout5N, Inter_Full_Body_workout19,
        Inter_Push_workout19, Inter_Pull_workout19, Inter_Leg_workout5N, Rest_workout,       Inter_Full_Body_workout20,
        Inter_Push_workout20, Inter_Pull_workout20, Inter_Leg_workout5N, Inter_Ab_workout5N, Inter_Full_Body_workout20,

        Advanced_Push_workout1, Advanced_Pull_workout1, Advanced_Leg_workout1, Advanced_Ab_workout1, Advanced_Full_Body_workout1,
        Advanced_Push_workout2, Advanced_Pull_workout1N, Advanced_Leg_workout2, Advanced_Ab_workout2, Advanced_Full_Body_workout2,
        Advanced_Push_workout3, Advanced_Pull_workout2, Advanced_Leg_workout3, Advanced_Ab_workout1N, Advanced_Full_Body_workout3,
        Advanced_Push_workout1N, Advanced_Pull_workout3, Advanced_Leg_workout1, Advanced_Ab_workout3, Advanced_Full_Body_workout3,
        Advanced_Push_workout4, Advanced_Pull_workout2N, Advanced_Leg_workout4, Advanced_Ab_workout4, Advanced_Full_Body_workout4,
        Advanced_Push_workout2N, Advanced_Pull_workout4, Advanced_Leg_workout5, Advanced_Ab_workout2N, Advanced_Full_Body_workout5,
        Advanced_Push_workout5, Advanced_Pull_workout5, Advanced_Leg_workout2N, Advanced_Ab_workout5, Advanced_Full_Body_workout5,
        Advanced_Push_workout3N, Advanced_Pull_workout3N, Advanced_Leg_workout6, Advanced_Ab_workout6, Advanced_Full_Body_workout6,
        Advanced_Push_workout6, Advanced_Pull_workout6, Advanced_Leg_workout3N, Advanced_Ab_workout3N, Advanced_Full_Body_workout7,
        Advanced_Push_workout7, Advanced_Pull_workout7, Advanced_Leg_workout7, Advanced_Ab_workout7, Advanced_Full_Body_workout8,
        Advanced_Push_workout4N, Advanced_Pull_workout8, Advanced_Leg_workout8, Advanced_Ab_workout8, Advanced_Full_Body_workout8,
        Advanced_Push_workout8, Advanced_Pull_workout4N, Advanced_Leg_workout4N, Advanced_Ab_workout9, Advanced_Full_Body_workout9,
        Advanced_Push_workout9, Advanced_Pull_workout9, Advanced_Leg_workout9, Advanced_Ab_workout4N, Advanced_Full_Body_workout9,
        Advanced_Push_workout10, Advanced_Pull_workout10, Advanced_Leg_workout10, Advanced_Ab_workout10, Advanced_Full_Body_workout10,
        Advanced_Push_workout5N, Advanced_Pull_workout5N, Advanced_Leg_workout5N, Advanced_Ab_workout5N, Advanced_Full_Body_workout10
        )

    val workoutPlansDays6 = listOf(
        Beginner_Full_Body_workout1, Beginner_Full_Body_workout2,Beginner_Full_Body_workout3, Beginner_Full_Body_workout4, Beginner_Full_Body_workout5,
        Beginner_Full_Body_workout6,Beginner_Full_Body_workout7,Beginner_Full_Body_workout8,Beginner_Full_Body_workout9,Beginner_Full_Body_workout10, Beginner_Full_Body_workout11,
        //ez a 11.
        Beginner_Push_workout1, Beginner_Pull_workout1, Beginner_Leg_workout1,Beginner_Push_workout2, Beginner_Pull_workout2,Beginner_Ab_workout1,
        Beginner_Push_workout3, Beginner_Pull_workout3, Beginner_Leg_workout2, Beginner_Push_workout4, Beginner_Pull_workout4,Beginner_Ab_workout2,
        Beginner_Push_workout5, Beginner_Pull_workout5, Beginner_Leg_workout3, Beginner_Push_workout6, Beginner_Pull_workout6,  Beginner_Ab_workout3,
        Beginner_Push_workout1N, Beginner_Pull_workout7, Beginner_Leg_workout4,Beginner_Push_workout7, Beginner_Pull_workout1N, Beginner_Ab_workout1N,
        Beginner_Push_workout2N, Beginner_Pull_workout8, Beginner_Leg_workout5, Beginner_Push_workout8, Beginner_Pull_workout2N,Beginner_Ab_workout4,
        Beginner_Push_workout3N, Beginner_Pull_workout9, Beginner_Leg_workout5, Beginner_Push_workout9, Beginner_Pull_workout3N, Beginner_Ab_workout1N,
        Beginner_Push_workout4N,Beginner_Pull_workout4N, Beginner_Leg_workout6, Beginner_Push_workout10,Beginner_Pull_workout10, Beginner_Ab_workout5,
        Beginner_Push_workout5N,Beginner_Pull_workout5N,Beginner_Leg_workout7,Beginner_Push_workout6N,Beginner_Pull_workout6N, Beginner_Ab_workout2N,
        Beginner_Push_workout11,Beginner_Pull_workout11, Beginner_Leg_workout1N,Beginner_Pull_workout7N,Beginner_Pull_workout7N, Beginner_Ab_workout3N,
        Beginner_Push_workout12,Beginner_Pull_workout12,Beginner_Leg_workout8,Beginner_Push_workout8N,Beginner_Pull_workout8N, Beginner_Ab_workout6,
        Beginner_Push_workout13,Beginner_Pull_workout13, Beginner_Leg_workout2N,Beginner_Push_workout9N,Beginner_Pull_workout9N, Beginner_Ab_workout3N,
        Beginner_Push_workout14,Beginner_Pull_workout14, Beginner_Leg_workout9,Beginner_Push_workout10N,Beginner_Pull_workout10N,Beginner_Ab_workout7,
        Beginner_Push_workout15,Beginner_Pull_workout15,  Beginner_Leg_workout3N,Beginner_Push_workout16,Beginner_Pull_workout11N, Beginner_Ab_workout8,
        Beginner_Push_workout17,Beginner_Pull_workout16,Beginner_Leg_workout9,Beginner_Push_workout16,Beginner_Pull_workout12N,Beginner_Ab_workout8,
        Beginner_Push_workout17,Beginner_Pull_workout17, Beginner_Leg_workout10,Beginner_Push_workout18,Beginner_Pull_workout13N,Beginner_Ab_workout4N,
        Beginner_Push_workout17,Beginner_Pull_workout18,Beginner_Leg_workout10,Beginner_Push_workout18,Beginner_Pull_workout14N, Beginner_Ab_workout9,
        Beginner_Push_workout19,Beginner_Pull_workout19, Beginner_Leg_workout5N,Beginner_Push_workout20,Beginner_Pull_workout15N,Beginner_Ab_workout10,
        Beginner_Push_workout20,Beginner_Pull_workout20,Beginner_Leg_workout5N,Beginner_Push_workout20,Beginner_Pull_workout20,Beginner_Ab_workout5N,
        //ez a 119 (lenti)
        Inter_Push_workout1, Inter_Pull_workout1, Inter_Leg_workout1, Inter_Push_workout2, Inter_Pull_workout2, Inter_Ab_workout1,
        Inter_Push_workout3, Inter_Pull_workout3, Inter_Leg_workout2, Inter_Push_workout4, Inter_Pull_workout1N, Inter_Ab_workout2,
        Inter_Push_workout5, Inter_Pull_workout4, Inter_Leg_workout4, Inter_Push_workout5, Inter_Pull_workout4, Inter_Ab_workout1N,
        Inter_Push_workout6, Inter_Pull_workout2N,Inter_Leg_workout5, Inter_Push_workout6, Inter_Pull_workout2N,Inter_Ab_workout4,
        Inter_Push_workout1N, Inter_Pull_workout5, Inter_Leg_workout6,Inter_Push_workout7, Inter_Pull_workout3N, Inter_Ab_workout2N,
        Inter_Push_workout2N, Inter_Pull_workout6, Inter_Leg_workout7,Inter_Push_workout2N, Inter_Pull_workout6,  Inter_Ab_workout6,
        Inter_Push_workout8, Inter_Pull_workout4N, Inter_Leg_workout8,  Inter_Push_workout9, Inter_Pull_workout7, Inter_Ab_workout3N,
        Inter_Push_workout3N, Inter_Pull_workout5N, Inter_Leg_workout9,Inter_Push_workout3N, Inter_Pull_workout5N,  Inter_Ab_workout7,
        Inter_Push_workout10, Inter_Pull_workout8, Inter_Leg_workout1N,Inter_Push_workout11, Inter_Pull_workout9, Inter_Ab_workout8,
        Inter_Push_workout12, Inter_Pull_workout10, Inter_Leg_workout2N,Inter_Push_workout12, Inter_Pull_workout10,  Inter_Ab_workout4N,
        Inter_Push_workout13, Inter_Pull_workout11, Inter_Leg_workout10, Inter_Push_workout14, Inter_Pull_workout12,Inter_Ab_workout9,
        Inter_Push_workout15, Inter_Pull_workout13, Inter_Leg_workout10, Inter_Push_workout16, Inter_Pull_workout14,Inter_Ab_workout10,
        Inter_Push_workout17, Inter_Pull_workout15, Inter_Leg_workout10, Inter_Push_workout17, Inter_Pull_workout15, Inter_Ab_workout4N,
        Inter_Push_workout4N, Inter_Pull_workout16, Inter_Leg_workout4N, Inter_Push_workout18, Inter_Pull_workout17, Inter_Ab_workout10,
        Inter_Push_workout5N, Inter_Pull_workout18, Inter_Leg_workout4N, Inter_Push_workout5N, Inter_Pull_workout18, Inter_Ab_workout5N,
        Inter_Push_workout19, Inter_Pull_workout19, Inter_Leg_workout5N,Inter_Push_workout20, Inter_Pull_workout20, Inter_Ab_workout10,
        Inter_Push_workout20, Inter_Pull_workout20, Inter_Leg_workout5N,Inter_Push_workout20, Inter_Pull_workout20, Inter_Ab_workout5N,
        //221. a lenti
        Advanced_Push_workout1, Advanced_Pull_workout1, Advanced_Leg_workout1, Advanced_Push_workout2, Advanced_Pull_workout1N,Advanced_Ab_workout1,
        Advanced_Push_workout3, Advanced_Pull_workout2, Advanced_Leg_workout3, Advanced_Push_workout1N, Advanced_Pull_workout3, Advanced_Ab_workout1N,
        Advanced_Push_workout4, Advanced_Pull_workout2N, Advanced_Leg_workout4, Advanced_Push_workout2N, Advanced_Pull_workout4, Advanced_Ab_workout4,
        Advanced_Push_workout5, Advanced_Pull_workout5, Advanced_Leg_workout2N, Advanced_Push_workout3N, Advanced_Pull_workout3N,Advanced_Ab_workout5,
        Advanced_Push_workout6, Advanced_Pull_workout6, Advanced_Leg_workout3N,Advanced_Push_workout6, Advanced_Pull_workout6, Advanced_Ab_workout3N,
        Advanced_Push_workout7, Advanced_Pull_workout7, Advanced_Leg_workout7, Advanced_Push_workout4N, Advanced_Pull_workout8,Advanced_Ab_workout7,
        Advanced_Push_workout8, Advanced_Pull_workout4N, Advanced_Leg_workout4N, Advanced_Push_workout8, Advanced_Pull_workout4N,Advanced_Ab_workout9,
        Advanced_Push_workout9, Advanced_Pull_workout9, Advanced_Leg_workout9, Advanced_Push_workout9, Advanced_Pull_workout9,Advanced_Ab_workout4N,
        Advanced_Push_workout10, Advanced_Pull_workout10, Advanced_Leg_workout10,Advanced_Push_workout10, Advanced_Pull_workout10,  Advanced_Ab_workout10,
        Advanced_Push_workout5N, Advanced_Pull_workout5N, Advanced_Leg_workout5N,Advanced_Push_workout5N, Advanced_Pull_workout5N, Advanced_Ab_workout5N,

        )
    val workoutPlansDays7 = listOf(
        Beginner_Full_Body_workout1, Beginner_Full_Body_workout2,Beginner_Full_Body_workout3, Beginner_Full_Body_workout4, Beginner_Full_Body_workout5,
        Beginner_Full_Body_workout6,Beginner_Full_Body_workout7,Beginner_Full_Body_workout8,Beginner_Full_Body_workout9,Beginner_Full_Body_workout10, Beginner_Full_Body_workout11,
        //ez a 11.
        Beginner_Push_workout1, Beginner_Pull_workout1, Beginner_Leg_workout1,Beginner_Push_workout2, Beginner_Pull_workout2,Beginner_Leg_workout1,Beginner_Ab_workout1,
        Beginner_Push_workout3, Beginner_Pull_workout3, Beginner_Leg_workout2, Beginner_Push_workout4, Beginner_Pull_workout4, Beginner_Leg_workout2,Beginner_Ab_workout2,
        Beginner_Push_workout5, Beginner_Pull_workout5, Beginner_Leg_workout3, Beginner_Push_workout6, Beginner_Pull_workout6, Beginner_Leg_workout3, Beginner_Ab_workout3,
        Beginner_Push_workout1N, Beginner_Pull_workout7, Beginner_Leg_workout4,Beginner_Push_workout7, Beginner_Pull_workout1N,Beginner_Leg_workout4, Beginner_Ab_workout1N,
        Beginner_Push_workout2N, Beginner_Pull_workout8, Beginner_Leg_workout5, Beginner_Push_workout8, Beginner_Pull_workout2N,Beginner_Leg_workout5,Beginner_Ab_workout4,
        Beginner_Push_workout3N, Beginner_Pull_workout9, Beginner_Leg_workout5, Beginner_Push_workout9, Beginner_Pull_workout3N, Beginner_Leg_workout5, Beginner_Ab_workout1N,
        Beginner_Push_workout4N,Beginner_Pull_workout4N, Beginner_Leg_workout6, Beginner_Push_workout10,Beginner_Pull_workout10,Beginner_Leg_workout6, Beginner_Ab_workout5,
        Beginner_Push_workout5N,Beginner_Pull_workout5N,Beginner_Leg_workout7,Beginner_Push_workout6N,Beginner_Pull_workout6N,Beginner_Leg_workout7, Beginner_Ab_workout2N,
        Beginner_Push_workout11,Beginner_Pull_workout11, Beginner_Leg_workout1N,Beginner_Pull_workout7N,Beginner_Pull_workout7N,Beginner_Leg_workout1N, Beginner_Ab_workout3N,
        Beginner_Push_workout12,Beginner_Pull_workout12,Beginner_Leg_workout8,Beginner_Push_workout8N,Beginner_Pull_workout8N, Beginner_Leg_workout8,Beginner_Ab_workout6,
        Beginner_Push_workout13,Beginner_Pull_workout13, Beginner_Leg_workout2N,Beginner_Push_workout9N,Beginner_Pull_workout9N,Beginner_Leg_workout2N, Beginner_Ab_workout3N,
        Beginner_Push_workout14,Beginner_Pull_workout14, Beginner_Leg_workout9,Beginner_Push_workout10N,Beginner_Pull_workout10N,Beginner_Leg_workout9,Beginner_Ab_workout7,
        Beginner_Push_workout15,Beginner_Pull_workout15,  Beginner_Leg_workout3N,Beginner_Push_workout16,Beginner_Pull_workout11N,Beginner_Leg_workout3N, Beginner_Ab_workout8,
        Beginner_Push_workout17,Beginner_Pull_workout16,Beginner_Leg_workout9,Beginner_Push_workout16,Beginner_Pull_workout12N,Beginner_Leg_workout9,Beginner_Ab_workout8,
        Beginner_Push_workout17,Beginner_Pull_workout17, Beginner_Leg_workout10,Beginner_Push_workout18,Beginner_Pull_workout13N,Beginner_Leg_workout10,Beginner_Ab_workout4N,
        Beginner_Push_workout17,Beginner_Pull_workout18,Beginner_Leg_workout10,Beginner_Push_workout18,Beginner_Pull_workout14N, Beginner_Leg_workout10,Beginner_Ab_workout9,
        Beginner_Push_workout19,Beginner_Pull_workout19, Beginner_Leg_workout5N,Beginner_Push_workout20,Beginner_Pull_workout15N,Beginner_Leg_workout5N,Beginner_Ab_workout10,
        Beginner_Push_workout20,Beginner_Pull_workout20,Beginner_Leg_workout5N,Beginner_Push_workout20,Beginner_Pull_workout20,Beginner_Leg_workout5N,Beginner_Ab_workout5N,

        Inter_Push_workout1, Inter_Pull_workout1, Inter_Leg_workout1, Inter_Push_workout2, Inter_Pull_workout2, Inter_Leg_workout1, Inter_Ab_workout1,
        Inter_Push_workout3, Inter_Pull_workout3, Inter_Leg_workout2, Inter_Push_workout4, Inter_Pull_workout1N,Inter_Leg_workout3, Inter_Ab_workout2,
        Inter_Push_workout5, Inter_Pull_workout4, Inter_Leg_workout4, Inter_Push_workout5, Inter_Pull_workout4,  Inter_Leg_workout4,Inter_Ab_workout1N,
        Inter_Push_workout6, Inter_Pull_workout2N,Inter_Leg_workout5, Inter_Push_workout6, Inter_Pull_workout2N,Inter_Leg_workout5,Inter_Ab_workout4,
        Inter_Push_workout1N, Inter_Pull_workout5, Inter_Leg_workout6,Inter_Push_workout7, Inter_Pull_workout3N,Inter_Leg_workout6, Inter_Ab_workout2N,
        Inter_Push_workout2N, Inter_Pull_workout6, Inter_Leg_workout7,Inter_Push_workout2N, Inter_Pull_workout6, Inter_Leg_workout7, Inter_Ab_workout6,
        Inter_Push_workout8, Inter_Pull_workout4N, Inter_Leg_workout8,  Inter_Push_workout9, Inter_Pull_workout7,Inter_Leg_workout8, Inter_Ab_workout3N,
        Inter_Push_workout3N, Inter_Pull_workout5N, Inter_Leg_workout1N,Inter_Push_workout3N, Inter_Pull_workout5N,  Inter_Leg_workout1N,Inter_Ab_workout7,
        Inter_Push_workout10, Inter_Pull_workout8, Inter_Leg_workout9,Inter_Push_workout11, Inter_Pull_workout9,Inter_Leg_workout9, Inter_Ab_workout8,
        Inter_Push_workout12, Inter_Pull_workout10, Inter_Leg_workout2N,Inter_Push_workout12, Inter_Pull_workout10, Inter_Leg_workout2N, Inter_Ab_workout4N,
        Inter_Push_workout13, Inter_Pull_workout11, Inter_Leg_workout10, Inter_Push_workout14, Inter_Pull_workout12, Inter_Leg_workout10,Inter_Ab_workout9,
        Inter_Push_workout15, Inter_Pull_workout13, Inter_Leg_workout10, Inter_Push_workout16, Inter_Pull_workout14,Inter_Leg_workout10,Inter_Ab_workout10,
        Inter_Push_workout17, Inter_Pull_workout15, Inter_Leg_workout10, Inter_Push_workout17, Inter_Pull_workout15, Inter_Leg_workout10,Inter_Ab_workout4N,
        Inter_Push_workout4N, Inter_Pull_workout16, Inter_Leg_workout4N, Inter_Push_workout18, Inter_Pull_workout17,Inter_Leg_workout4N, Inter_Ab_workout10,
        Inter_Push_workout5N, Inter_Pull_workout18, Inter_Leg_workout4N, Inter_Push_workout5N, Inter_Pull_workout18, Inter_Leg_workout4N, Inter_Ab_workout5N,
        Inter_Push_workout19, Inter_Pull_workout19, Inter_Leg_workout5N,Inter_Push_workout20, Inter_Pull_workout20, Inter_Leg_workout5N,Inter_Ab_workout10,
        Inter_Push_workout20, Inter_Pull_workout20, Inter_Leg_workout5N,Inter_Push_workout20, Inter_Pull_workout20,  Inter_Leg_workout5N,Inter_Ab_workout5N,
        //221. a lenti +35
        Advanced_Push_workout1, Advanced_Pull_workout1, Advanced_Leg_workout1, Advanced_Push_workout2, Advanced_Pull_workout1N,Advanced_Leg_workout2,Advanced_Ab_workout1,
        Advanced_Push_workout3, Advanced_Pull_workout2, Advanced_Leg_workout3, Advanced_Push_workout1N, Advanced_Pull_workout3,Advanced_Leg_workout3, Advanced_Ab_workout1N,
        Advanced_Push_workout4, Advanced_Pull_workout2N, Advanced_Leg_workout4, Advanced_Push_workout2N, Advanced_Pull_workout4,Advanced_Leg_workout4, Advanced_Ab_workout4,
        Advanced_Push_workout5, Advanced_Pull_workout5, Advanced_Leg_workout5, Advanced_Push_workout3N, Advanced_Pull_workout3N,Advanced_Leg_workout2N,Advanced_Ab_workout5,
        Advanced_Push_workout6, Advanced_Pull_workout6, Advanced_Leg_workout3N,Advanced_Push_workout6, Advanced_Pull_workout6,  Advanced_Leg_workout3N,Advanced_Ab_workout3N,
        Advanced_Push_workout7, Advanced_Pull_workout7, Advanced_Leg_workout6, Advanced_Push_workout4N, Advanced_Pull_workout8, Advanced_Leg_workout7,Advanced_Ab_workout7,
        Advanced_Push_workout8, Advanced_Pull_workout4N, Advanced_Leg_workout8, Advanced_Push_workout8, Advanced_Pull_workout4N,Advanced_Leg_workout4N, Advanced_Ab_workout9,
        Advanced_Push_workout9, Advanced_Pull_workout9, Advanced_Leg_workout9, Advanced_Push_workout9, Advanced_Pull_workout9,Advanced_Leg_workout9,Advanced_Ab_workout4N,
        Advanced_Push_workout10, Advanced_Pull_workout10, Advanced_Leg_workout10,Advanced_Push_workout10, Advanced_Pull_workout10, Advanced_Leg_workout10, Advanced_Ab_workout10,
        Advanced_Push_workout5N, Advanced_Pull_workout5N, Advanced_Leg_workout5N,Advanced_Push_workout5N, Advanced_Pull_workout5N, Advanced_Leg_workout5N,Advanced_Ab_workout5N,

        )

    val startIndex = when {
        userProfile.daysToTrain == 3 || userProfile.daysToTrain == 2 -> {
            when {
                (userProfile.maxPull == 0 || userProfile.maxPush < 15) -> 0
                 userProfile.maxPull == 0 || userProfile.maxPush < 20 -> 3
                 userProfile.maxPull == 0 || userProfile.maxPush < 30 -> 6
                 userProfile.maxPull <=  10 -> 9 //beginner fb 10
                (userProfile.maxPull <=  25 || userProfile.maxPush <= 30) -> 20 //inter fb 1
                (userProfile.maxPull <=  40 || userProfile.maxPush < 80) -> 40 //advanced fb 1
                else -> 0
            }
        }
        userProfile.daysToTrain == 4 -> {
            when {
                (userProfile.maxPull == 0 || userProfile.maxPush < 10) -> 0
                (userProfile.maxPull == 0 || userProfile.maxPush < 15) -> 3
                (userProfile.maxPull == 0 || userProfile.maxPush < 20) -> 6
                userProfile.maxPull <=  5 -> 11 //beginner push 1
                (userProfile.maxPull <=  10 || userProfile.maxPush < 30) -> 23 //beginner push 4
                (userProfile.maxPull <=  15 || userProfile.maxPush < 40) -> 63 //beginner push 10
                (userProfile.maxPull <=  20 || userProfile.maxPush < 50) -> 107 //beginner push 15
                (userProfile.maxPull <=  25 || userProfile.maxPush < 60) -> 151 //inter push 1
                (userProfile.maxPull <=  35 || userProfile.maxPush < 80) -> 185 //inter push 8
                (userProfile.maxPull <=  50 || userProfile.maxPush < 100) -> 251 //advanced push 1
                else -> 0
            }
        }
        userProfile.daysToTrain == 5 -> {
            when {
                (userProfile.maxPull == 0 || userProfile.maxPush < 10) -> 0 //beginner full body 1
                (userProfile.maxPull == 0 || userProfile.maxPush < 15 ) -> 3
                (userProfile.maxPull == 0 || userProfile.maxPush < 20 ) -> 6
                userProfile.maxPull <=  5 -> 9 //beginner push 1
                (userProfile.maxPull <=  10 || userProfile.maxPush < 30) -> 24 //beginner push 4
                (userProfile.maxPull <=  15 || userProfile.maxPush < 40) -> 74 //beginner push 10
                (userProfile.maxPull <=  20 || userProfile.maxPush < 50) -> 129 //beginner push 15
                (userProfile.maxPull <=  25 || userProfile.maxPush < 60) -> 139 //inter push 1
                (userProfile.maxPull <=  35 || userProfile.maxPush < 80) -> 244 //inter push 10
                (userProfile.maxPull <=  50 || userProfile.maxPush < 100) -> 309 //advanced push 1
                else -> 0
            }
        }
        userProfile.daysToTrain == 6 -> {
            when {
                (userProfile.maxPull == 0 || userProfile.maxPush < 10) -> 0 //beginner full body 1
                (userProfile.maxPull == 0 || userProfile.maxPush < 15 ) -> 3
                (userProfile.maxPull == 0 || userProfile.maxPush < 20 ) -> 6
                userProfile.maxPull <=  5 -> 11 //beginner push 1
                (userProfile.maxPull <=  10 || userProfile.maxPush < 30) -> 35 //beginner push 2N
                (userProfile.maxPull <=  20 || userProfile.maxPush < 50) -> 65 //beginner push 12
                (userProfile.maxPull <=  25 || userProfile.maxPush < 60) -> 119 //inter push 1
                (userProfile.maxPull <=  50 || userProfile.maxPush < 100) -> 221 //advanced push 1
                else -> 0
            }
        }
        userProfile.daysToTrain == 7 -> {
            when {
                (userProfile.maxPull == 0 || userProfile.maxPush < 10) -> 0 //beginner full body 1
                (userProfile.maxPull == 0 || userProfile.maxPush < 15 ) -> 3
                (userProfile.maxPull == 0 || userProfile.maxPush < 20 ) -> 6
                userProfile.maxPull <=  5 -> 11 //beginner push 1
                (userProfile.maxPull <=  10 || userProfile.maxPush < 30) -> 39 //beginner push 2N
                (userProfile.maxPull <=  20 || userProfile.maxPush < 50) -> 74 //beginner push 12
                (userProfile.maxPull <=  25 || userProfile.maxPush < 60) -> 137 //inter push 1
                (userProfile.maxPull <=  50 || userProfile.maxPush < 100) -> 256 //advanced push 1
                else -> 0
            }
        }
        else -> 0
    }

    return when {
        (userProfile.daysToTrain == 3 || userProfile.daysToTrain == 2) -> workoutPlansDays3.subList(startIndex, workoutPlansDays3.size)
        userProfile.daysToTrain == 4 -> workoutPlansDays4.subList(startIndex, workoutPlansDays4.size)
        userProfile.daysToTrain == 5 -> workoutPlansDays5.subList(startIndex, workoutPlansDays5.size)
        userProfile.daysToTrain == 6 -> workoutPlansDays6.subList(startIndex, workoutPlansDays6.size)
        userProfile.daysToTrain == 7 -> workoutPlansDays7.subList(startIndex, workoutPlansDays7.size)
        else -> emptyList()
    }

}

//Push exercises
var PushUpHold = Exercise("Push-up Hold", "10-12", "videos/pushup.jpg","AabW70NVg5o")
var KneePushUp = Exercise("Knee Push-up", "10-12", "videos/knee_pushup.jpg","rMGFLWLLivo")
var NegativePushUp = Exercise("Negative Push-up", "10-12", "videos/negative_pushup.jpg","yOPz3wUDGwA")
var InclinePushUp = Exercise("Incline Push-up", "", "videos/incline_pushup.jpg","t38mF1vZ1fI")
var PushUp = Exercise("Push-up", "10-12", "videos/pushup.jpg","2Gsefa3R_Ls")
var DiamondPushUp = Exercise("Diamond Push-up", "", "videos/diamond_pushup.jpg","Vln_bHIFwvg")
var ArcherPushUp = Exercise("Archer Push-up", "", "videos/archer_pushup.jpg","elqTfo3dT0o")
var PikePushUp = Exercise("Pike Push-up", "", "videos/pike_pushup.jpg","yGaxT5uQlDM")
var WallHsPushUp = Exercise("Back to Wall Handstand Push-up", "", "videos/wall_hs_pushup.jpg","3lrvoaUGKDg")
var HsPushUp = Exercise("Handstand Push-up", "", "videos/hspu.jpg","aa5qmlhpPBQ")
var HinduPushUp = Exercise("Hindu Push-up", "", "videos/hindu_pushup.jpg","r-I1oNbQgbw")
var PseudoPlanchePushUp= Exercise("Pseudo Planche Push-up","","videos/pseudo_planche_pushup.jpg","YE-hH9YwcrU")
var ExplosivePushUp = Exercise("Explosive Push-up", "", "videos/explosive_pushup.jpg","-qtCyeRzy0w")
var ClapPushUp = Exercise("Clap Push-up", "", "videos/clap_pushup.jpg","Z4kveDWXFXY")
var DeepPushUp = Exercise("Deep Push-up", "", "videos/deep_pushup.jpg","fVn4l726qR8")
var DeclinePushUp = Exercise("Decline Push-up", "", "videos/decline_pushup.jpg","Ecn-b8144f4")
//var SpiderManPushUp = Exercise("Spider Man Push-up", "", "videos/chinup.jpg","pictures/knee_pushup.mp4")

val NegativeDip = Exercise("Negative Dip", "10-12", "videos/negative_dip.jpg","dCGvbyghy6Q")
val Dip = Exercise("Dip", "10-12", "videos/dip.jpg","rasImHWoEEY")
val StraightBarDip = Exercise("Straight Bar Dip", "10-12", "videos/straight_bar_dip.jpg","UcZUtVdVgNM")
val CloseStraightBarDip = Exercise("Close Straight Bar Dip", "10-12", "videos/close_straight_bar_dip.jpg","nVa34yMTM14")
//val FastDip= Exercise("Fast dip", "10-12", "videos/chinup.jpg","pictures/knee_pushup.mp4")
//val SlowDip= Exercise("Slow dip", "10", "videos/chinup.jpg","pictures/knee_pushup.mp4")
val BenchDip= Exercise("Bench dip", "10-12", "videos/bench_dip.jpg","A4IKDZ-nKN4")
val ExplosiveDip = Exercise("Explosive dip", "10", "videos/explosive_dip.jpg","Fu6__M4sy2w")
val SkullcrusherLvl1 = Exercise("Skullcrusher", "10-12", "videos/skullcrusher_lvl1.jpg","ge1sogHHaA4")
val SkullcrusherLvl2 = Exercise("Skullcrusher", "10-12", "videos/skullcrusher_lvl2.jpg","1o0xeY3azrQ")
val SkullcrusherLvl3 = Exercise("Skullcrusher", "10-12", "videos/skullcrusher_lvl3.jpg","pHvSOSS6g7o")
val TricepExtension = Exercise("Tricep extension", "10-12", "videos/tricep_extension.jpg","vCSXLU70LOA")

//Pull exercises
val PullUp = Exercise("Pull-up", "", "videos/pullup.jpg","JCdN6LHp9Hk")
val CloseGripPullUp = Exercise("Close Grip Pull-up", "", "videos/close_grip_pullup.jpg","Utb9EE1OG0g")
val WidePullUp = Exercise("Wide Pull-up", "", "videos/wide_pullup.jpg","W9K0bcERojM")
val ChestTOBarPullUp = Exercise("Chest-to-bar Pull-up", "", "videos/chest_to_bar_pullup.jpg","B_twBiErInw")
val WaistTOBarPullUp = Exercise("Waist-to-bar Pull-up", "", "videos/waist_to_bar_pullup.jpg","oY8TLjjRT4c")
val ExplosivePullUp = Exercise("Explosive Pull-up", "", "videos/explosive_pullup.jpg","nRckMblmSzU")
val ArcherPullUp = Exercise("Archer Pull-up", "", "videos/archer_pullup.jpg","qR5SG9rxMLc")
val ArcherPullUpHold = Exercise("Archer Pull-up hold", "", "videos/archer_pullup_hold.jpg","2_tzpHJZJlo")
val TypewriterPullUp = Exercise("Typewriter Pull-up", "", "videos/typewriter_pullup.jpg","jz_B-fcXRYs")
val CommandoPullUp = Exercise("Commando Pull-up", "", "videos/commando_pullup.jpg","rH61-iSHGXM")
val OneArmPullUp = Exercise("One Arm Pull-up", "", "videos/oapu.jpg","CZCHbxY-lwE")
val OneArmChinUp = Exercise("One Arm Chin-up", "", "videos/oachu.jpg","jCs1zCXUuiA")
val NegativeOaPullUp = Exercise("Negative One Arm Pull-up", "", "videos/oapu.jpg","lNi2XgRN6MQ")
//val OaPullUpHold = Exercise("One Arm Pull-up hold", "", "videos/chinup.jpg","pictures/knee_pushup.mp4")
val OaChinUpHold = Exercise("One Arm Chin-up hold", "", "videos/one_arm_chinup_hold.jpg","zN4v29p0K3I")
val ChinUp = Exercise("Chin-up", "", "videos/chinup.jpg","hwJglrHYi9Q")
val CloseGripChinUp = Exercise("Close Grip Chin-up", "", "videos/close_grip_chinup.jpg","rFrbfWnhbhU")
val WideChinUp = Exercise("Wide Chin-up", "", "videos/wide_chinup.jpg","Sm0lfIdQcv4")
val AustralianPullUp = Exercise("Australian Pull-up", "10-12", "videos/australian_pullup.jpg","H_mUP0YELL0")
val WideAustralianPullUp = Exercise("Wide Australian Pull-up", "10-12", "videos/wide_australian_pullup.jpg","3RIabYjO8bM")
val AustralianChinUp = Exercise("Australian Chin-up", "10-12", "videos/australian_chinup.jpg","2VAdY86njcE")
val WideAustralianChinUp = Exercise("Wide Australian Chin-up", "10-12", "videos/wide_australian_chinup.jpg","giqOwgUQQPA")
val ArcherAustralianPullUp = Exercise("Archer Australian Pull-up", "10-12", "videos/archer_australian_pullup.jpg","Ch7tKQHO3WE")
val OneArmAustralianPullUp = Exercise("One Arm Australian Pull-up", "10-12", "videos/one_arm_australian_pullup.jpg","mWo_RFTqX3I")
val BarCurl = Exercise("Bar Curl", "", "videos/bar_curl.jpg","t6-exxGzZhI")
val SemiHephesto = Exercise("Semi Hephesto", "", "videos/semi_hephesto.jpg","KeOU5D2MfqE")
val HeadBanger = Exercise("Headbanger", "", "videos/head_banger.jpg","KGtV50TQY1o")
val LSitPullUp = Exercise("L-Sit Pull-up", "", "videos/l_sit_pullup.jpg","vki5A0r4uEU")
val HighLSitPullUp = Exercise("High L-Sit Pull-up", "", "videos/high_l_sit_pullup.jpg","nZYF-X4MMS0")
val JumpingPullUp = Exercise("Jumping Pull-up", "10-12", "videos/jumping_pullup.jpg","TAhW6XH5W_4")
val JumpingChinUp = Exercise("Jumping Chin-up", "10-12", "videos/jumping_chinup.jpg","6qmi2JvKeFA")
val NegativePullUp = Exercise("Negative Pull-up", "", "videos/negative_pullup.jpg","akt4OTAXiIE")
val NegativeChinUp = Exercise("Negative Chin-up", "", "videos/negative_chinup.jpg","cdYr3qy74Yk")
val PullUpHold = Exercise("Pull-up hold", "", "videos/pullup_hold.jpg","ogJZ-Lx7aU8")
val ChinUpHold = Exercise("Chin-up hold", "", "videos/chinup.jpg","XbvnmkTAztM")


//Leg exercises
val Squat = Exercise("Squat", "15-20", "videos/sq.jpg","LedMSx93a9I")
val WideSquat = Exercise("Wide Squat", "15-20", "videos/wide_sq.jpg","hdTDzoAaaXY")
val NarrowSquat = Exercise("Narrow Squat", "15-20", "videos/narrow_sq.jpg","4zg76P_If6U")
val SemiSquat = Exercise("Semi Squat", "15-20", "videos/semi_squat.jpg","9jCgRPBT2Uw")
val JumpSquat = Exercise("Jump Squat", "15-20", "videos/sq.jpg","4fRIs4PKmLk")
val SissySquat = Exercise("Sissy Squat", "15-20", "videos/sissy_sq.jpg","kETYyFo2W10")
val PistolSquat = Exercise("Pistol Squat", "15-20", "videos/pistol_sq.jpg","xWB-UMvwDv4")
val SemiPistolSquatLvl1 = Exercise("Semi Pistol Squat Lvl1", "15-20", "videos/semi_pistol_squat_lvl1.jpg","9D3bkGEcHBQ")
val SemiPistolSquatLvl2 = Exercise("Semi Pistol Squat Lvl2", "15-20", "videos/semi_pistol_squat_lvl2.jpg","3rx1GJJ1vuY")
val BulgSplitSquat = Exercise("Bulgarian Split Squat", "", "videos/Bulgarian_split_squat.jpg","Mg792vOIBZg")
val WallSit = Exercise("Wall Sit", "", "videos/wall_sit.jpg","IDx737mAyjY")
val Lunges = Exercise("Lunges", "10-10/side", "videos/lunges.jpg","zxD_EEVMhyA")
val SideLunges = Exercise("Side Lunges", "10-10/side", "videos/side_lunges.jpg","F1TI5WMtkw8")
val BoxJumpsLvl1 = Exercise("Box Jumps Lvl1", "10-10/side", "videos/box_jumps_lvl1.jpg","ughNy3aGrL4")
val BoxStepsLvl1 = Exercise("Box Steps Lvl1", "10-10/side", "videos/box_steps_lvl1.jpg","N72oVr1OHpA")
val BoxJumpsLvl2 = Exercise("Box Jumps Lvl2", "10-10/side", "videos/box_jumps_lvl2.jpg","NDYwuZj-wfE")
val BoxStepsLvl2 = Exercise("Box Steps Lvl2", "10-10/side", "videos/box_steps_lvl2.jpg","wqiPxCJFs3o")
val GluteBridge = Exercise("Glute Bridge", "10-10/side", "videos/glute_bridge.jpg","pYTssbv-_fg")
val SideWalk = Exercise("Side Walk", "10-10/side", "videos/side_walk.jpg","vNMjMliQjsI")
val GluteBridgeHold = Exercise("Glute Bridge Hold", "10-10/side", "videos/glute_bridge.jpg","2ZLE15W08f0")
val SingleLegGluteBridgeHold = Exercise("Single Leg Glute Bridge Hold", "10-10/side", "videos/single_leg_glute_bridge.jpg","rPm3Eti9GXM")
val SingleLegGluteBridge = Exercise("Single LEg Glute Bridge", "10-10/side", "videos/single_leg_glute_bridge.jpg","-xYjj6GgY1I")
val CalfRaise = Exercise("Calf Raises", "10-10/side", "videos/calf_raises.jpg","9QzuzyhyLyI")
val SingleLegCalfRaise = Exercise("Single Leg Calf Raises", "10-10/side", "videos/sing_leg_calf_raises.jpg","q9kIJoUXt6A")
val ElevatedCalfRaise = Exercise("Elevated Calf Raises", "10-10/side", "videos/elevated_calf_raise.jpg","RiVCS90jhIo")
val ElevatedSingleLegCalfRaise = Exercise("Elevated Single Leg Calf Raises", "10-10/side", "videos/elevated_single_leg_calf_raise.jpg","6Bzh2zbMlpM")



//Ab exercises
val KneeRaises = Exercise("Knee Raises", "", "videos/knee_raise.jpg","GYuKQqa6Zws")
val LegRaises = Exercise("Leg Raises", "", "videos/leg_raise.jpg","F4sE4NhZNlo")
val SideKneeRaises = Exercise("Side Knee Raises", "", "videos/side_knee_raise.jpg","ST_WtH_eKX0")
val HighLegRaises = Exercise("High Leg Raises", "", "videos/high_leg_raise.jpg","C8IyVeo2VkI")
val HangingKneeRaises = Exercise("Hanging Knee Raises", "", "videos/hanging_knee_raises.jpg","yu77DNWhOZc")
val HangingSideKneeRaises = Exercise("Hanging Side Knee Raises", "", "videos/hanging_side_knee_raises.jpg","xPmzNG_aGRI")
val HangingLegRaises = Exercise("Hanging Leg Raises", "", "videos/hanging_leg_raises.jpg","Rbx7PMcJIKk")
val ToesToBar = Exercise("Toes To Bar", "", "videos/toes_to_bar.jpg","8FNo6BGEGU8")
val SideToesToBar = Exercise("Side Toes To Bar", "", "videos/side_toes_to_bar.jpg","HdiuMEpsyY4")
val Plank = Exercise("Plank", "1 min", "videos/plank.jpg","pdAMy1uO5bo")
val BoatHold = Exercise("Boat hold", "", "videos/boat_hold.jpg","T4BbyaxQ9V0")
val Clock = Exercise("Clock", "", "videos/clock.jpg","0uEhVug_b3Y")
val SitUp = Exercise("Sit-up", "", "videos/sit_up.jpg","bH_lPEpMPxo")
val LyingLegRaises = Exercise("Lying Leg Raises", "", "videos/lying_leg_raise.jpg","l-cDcnvSoD0")
val L_sit = Exercise("L-sit", "", "videos/l_sit.jpg","EP5uZvyXk78")
val AlternateLegL_sit = Exercise("Alternate Leg L-sit", "", "videos/alternate_l_sit.jpg","ztu2aV-5U7Q")
val Tuck_L_sit = Exercise("Tuck L-sit", "", "videos/tuck_l_sit.jpg","1dJVbAPOFu4")
val HipLift = Exercise("Hip lift", "", "videos/hip_lift.jpg","oy5MEEQOQRs")
val SeatedLegRaises = Exercise("Seated Leg Raises", "", "videos/seated_leg_raise.jpg","eL7K9lEUXU0")
val LegRaisesFromL_sit = Exercise("Leg Raises From L-sit", "", "videos/leg_raise_from_l_sit.jpg","gs_uh0aRCp8")
val V_sit = Exercise("V-sit", "", "videos/v-sit.jpg","xwgPrMGnRDE")
val LyingHyperExtension = Exercise("Lying Hyperextensions", "", "videos/lying_hyperextension.jpg","rd9kmWAMU4k")
val LyingHyperExtensionHold = Exercise("Lying Hyperextensions Hold", "", "videos/lying_hyperextension.jpg","hmGjKxpLp88")




//Front lever
//val L_FLPullUp = Exercise("L Front Lever Pull-up", "", "","pictures/knee_pushup.mp4")
val TuckFLPullUp = Exercise("Tuck Front Lever Pull-up", "", "videos/tuck_fl_pullup.jpg","9mlcBod4PtU")
val AdvTuckFLPullUp = Exercise("Advanced Tuck Front Lever Pull-up", "", "videos/adv_tuck_fl_pullup.jpg","FgnrmT_Bvak")
//val StraddleFLPullUp = Exercise("Straddle Front Lever Pull-up", "", "videos/staddle_fl_pullup.jpg","pictures/staddle_fl_pullup.mp4")
val FullFLPullUp = Exercise("Front Lever Pull-up", "", "videos/flpu.jpg","0MKRw_gD8Ok")
val FullFLRaise = Exercise("Front Lever Raise", "", "videos/full_fl_pullup.jpg","F8wlaaDpNLE")
val StraddleFLRaise = Exercise("Straddle Front Lever Raise", "", "videos/staddle_fl_raise.jpg","pictures/aJfn6m5OGs8")
val OneLegFLRaise = Exercise("One Legged Front Lever Raise", "", "videos/one_leg_fl_raise.jpg","uh7i2pzW8ys")
val AdvTuckFLRaise = Exercise("Advanced Tuck Front Lever Raise", "", "videos/adv_tuck_fl_raise.jpg","Rf9XckPR_UE")
val TuckFLRaise = Exercise("Tuck Front Lever Raise", "", "videos/tuck_fl_raise.jpg","15K5y4j9a-Q")
val FullFLHold = Exercise("Front Lever Hold", "", "videos/full_fl_hold.jpg","MvJSZZz8KcY")
val OneLegFLHold = Exercise("One Legged Front Lever Hold", "", "videos/one_leg_fl_hold.jpg","J_8MjBc8b_o")
val AdvTuckFLHold = Exercise("Advanced Tuck Front Lever Hold", "", "videos/adv_tuck_fl_hold.jpg","Qb-mwD1JRiY")
val TuckFLHold = Exercise("Tuck Front Lever Hold", "", "videos/tuck_fl_hold.jpg","GIja90ShmAM")
val StraddleFLHold = Exercise("Straddle Front Lever Hold", "", "videos/straddle_fl_hold.jpg","VtAo93SutpY")

//Muscle up
val MuscleUp = Exercise("Muscle-up", "", "videos/muscle_up.jpg","WnxvA-ycd10")
//val ExplosiveMuscleUp = Exercise("Explosive Muscle-up", "", "","pictures/KI92MuPVA_g")
val NegativeMuscleUp = Exercise("Negative Muscle-up", "", "videos/negative_muscleup.jpg","_mMysJNTGGU")
//val CloseGripMuscleUp = Exercise("Close Grip Muscle-up", "", "","pictures/knee_pushup.mp4")
//val WideGripMuscleUp = Exercise("Wide Grip Muscle-up", "", "","pictures/knee_pushup.mp4")


val Rest_workout = Workout(
    "Rest Day",
    "",
    "",
    0,"",
    0,
    emptyList(),
    0
)
val Proba_workout = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 3,
    rest= "1-2 min",
    position = 0,
    exercises = listOf(
        WaistTOBarPullUp, FullFLHold, OneArmChinUp, BenchDip, CommandoPullUp,
        ArcherPullUp, ChestTOBarPullUp, OneArmPullUp, PushUp, HighLSitPullUp

    ),
    idetifier = 0
)
val Beginner_Pull_workout1 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PullUpHold.copy(repetitionCount = "10-15 sec"), JumpingChinUp.copy(repetitionCount = "8-10"), AustralianPullUp.copy(repetitionCount = "8-10"),
        ChinUpHold.copy(repetitionCount = "10-15 sec"), JumpingPullUp.copy(repetitionCount = "8-10"), AustralianChinUp.copy(repetitionCount = "8-10")
    ),
    idetifier = 1
)
val Beginner_Pull_workout2 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        JumpingPullUp.copy(repetitionCount = "8-10"), ChinUpHold.copy(repetitionCount = "10-15 sec"), AustralianPullUp.copy(repetitionCount = "10-12"),
        JumpingChinUp.copy(repetitionCount = "8-10"), PullUpHold.copy(repetitionCount = "10-15 sec"), AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 2
)
val Beginner_Pull_workout3 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        NegativePullUp.copy(repetitionCount = "4-5"),ChinUpHold.copy(repetitionCount = "10-15 sec"), JumpingPullUp.copy(repetitionCount = "10-12"),
        AustralianChinUp.copy(repetitionCount = "10-12"),PullUpHold.copy(repetitionCount = "Max"), AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 3
)
val Beginner_Pull_workout4 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        NegativeChinUp.copy(repetitionCount = "4-6"), PullUpHold.copy(repetitionCount = "10-15 sec"), AustralianPullUp.copy(repetitionCount = "10-12"),
        ChinUpHold.copy(repetitionCount = "10-15 sec"), NegativePullUp.copy(repetitionCount = "4-6"), AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 4
)
val Beginner_Pull_workout5 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PullUp.copy(repetitionCount = "6-8"),ChinUpHold.copy(repetitionCount = "15-20 sec"), NegativePullUp.copy(repetitionCount = "5-6"),
            ChinUpHold.copy(repetitionCount = "6-8"), AustralianPullUp.copy(repetitionCount = "10-12"), AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 5
)
val Beginner_Pull_workout6 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        NegativeChinUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "8-10"), NegativeChinUp.copy(repetitionCount = "5-6"),
        PullUpHold.copy(repetitionCount = "15-20 sec"), AustralianPullUp.copy(repetitionCount = "12-15"), AustralianChinUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 6
)
val Beginner_Pull_workout7 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ChinUp.copy(repetitionCount = "8-10"),NegativeChinUp.copy(repetitionCount = "5-6"), ChinUpHold.copy(repetitionCount = "15-20 sec"),
        PullUp.copy(repetitionCount = "6-8"),NegativePullUp.copy(repetitionCount = "4-6"), PullUpHold.copy(repetitionCount = "15-20 sec")
    ),
    idetifier = 7
)
val Beginner_Pull_workout8 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        CloseGripPullUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "6-8"), NegativeChinUp.copy(repetitionCount = "15-20 sec"),
        ChinUpHold.copy(repetitionCount = "10-15 sec"), AustralianPullUp.copy(repetitionCount = "12-15"), AustralianChinUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 8
)
val Beginner_Pull_workout9 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        CloseGripChinUp.copy(repetitionCount = "8-10"), PullUp.copy(repetitionCount = "8-10"), PullUpHold.copy(repetitionCount = "15-20 sec"),
        NegativeChinUp.copy(repetitionCount = "4-6"), ChinUpHold.copy(repetitionCount = "15-20 sec"), WideAustralianPullUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 9
)
val Beginner_Pull_workout10 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PullUp.copy(repetitionCount = "8-10"), PullUpHold.copy(repetitionCount = "15-20 sec"), AustralianPullUp.copy(repetitionCount = "12-15"),
        CloseGripChinUp.copy(repetitionCount = "8-10"), NegativeChinUp.copy(repetitionCount = "4-6"), AustralianChinUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 10
)
val Beginner_Pull_workout11 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "5-6"), CommandoPullUp.copy(repetitionCount = "4-5 each"), PullUp.copy(repetitionCount = "6-8"),
        ChinUpHold.copy(repetitionCount = "15-20 sec"), SemiHephesto.copy(repetitionCount = "6-8"), AustralianChinUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 11
)
val Beginner_Pull_workout12 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        CommandoPullUp.copy(repetitionCount = "4-6 each"), WidePullUp.copy(repetitionCount = "6-8"), PullUpHold.copy(repetitionCount = "15-20 sec"),
        ChinUp.copy(repetitionCount = "6-8"), BarCurl.copy(repetitionCount = "6-8"), WideAustralianPullUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 12
)
val Beginner_Pull_workout13 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WidePullUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "4-5 each"), PullUpHold.copy(repetitionCount = "25-30 sec"),
        ChinUp.copy(repetitionCount = "6-8"), SemiHephesto.copy(repetitionCount = "6-8"), AustralianChinUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 13
)
val Beginner_Pull_workout14 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "6-8"), CommandoPullUp.copy(repetitionCount = "4-5 each"), ChinUp.copy(repetitionCount = "6-8"),
        CloseGripPullUp.copy(repetitionCount = "6-8"), BarCurl.copy(repetitionCount = "6-8"), AustralianPullUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 14
)
val Beginner_Pull_workout15 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WidePullUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "6-8"), CloseGripPullUp.copy(repetitionCount = "6-8"),
        ChinUpHold.copy(repetitionCount = "25-30 sec"), SemiHephesto.copy(repetitionCount = "6-8"), WideAustralianPullUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 15
)
val Beginner_Pull_workout16 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        CommandoPullUp.copy(repetitionCount = "4-5 each"), WideChinUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "6-8"),
        ChinUpHold.copy(repetitionCount = "25-30 sec"), PullUpHold.copy(repetitionCount = "15-20 sec"), BarCurl.copy(repetitionCount = "6-8")
    ),
    idetifier = 16
)
val Beginner_Pull_workout17 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "6-8"), CloseGripPullUp.copy(repetitionCount = "6-8"),
        ChinUp.copy(repetitionCount = "6-8"), BarCurl.copy(repetitionCount = "6-8"), ArcherAustralianPullUp.copy(repetitionCount = "5-6 each")
    ),
    idetifier = 17
)
val Beginner_Pull_workout18 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WideChinUp.copy(repetitionCount = "6-8"), ChinUp.copy(repetitionCount = "6-8"), CloseGripChinUp.copy(repetitionCount = "6-8"),
        ChinUpHold.copy(repetitionCount = "25-30 sec"), WideAustralianChinUp.copy(repetitionCount = "10-12"), AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 18
)
val Beginner_Pull_workout20 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "6-8"), WidePullUp.copy(repetitionCount = "6-8"),
        CloseGripChinUp.copy(repetitionCount = "6-8"), ChinUpHold.copy(repetitionCount = "25-30 sec"), AustralianPullUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 19
)
val Beginner_Pull_workout19 = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WidePullUp.copy(repetitionCount = "6-8"), WideChinUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "6-8"),
        ChinUpHold.copy(repetitionCount = "25-30 sec"), BarCurl.copy(repetitionCount = "6-8"), OneArmAustralianPullUp.copy(repetitionCount = "6-8 each")
    ),
    idetifier = 20
)

val Beginner_Pull_workout1N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ChinUp.copy(repetitionCount = "8-10"),PullUp.copy(repetitionCount = "6-8"),NegativeChinUp.copy(repetitionCount = "4-6"),
        NegativePullUp.copy(repetitionCount = "4-6"), AustralianChinUp.copy(repetitionCount = "10-12"),AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 21
)
val Beginner_Pull_workout2N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        PullUp.copy(repetitionCount = "8-10"), NegativePullUp.copy(repetitionCount = "4-6"), PullUpHold.copy(repetitionCount = "15-20 sec"),
        ChinUp.copy(repetitionCount = "8-10"), ChinUpHold.copy(repetitionCount = "15-20 sec"), WideAustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 22
)
val Beginner_Pull_workout3N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ChinUp.copy(repetitionCount = "8-10"), PullUp.copy(repetitionCount = "8-10"), CloseGripChinUp.copy(repetitionCount = "6-8"),
        NegativePullUp.copy(repetitionCount = "4-5"), WideAustralianChinUp.copy(repetitionCount = "10-12"), AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 23
)
val Beginner_Pull_workout4N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 5,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        PullUp.copy(repetitionCount = "8-10"),CloseGripChinUp.copy(repetitionCount = "8-10"), ChinUp.copy(repetitionCount = "6-8"),
        NegativePullUp.copy(repetitionCount = "5-6"), WideAustralianPullUp.copy(repetitionCount = "10-12"), AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 24
)
val Beginner_Pull_workout5N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 5,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        CloseGripPullUp.copy(repetitionCount = "8-10"), CloseGripChinUp.copy(repetitionCount = "8-10"), PullUp.copy(repetitionCount = "6-8"),
        ChinUpHold.copy(repetitionCount = "15-20 sec"), NegativePullUp.copy(repetitionCount = "5-6"), WideAustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 25
)
val Beginner_Pull_workout6N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 3,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        CommandoPullUp.copy(repetitionCount = "4-5 each"), WidePullUp.copy(repetitionCount = "6-8"), CloseGripChinUp.copy(repetitionCount = "6-8"),
        PullUpHold.copy(repetitionCount = "15-20 sec"), BarCurl.copy(repetitionCount = "6-8"), AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 26
)
val Beginner_Pull_workout7N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "6-8"), ChinUp.copy(repetitionCount = "6-8"),
        ChinUpHold.copy(repetitionCount = "6-8"), SemiHephesto.copy(repetitionCount = "6-8"), WideAustralianPullUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 27
)
val Beginner_Pull_workout8N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        WidePullUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "6-8"), CloseGripChinUp.copy(repetitionCount = "6-8"),
        NegativeChinUp.copy(repetitionCount = "4-5"), BarCurl.copy(repetitionCount = "6-8"), ArcherAustralianPullUp.copy(repetitionCount = "6-8 each")
    ),
    idetifier = 28
)
val Beginner_Pull_workout9N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        CommandoPullUp.copy(repetitionCount = "4-5 each"), WideChinUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "6-8"),
        PullUpHold.copy(repetitionCount = "20-25 sec"), WideAustralianChinUp.copy(repetitionCount = "10-12"), AustralianPullUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 29
)
val Beginner_Pull_workout10N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        WideChinUp.copy(repetitionCount = "6-8"), ExplosivePullUp.copy(repetitionCount = "6-8"), CloseGripChinUp.copy(repetitionCount = "6-8"),
        NegativeChinUp.copy(repetitionCount = "4-5"), BarCurl.copy(repetitionCount = "6-8"), ArcherAustralianPullUp.copy(repetitionCount = "6-8 each")
    ),
    idetifier = 30
)
val Beginner_Pull_workout11N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        CommandoPullUp.copy(repetitionCount = "4-5 each"), WidePullUp.copy(repetitionCount = "6-8"), ChinUp.copy(repetitionCount = "6-8"),
        ChinUpHold.copy(repetitionCount = "20-25 sec"), SemiHephesto.copy(repetitionCount = "6-8"), WideAustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 31
)
val Beginner_Pull_workout12N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "8-10"), ChinUp.copy(repetitionCount = "6-8"),
        ChinUpHold.copy(repetitionCount = "20-25"), BarCurl.copy(repetitionCount = "6-8"), ArcherAustralianPullUp.copy(repetitionCount = "6-8 each")
    ),
    idetifier = 32
)
val Beginner_Pull_workout13N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        WidePullUp.copy(repetitionCount = "6-8"), PullUp.copy(repetitionCount = "8-10"), CloseGripPullUp.copy(repetitionCount = "6-8"),
        PullUpHold.copy(repetitionCount = "20-25 sec"), ArcherPullUpHold.copy(repetitionCount = "6-8 each"), AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 33
)
val Beginner_Pull_workout14N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "6-8"), WideChinUp.copy(repetitionCount = "6-8"), ChinUp.copy(repetitionCount = "6-8"),
        BarCurl.copy(repetitionCount = "6-8"), WideAustralianChinUp.copy(repetitionCount = "10-12"), AustralianChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 34
)
val Beginner_Pull_workout15N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        CommandoPullUp.copy(repetitionCount = "4-5 each"), PullUp.copy(repetitionCount = "6-8"), CloseGripChinUp.copy(repetitionCount = "6-8"),
        PullUpHold.copy(repetitionCount = "4-5"), OneArmAustralianPullUp.copy(repetitionCount = "6-8 each"), SemiHephesto.copy(repetitionCount = "6-8")
    ),
    idetifier = 35
)
val Inter_Pull_workout1N = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "6-8"), WidePullUp.copy(repetitionCount = "6-8"), BarCurl.copy(repetitionCount = "6-8"),
        PullUp.copy(repetitionCount = "8-10"), OneArmAustralianPullUp.copy(repetitionCount = "6-8 each"), SemiHephesto.copy(repetitionCount = "8-10")
    ),
    idetifier = 155
)
val Inter_Pull_workout2N = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        CommandoPullUp.copy(repetitionCount = "5-6 each"), ChinUp.copy(repetitionCount = "8"), PullUp.copy(repetitionCount = "8"),
        ChinUpHold.copy(repetitionCount = "30 sec"), OneArmAustralianPullUp.copy(repetitionCount = "6-8 each"), CloseGripPullUp.copy(repetitionCount = "6-8")
    ),
    idetifier = 156
)
val Inter_Pull_workout3N = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        LSitPullUp.copy(repetitionCount = "8"), WidePullUp.copy(repetitionCount = "8"), ChinUp.copy(repetitionCount = "8-10"),
        PullUp.copy(repetitionCount = "10"), ArcherAustralianPullUp.copy(repetitionCount = "8-10 each"), ChinUpHold.copy(repetitionCount = "25-30 sec")
    ),
    idetifier = 157
)
val Inter_Pull_workout4N = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ArcherPullUp.copy(repetitionCount = "4-6 each"), ExplosivePullUp.copy(repetitionCount = "8-10"), PullUp.copy(repetitionCount = "10"),
        CloseGripChinUp.copy(repetitionCount = "10"), OneArmAustralianPullUp.copy(repetitionCount = "8 each"), SemiHephesto.copy(repetitionCount = "8-10")
    ),
    idetifier = 158
)
val Inter_Pull_workout5N = Workout(
    name = "Pull Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ChestTOBarPullUp.copy(repetitionCount = "8-10"), WidePullUp.copy(repetitionCount = "10"), LSitPullUp.copy(repetitionCount = "8-10"),
        PullUp.copy(repetitionCount = "10"), BarCurl.copy(repetitionCount = "8-10"), ChinUpHold.copy(repetitionCount = "30-35 sec")
    ),
    idetifier = 159
)


val Inter_Pull_workout1 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        CommandoPullUp.copy(repetitionCount = "6-8 each"), CommandoPullUp.copy(repetitionCount = "6-8 each"), ChinUp.copy(repetitionCount = "8-10"),
        CloseGripChinUp.copy(repetitionCount = "8-10"), PullUpHold.copy(repetitionCount = "20-25 sec"), ArcherAustralianPullUp.copy(repetitionCount = "8-10 each")
    ),
    idetifier = 36
)
val Inter_Pull_workout2 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WidePullUp.copy(repetitionCount = "6-8"), CloseGripChinUp.copy(repetitionCount = "8-10"), PullUp.copy(repetitionCount = "8-10"),
        ChinUp.copy(repetitionCount = "8-10"), CloseGripPullUp.copy(repetitionCount = "8-10"), ArcherAustralianPullUp.copy(repetitionCount = "8-10 each")
    ),
    idetifier = 37
)
val Inter_Pull_workout3 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "6-8"), WideChinUp.copy(repetitionCount = "6-8"), CommandoPullUp.copy(repetitionCount = "6-8 each"),
        CloseGripChinUp.copy(repetitionCount = "8-10"), PullUp.copy(repetitionCount = "8-10 sec"), BarCurl.copy(repetitionCount = "6-8")
    ),
    idetifier = 38
)
val Inter_Pull_workout4 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WidePullUp.copy(repetitionCount = "6-8"), CloseGripChinUp.copy(repetitionCount = "8-10"), PullUp.copy(repetitionCount = "8-10"),
        ChinUp.copy(repetitionCount = "8-10"), PullUpHold.copy(repetitionCount = "20-25 sec"), SemiHephesto.copy(repetitionCount = "8-10")
    ),
    idetifier = 39
)
val Inter_Pull_workout5 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        LSitPullUp.copy(repetitionCount = "6-8"), CommandoPullUp.copy(repetitionCount = "6-8 each"), WideChinUp.copy(repetitionCount = "6-8"),
        PullUp.copy(repetitionCount = "8-10"), ChinUpHold.copy(repetitionCount = "20-25 sec"), ArcherAustralianPullUp.copy(repetitionCount = "8-10 each")
    ),
    idetifier = 40
)
val Inter_Pull_workout6 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "8-10"), CommandoPullUp.copy(repetitionCount = "6-8 each"), HeadBanger.copy(repetitionCount = "8-10"),
        WidePullUp.copy(repetitionCount = "6-8"), CloseGripChinUp.copy(repetitionCount = "8-10"), ArcherAustralianPullUp.copy(repetitionCount = "8-10 each")
    ),
    idetifier = 41
)
val Inter_Pull_workout7 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        LSitPullUp.copy(repetitionCount = "6-8"), ExplosivePullUp.copy(repetitionCount = "8-10"), PullUp.copy(repetitionCount = "8-10"),
        ChinUp.copy(repetitionCount = "8-10"), ChinUpHold.copy(repetitionCount = "20-25 sec"), BarCurl.copy(repetitionCount = "8-10")
    ),
    idetifier = 42
)
val Inter_Pull_workout8 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ChestTOBarPullUp.copy(repetitionCount = "6-8"), CommandoPullUp.copy(repetitionCount = "6-8 each"), ExplosivePullUp.copy(repetitionCount = "8-10"),
        WideChinUp.copy(repetitionCount = "8-10"), CloseGripChinUp.copy(repetitionCount = "8-10"), SemiHephesto.copy(repetitionCount = "8-10")
    ),
    idetifier = 43
)
val Inter_Pull_workout9 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount = "8-10"), CommandoPullUp.copy(repetitionCount = "6-8 each"), ChinUp.copy(repetitionCount = "8-10"),
        PullUp.copy(repetitionCount = "8-10"), ChinUpHold.copy(repetitionCount = "20-25 sec"), BarCurl.copy(repetitionCount = "8-10")
    ),
    idetifier = 44
)
val Inter_Pull_workout10 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ChestTOBarPullUp.copy(repetitionCount = "6-8"), HeadBanger.copy(repetitionCount = "8-10"), LSitPullUp.copy(repetitionCount = "6-8"),
        WidePullUp.copy(repetitionCount = "8-10"), CloseGripChinUp.copy(repetitionCount = "8-10"), ArcherAustralianPullUp.copy(repetitionCount = "8-10 each")
    ),
    idetifier = 45
)
val Inter_Pull_workout11 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ArcherPullUp.copy(repetitionCount = "4-6 each"), LSitPullUp.copy(repetitionCount = "8-10"), ChestTOBarPullUp.copy(repetitionCount = "6-8"),
        WideChinUp.copy(repetitionCount = "8-10"), CommandoPullUp.copy(repetitionCount = "6-8"), ChinUp.copy(repetitionCount = "8-10")
    ),
    idetifier = 46
)
val Inter_Pull_workout12 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ChestTOBarPullUp.copy(repetitionCount = "8-10"), ExplosivePullUp.copy(repetitionCount = "8-10"), ArcherPullUpHold.copy(repetitionCount = "15-20 sec each"),
        WidePullUp.copy(repetitionCount = "8-10"), CloseGripChinUp.copy(repetitionCount = "8-10"), BarCurl.copy(repetitionCount = "8-10")
    ),
    idetifier = 47
)
val Inter_Pull_workout13 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ArcherPullUp.copy(repetitionCount = "4-6 each"), HeadBanger.copy(repetitionCount = "8-10"), LSitPullUp.copy(repetitionCount = "8-10"),
        ExplosivePullUp.copy(repetitionCount = "8-10"), WideChinUp.copy(repetitionCount = "8-10"), ArcherAustralianPullUp.copy(repetitionCount = "8-10 each")
    ),
    idetifier = 48
)
val Inter_Pull_workout14 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        LSitPullUp.copy(repetitionCount = "8-10"), WidePullUp.copy(repetitionCount = "8-10"), ExplosivePullUp.copy(repetitionCount = "8-10"),
        CommandoPullUp.copy(repetitionCount = "6-8 each"), CloseGripChinUp.copy(repetitionCount = "8-10"), SemiHephesto.copy(repetitionCount = "8-10")
    ),
    idetifier = 49
)
val Inter_Pull_workout15 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        TypewriterPullUp.copy(repetitionCount = "4-5 each"), LSitPullUp.copy(repetitionCount = "8-10"), CommandoPullUp.copy(repetitionCount = "6-8 each"),
        PullUp.copy(repetitionCount = "8-10"), CloseGripChinUp.copy(repetitionCount = "8-10"), OneArmAustralianPullUp.copy(repetitionCount = "6-8 each")
    ),
    idetifier = 50
)
val Inter_Pull_workout16 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ChestTOBarPullUp.copy(repetitionCount = "8-10"), ArcherPullUpHold.copy(repetitionCount = "15-20 sec each"), LSitPullUp.copy(repetitionCount = "8-10"),
        WideChinUp.copy(repetitionCount = "8-10"), PullUpHold.copy(repetitionCount = "25-30 sec"), ArcherAustralianPullUp.copy(repetitionCount = "8-10 each")
    ),
    idetifier = 51
)
val Inter_Pull_workout17 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        NegativeOaPullUp.copy(repetitionCount = "3-4 each"), ChestTOBarPullUp.copy(repetitionCount = "8-10"), LSitPullUp.copy(repetitionCount = "6-8"),
        ChinUp.copy(repetitionCount = "8-10"), CommandoPullUp.copy(repetitionCount = "6-8 each"), OneArmAustralianPullUp.copy(repetitionCount = "8-10 each")
    ),
    idetifier = 52
)
val Inter_Pull_workout18 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ArcherPullUp.copy(repetitionCount = "5-6 each"), HeadBanger.copy(repetitionCount = "8-10"), CommandoPullUp.copy(repetitionCount = "6-8 each"),
        PullUp.copy(repetitionCount = "8-10"), ChinUpHold.copy(repetitionCount = "30-35 sec"), WideAustralianPullUp.copy(repetitionCount = "20-25")
    ),
    idetifier = 53
)
val Inter_Pull_workout19 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        LSitPullUp.copy(repetitionCount = "8-10"), TypewriterPullUp.copy(repetitionCount = "4-5 each"), ChestTOBarPullUp.copy(repetitionCount = "8-10"),
        WidePullUp.copy(repetitionCount = "8-10"), CloseGripChinUp.copy(repetitionCount = "8-10"), ArcherAustralianPullUp.copy(repetitionCount = "8-10 each")
    ),
    idetifier = 54
)
val Inter_Pull_workout20 = Workout(
    name = "Pull Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ChestTOBarPullUp.copy(repetitionCount = "10"), NegativeOaPullUp.copy(repetitionCount = "3-4 each"), PullUp.copy(repetitionCount = "10"),
        WideChinUp.copy(repetitionCount = "8-10"), CommandoPullUp.copy(repetitionCount = "6-8 each"), OneArmAustralianPullUp.copy(repetitionCount = "8-10 each")
    ),
    idetifier = 55
)
val Advanced_Pull_workout1N = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ArcherPullUp.copy(repetitionCount = "5 each"), OaChinUpHold.copy(repetitionCount = "20-25 sec each"), LSitPullUp.copy(repetitionCount = "10"),
        WideChinUp.copy(repetitionCount = "10"), ChinUpHold.copy(repetitionCount = "45 sec"), PullUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 226
)
val Advanced_Pull_workout2N = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        TypewriterPullUp.copy(repetitionCount = "5 each"), ChestTOBarPullUp.copy(repetitionCount = "10"), LSitPullUp.copy(repetitionCount = "10-12"),
        ExplosivePullUp.copy(repetitionCount = "10-12"), PullUp.copy(repetitionCount = "10-12"), CloseGripChinUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 227
)
val Advanced_Pull_workout3N = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        OneArmPullUp.copy(repetitionCount = "max each"), ChestTOBarPullUp.copy(repetitionCount = "10-12"), ExplosivePullUp.copy(repetitionCount = "12"),
        LSitPullUp.copy(repetitionCount = "10-12"), WidePullUp.copy(repetitionCount = "12"), BarCurl.copy(repetitionCount = "10")
    ),
    idetifier = 228
)
val Advanced_Pull_workout4N = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        OneArmPullUp.copy(repetitionCount = "max each"), HighLSitPullUp.copy(repetitionCount = "max"), WaistTOBarPullUp.copy(repetitionCount = "max"),
        ExplosivePullUp.copy(repetitionCount = "12"), ArcherPullUp.copy(repetitionCount = "5-6 each"),
        LSitPullUp.copy(repetitionCount = "10-12"), WidePullUp.copy(repetitionCount = "12"), BarCurl.copy(repetitionCount = "10")
    ),
    idetifier = 229
)
val Advanced_Pull_workout5N = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        HighLSitPullUp.copy(repetitionCount = "5"), OneArmPullUp.copy(repetitionCount = "5 each"), WaistTOBarPullUp.copy(repetitionCount = "5"),
        TypewriterPullUp.copy(repetitionCount = "5-6 each"), WideChinUp.copy(repetitionCount = "12"),
        LSitPullUp.copy(repetitionCount = "12"), WidePullUp.copy(repetitionCount = "12"), PullUp.copy(repetitionCount = "12")
    ),
    idetifier = 230
)
val Advanced_Pull_workout1 = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        LSitPullUp.copy(repetitionCount = "8-10"), ArcherPullUp.copy(repetitionCount = "4-5 each"), PullUp.copy(repetitionCount = "10"),
        WideChinUp.copy(repetitionCount = "8-10"), ChinUpHold.copy(repetitionCount = "45 sec"), CloseGripPullUp.copy(repetitionCount = "10")
    ),
    idetifier = 188
)
val Advanced_Pull_workout2 = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        TypewriterPullUp.copy(repetitionCount = "5-6 each"), WidePullUp.copy(repetitionCount = "10"), LSitPullUp.copy(repetitionCount = "10"),
        OaChinUpHold.copy(repetitionCount = "20 sec each"), CommandoPullUp.copy(repetitionCount = "6 each"), BarCurl.copy(repetitionCount = "10-12")
    ),
    idetifier = 189
)
val Advanced_Pull_workout3 = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WidePullUp.copy(repetitionCount = "12"), PullUp.copy(repetitionCount = "12"), LSitPullUp.copy(repetitionCount = "12"),
        WideChinUp.copy(repetitionCount = "12"), ChinUp.copy(repetitionCount = "12"), CloseGripChinUp.copy(repetitionCount = "12")
    ),
    idetifier = 190
)
val Advanced_Pull_workout4 = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ArcherPullUpHold.copy(repetitionCount = "20-25 sec each"), ChestTOBarPullUp.copy(repetitionCount = "10-12"), LSitPullUp.copy(repetitionCount = "10-12"),
        ChinUp.copy(repetitionCount = "12"), ChinUpHold.copy(repetitionCount = "35-40 sec"), OneArmAustralianPullUp.copy(repetitionCount = "10 each")
    ),
    idetifier = 191
)
val Advanced_Pull_workout5 = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        OneArmPullUp.copy(repetitionCount = "max each"), WidePullUp.copy(repetitionCount = "12"), ExplosivePullUp.copy(repetitionCount = "12"),
        OaChinUpHold.copy(repetitionCount = "20-25 sec each"), PullUp.copy(repetitionCount = "12"), HeadBanger.copy(repetitionCount = "12")
    ),
    idetifier = 192
)
val Advanced_Pull_workout6 = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        TypewriterPullUp.copy(repetitionCount = "5-6 each"), LSitPullUp.copy(repetitionCount = "12"), ChestTOBarPullUp.copy(repetitionCount = "12"),
        OaChinUpHold.copy(repetitionCount = "20-25 sec each"), PullUp.copy(repetitionCount = "12-15"), BarCurl.copy(repetitionCount = "12")
    ),
    idetifier = 193
)
val Advanced_Pull_workout7 = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        OneArmPullUp.copy(repetitionCount = "max each"), ArcherPullUp.copy(repetitionCount = "max each"), PullUp.copy(repetitionCount = "12"),
        CloseGripChinUp.copy(repetitionCount = "12"), ExplosivePullUp.copy(repetitionCount = "12"), ChinUpHold.copy(repetitionCount = "40-45 sec")
    ),
    idetifier = 194
)
val Advanced_Pull_workout8 = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        HighLSitPullUp.copy(repetitionCount = "max"), LSitPullUp.copy(repetitionCount = "max"), MuscleUp.copy(repetitionCount = "10"),
        ChinUp.copy(repetitionCount = "12"), PullUp.copy(repetitionCount = "12"), HeadBanger.copy(repetitionCount = "12")
    ),
    idetifier = 195
)
val Advanced_Pull_workout9 = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        OneArmPullUp.copy(repetitionCount = "5-6 each"), WaistTOBarPullUp.copy(repetitionCount = "5-6"), WidePullUp.copy(repetitionCount = "12"),
        ChestTOBarPullUp.copy(repetitionCount = "12"), ChinUp.copy(repetitionCount = "12"), ChinUpHold.copy(repetitionCount = "40-45 sec")
    ),
    idetifier = 196
)
val Advanced_Pull_workout10 = Workout(
    name = "Pull Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        OneArmPullUp.copy(repetitionCount = "6 each"), HighLSitPullUp.copy(repetitionCount = "5"), ChestTOBarPullUp.copy(repetitionCount = "12"),
        CommandoPullUp.copy(repetitionCount = "6-8 each"), ArcherPullUpHold.copy(repetitionCount = "25-30 sec each"), PullUp.copy(repetitionCount = "12-15"),
        BarCurl.copy(repetitionCount = "12-15")
    ),
    idetifier = 197
)

val Beginner_Push_workout1N = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        Dip.copy(repetitionCount = "8-10"), PushUp.copy(repetitionCount = "10-12"), DiamondPushUp.copy(repetitionCount = "10-12"),
        InclinePushUp.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15"), KneePushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 56
)
val Beginner_Push_workout2N = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ExplosivePushUp.copy(repetitionCount = "8-10"), Dip.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "10-12"),
        SkullcrusherLvl2.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "12-15"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 57
)
val Beginner_Push_workout3N = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        Dip.copy(repetitionCount = "10-12"), ExplosivePushUp.copy(repetitionCount = "10-12"), TricepExtension.copy(repetitionCount = "10-12"),
        PushUp.copy(repetitionCount = "10-12"), DiamondPushUp.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "12")
    ),
    idetifier = 58
)
val Beginner_Push_workout4N = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ExplosivePushUp.copy(repetitionCount = "8-10"), Dip.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "10-12"),
        TricepExtension.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 59
)
val Beginner_Push_workout5N = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        Dip.copy(repetitionCount = "10-12"), ExplosivePushUp.copy(repetitionCount = "10-12"), SkullcrusherLvl2.copy(repetitionCount = "10-12"),
        PushUp.copy(repetitionCount = "10-12"), DiamondPushUp.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 60
)
val Beginner_Push_workout6N = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        DeclinePushUp.copy(repetitionCount = "8-10"), Dip.copy(repetitionCount = "10-12"), TricepExtension.copy(repetitionCount = "10-12"),
        PushUp.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15"), InclinePushUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 148
)
val Beginner_Push_workout7N = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ExplosivePushUp.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "12"),
        Dip.copy(repetitionCount = "10-12"), TricepExtension.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 149
)
val Beginner_Push_workout8N = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ExplosivePushUp.copy(repetitionCount = "8"), StraightBarDip.copy(repetitionCount = "10-12"), DeclinePushUp.copy(repetitionCount = "10-12"),
        SkullcrusherLvl2.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "12-15"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 150
)
val Beginner_Push_workout9N = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        StraightBarDip.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "12"), InclinePushUp.copy(repetitionCount = "12"),
        Dip.copy(repetitionCount = "10-12"), DiamondPushUp.copy(repetitionCount = "10-12"), TricepExtension.copy(repetitionCount = "10-12")
    ),
    idetifier = 151
)
val Beginner_Push_workout10N = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 5,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        Dip.copy(repetitionCount = "12"), ExplosivePushUp.copy(repetitionCount = "12"), DiamondPushUp.copy(repetitionCount = "10-12"),
        DeclinePushUp.copy(repetitionCount = "10-12"), TricepExtension.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "12")
    ),
    idetifier = 152
)
val Beginner_Push_workout1 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        InclinePushUp.copy(repetitionCount = "8-10"), NegativeDip.copy(repetitionCount = "4-6"), KneePushUp.copy(repetitionCount = "8-10"),
        BenchDip.copy(repetitionCount = "8-10"), NegativePushUp.copy(repetitionCount = "4-6"), SkullcrusherLvl1.copy(repetitionCount = "8-10")
    ),
    idetifier = 61
)
val Beginner_Push_workout2 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        KneePushUp.copy(repetitionCount = "8-10"), BenchDip.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "10-12"),
        NegativeDip.copy(repetitionCount = "5-6"), NegativePushUp.copy(repetitionCount = "5-6"), SkullcrusherLvl1.copy(repetitionCount = "12")
    ),
    idetifier = 62
)
val Beginner_Push_workout3 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PushUp.copy(repetitionCount = "8-10"),SkullcrusherLvl2.copy(repetitionCount = "8-10"), InclinePushUp.copy(repetitionCount = "10-12"), NegativeDip.copy(repetitionCount = "5-6"), KneePushUp.copy(repetitionCount = "10-12"),
        BenchDip.copy(repetitionCount = "10-12")
    ),
    idetifier = 63
)
val Beginner_Push_workout4 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PushUp.copy(repetitionCount = "8-10"), SkullcrusherLvl2.copy(repetitionCount = "8-10"), NegativePushUp.copy(repetitionCount = "6-8"),
        BenchDip.copy(repetitionCount = "12"), InclinePushUp.copy(repetitionCount = "10-12"), SkullcrusherLvl1.copy(repetitionCount = "12-15")
    ),
    idetifier = 64
)
val Beginner_Push_workout5 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        DiamondPushUp.copy(repetitionCount = "8-10"), InclinePushUp.copy(repetitionCount = "10-12"), SkullcrusherLvl2.copy(repetitionCount = "8-10"),
        PushUp.copy(repetitionCount = "10-12"),BenchDip.copy(repetitionCount = "12-15"), NegativePushUp.copy(repetitionCount = "5-6")
    ),
    idetifier = 65
)
val Beginner_Push_workout6 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Dip.copy(repetitionCount = "8-10"), PushUp.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15"),
        InclinePushUp.copy(repetitionCount = "10-12"), DiamondPushUp.copy(repetitionCount = "10-12"), KneePushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 66
)
val Beginner_Push_workout7 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePushUp.copy(repetitionCount = "8-10"), SkullcrusherLvl2.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "10-12"),
        TricepExtension.copy(repetitionCount = "8-10"), InclinePushUp.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 67
)
val Beginner_Push_workout8 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Dip.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "10-12"), SkullcrusherLvl2.copy(repetitionCount = "10-12"),
         InclinePushUp.copy(repetitionCount = "10-12"), TricepExtension.copy(repetitionCount = "8-10"), PushUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 68
)
val Beginner_Push_workout9 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePushUp.copy(repetitionCount = "8-10"), Dip.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "10-12"),
        TricepExtension.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 69
)
val Beginner_Push_workout10 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Dip.copy(repetitionCount = "10-12"), ExplosivePushUp.copy(repetitionCount = "10-12"), SkullcrusherLvl2.copy(repetitionCount = "10-12"),
        InclinePushUp.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15"), PushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 70
)
val Beginner_Push_workout11 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        DeclinePushUp.copy(repetitionCount = "10-12"), TricepExtension.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "10-12"),
        InclinePushUp.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15"), PushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 138
)
val Beginner_Push_workout12 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        DeepPushUp.copy(repetitionCount = "10-12"), SkullcrusherLvl2.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "10-12"),
        DiamondPushUp.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 139
)
val Beginner_Push_workout13 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PushUp.copy(repetitionCount = "12-15"), DiamondPushUp.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "10-12"),
        PushUpHold.copy(repetitionCount = "20-30 sec"), TricepExtension.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 140
)
val Beginner_Push_workout14 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        DeclinePushUp.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "10-12"),
        Dip.copy(repetitionCount = "10-12"), NegativeDip.copy(repetitionCount = "6-8"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 141
)
val Beginner_Push_workout15 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PushUp.copy(repetitionCount = "10-12"), StraightBarDip.copy(repetitionCount = "8-10"), DiamondPushUp.copy(repetitionCount = "10-12"),
        Dip.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12-15"), InclinePushUp.copy(repetitionCount = "12")
    ),
    idetifier = 142
)
val Beginner_Push_workout16 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePushUp.copy(repetitionCount = "8"), Dip.copy(repetitionCount = "10-12"), DeclinePushUp.copy(repetitionCount = "10-12"),
        SkullcrusherLvl2.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "12-15"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 143
)
val Beginner_Push_workout17 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        StraightBarDip.copy(repetitionCount = "10-12"), DiamondPushUp.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "10-12"),
        TricepExtension.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "10-12")
    ),
    idetifier = 144
)
val Beginner_Push_workout18 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ExplosivePushUp.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "10-12"), DeclinePushUp.copy(repetitionCount = "10-12"),
        SkullcrusherLvl2.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "12-15"), BenchDip.copy(repetitionCount = "12-15")
    ),
    idetifier = 145
)
val Beginner_Push_workout19 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        DeclinePushUp.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "10-12"), InclinePushUp.copy(repetitionCount = "10-12"),
        DiamondPushUp.copy(repetitionCount = "10-12"), SkullcrusherLvl2.copy(repetitionCount = "12-15"), TricepExtension.copy(repetitionCount = "12-15")
    ),
    idetifier = 146
)
val Beginner_Push_workout20 = Workout(
    name = "Push Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        StraightBarDip.copy(repetitionCount = "10-12"), DeclinePushUp.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "10-12"),
        PushUp.copy(repetitionCount = "10-12"), TricepExtension.copy(repetitionCount = "12-15"), InclinePushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 147
)
val Inter_Push_workout1 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Dip.copy(repetitionCount = "10-12"), DeepPushUp.copy(repetitionCount = "10-12"), SkullcrusherLvl2.copy(repetitionCount = "12"),
        ExplosivePushUp.copy(repetitionCount = "12"), DiamondPushUp.copy(repetitionCount = "12"), InclinePushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 204
)
val Inter_Push_workout2 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        StraightBarDip.copy(repetitionCount = "12"), ClapPushUp.copy(repetitionCount = "8-10"), TricepExtension.copy(repetitionCount = "12"),
        DeclinePushUp.copy(repetitionCount = "12"), DiamondPushUp.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 205
)
val Inter_Push_workout3 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        DeepPushUp.copy(repetitionCount = "12"), Dip.copy(repetitionCount = "10-12"), TricepExtension.copy(repetitionCount = "12"),
        PushUpHold.copy(repetitionCount = "30 sec"), SkullcrusherLvl2.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 206
)
val Inter_Push_workout4 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Dip.copy(repetitionCount = "12-15"), DeclinePushUp.copy(repetitionCount = "12"), DiamondPushUp.copy(repetitionCount = "12"),
        ExplosivePushUp.copy(repetitionCount = "12"), TricepExtension.copy(repetitionCount = "12"), InclinePushUp.copy(repetitionCount = "15")
    ),
    idetifier = 207
)
val Inter_Push_workout5 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        StraightBarDip.copy(repetitionCount = "12"), TricepExtension.copy(repetitionCount = "12"), ClapPushUp.copy(repetitionCount = "10"),
        ExplosivePushUp.copy(repetitionCount = "10"), DiamondPushUp.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "15")
    ),
    idetifier = 208
)
val Inter_Push_workout6 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PushUpHold.copy(repetitionCount = "35-40 sec"), PseudoPlanchePushUp.copy(repetitionCount = "8-10"), Dip.copy(repetitionCount = "10"),
        HinduPushUp.copy(repetitionCount = "10"), DeclinePushUp.copy(repetitionCount = "10"), InclinePushUp.copy(repetitionCount = "12")
    ),
    idetifier = 209
)
val Inter_Push_workout7 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ArcherPushUp.copy(repetitionCount = "5-6 each"), DiamondPushUp.copy(repetitionCount = "12"), StraightBarDip.copy(repetitionCount = "12"),
        ExplosivePushUp.copy(repetitionCount = "12"), SkullcrusherLvl2.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "12")
    ),
    idetifier = 210
)
val Inter_Push_workout8 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PikePushUp.copy(repetitionCount = "10"), DeclinePushUp.copy(repetitionCount = "10-12"), StraightBarDip.copy(repetitionCount = "10-12"),
        PseudoPlanchePushUp.copy(repetitionCount = "10"), ExplosivePushUp.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "12")
    ),
    idetifier = 211
)
val Inter_Push_workout9 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ArcherPushUp.copy(repetitionCount = "6 each"), TricepExtension.copy(repetitionCount = "12"), ExplosiveDip.copy(repetitionCount = "8-10"),
        DeepPushUp.copy(repetitionCount = "10-12"), SkullcrusherLvl2.copy(repetitionCount = "12-15"), PushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 212
)
val Inter_Push_workout10 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PikePushUp.copy(repetitionCount = "10"), StraightBarDip.copy(repetitionCount = "10-12"), HinduPushUp.copy(repetitionCount = "10"),
        PseudoPlanchePushUp.copy(repetitionCount = "10"), Dip.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 213
)
val Inter_Push_workout11 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ClapPushUp.copy(repetitionCount = "10-12"), CloseStraightBarDip.copy(repetitionCount = "8-10"), Dip.copy(repetitionCount = "12"),
        DeclinePushUp.copy(repetitionCount = "12"), TricepExtension.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 214
)
val Inter_Push_workout12 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PushUp.copy(repetitionCount = "12"), WallHsPushUp.copy(repetitionCount = "6-8"), Dip.copy(repetitionCount = "12"),
        PseudoPlanchePushUp.copy(repetitionCount = "10"), PikePushUp.copy(repetitionCount = "10"), InclinePushUp.copy(repetitionCount = "15")
    ),
    idetifier = 215
)
val Inter_Push_workout13 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        CloseStraightBarDip.copy(repetitionCount = "10"), TricepExtension.copy(repetitionCount = "12"), BenchDip.copy(repetitionCount = "20"),
        DeclinePushUp.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "12"), InclinePushUp.copy(repetitionCount = "15")
    ),
    idetifier = 216
)
val Inter_Push_workout14 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "8"), PseudoPlanchePushUp.copy(repetitionCount = "10-12"), StraightBarDip.copy(repetitionCount = "12"),
        DeepPushUp.copy(repetitionCount = "12"), Dip.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 217
)
val Inter_Push_workout15 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ArcherPushUp.copy(repetitionCount = "8 each"), ClapPushUp.copy(repetitionCount = "10"), SkullcrusherLvl2.copy(repetitionCount = "12-15"),
        ExplosiveDip.copy(repetitionCount = "10-12"), DiamondPushUp.copy(repetitionCount = "12"), InclinePushUp.copy(repetitionCount = "15")
    ),
    idetifier = 218
)
val Inter_Push_workout16 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        HinduPushUp.copy(repetitionCount = "10"), DeclinePushUp.copy(repetitionCount = "12"), PikePushUp.copy(repetitionCount = "12"),
        DeepPushUp.copy(repetitionCount = "12"), PseudoPlanchePushUp.copy(repetitionCount = "12"), StraightBarDip.copy(repetitionCount = "12")
    ),
    idetifier = 219
)
val Inter_Push_workout17 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        CloseStraightBarDip.copy(repetitionCount = "8-10"), ExplosiveDip.copy(repetitionCount = "12"), DiamondPushUp.copy(repetitionCount = "12"),
        ExplosivePushUp.copy(repetitionCount = "12"), TricepExtension.copy(repetitionCount = "12"), DeclinePushUp.copy(repetitionCount = "12")
    ),
    idetifier = 220
)
val Inter_Push_workout18 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "8"), PikePushUp.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "12"),
        DeepPushUp.copy(repetitionCount = "12"), ClapPushUp.copy(repetitionCount = "12"), ExplosivePushUp.copy(repetitionCount = "12")
    ),
    idetifier = 221
)
val Inter_Push_workout19 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        CloseStraightBarDip.copy(repetitionCount = "10"), PikePushUp.copy(repetitionCount = "12"), DeepPushUp.copy(repetitionCount = "12"),
        PseudoPlanchePushUp.copy(repetitionCount = "12"), Dip.copy(repetitionCount = "12"), DeclinePushUp.copy(repetitionCount = "12")
    ),
    idetifier = 222
)
val Inter_Push_workout20 = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "10"), ArcherPushUp.copy(repetitionCount = "8-10 each"), ExplosiveDip.copy(repetitionCount = "12"),
        DeclinePushUp.copy(repetitionCount = "12"), SkullcrusherLvl3.copy(repetitionCount = "8-10"), PseudoPlanchePushUp.copy(repetitionCount = "10")
    ),
    idetifier = 223
)
val Inter_Push_workout1N = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        ArcherPushUp.copy(repetitionCount = "5-6 each"), TricepExtension.copy(repetitionCount = "12"),SkullcrusherLvl2.copy(repetitionCount = "12"),
        StraightBarDip.copy(repetitionCount = "10"), DeclinePushUp.copy(repetitionCount = "10"), PushUp.copy(repetitionCount = "12")
    ),
    idetifier = 224
)
val Inter_Push_workout2N = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        PikePushUp.copy(repetitionCount = "10"), PseudoPlanchePushUp.copy(repetitionCount = "10"), HinduPushUp.copy(repetitionCount = "10"),
        DeclinePushUp.copy(repetitionCount = "10"), PushUp.copy(repetitionCount = "12"), InclinePushUp.copy(repetitionCount = "15")
    ),
    idetifier = 225
)
val Inter_Push_workout3N = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        CloseStraightBarDip.copy(repetitionCount = "8"), ExplosiveDip.copy(repetitionCount = "10"), DiamondPushUp.copy(repetitionCount = "12"),
        StraightBarDip.copy(repetitionCount = "12"), Dip.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "12")
    ),
    idetifier = 231
)
val Inter_Push_workout4N = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "8"), DeepPushUp.copy(repetitionCount = "10"), PseudoPlanchePushUp.copy(repetitionCount = "12"),
        DeclinePushUp.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "12"), Dip.copy(repetitionCount = "12")
    ),
    idetifier = 232
)
val Inter_Push_workout5N = Workout(
    name = "Push Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        CloseStraightBarDip.copy(repetitionCount = "10"), PikePushUp.copy(repetitionCount = "12"), PseudoPlanchePushUp.copy(repetitionCount = "12"),
        DeclinePushUp.copy(repetitionCount = "12"), DeepPushUp.copy(repetitionCount = "12"), Dip.copy(repetitionCount = "12")
    ),
    idetifier = 233
)
val Advanced_Push_workout1 = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "10"), PikePushUp.copy(repetitionCount = "12"), PseudoPlanchePushUp.copy(repetitionCount = "12"),
        DeclinePushUp.copy(repetitionCount = "12"), PushUp.copy(repetitionCount = "15"), InclinePushUp.copy(repetitionCount = "20")
    ),
    idetifier = 234
)
val Advanced_Push_workout2 = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        CloseStraightBarDip.copy(repetitionCount = "10-12"), ArcherPushUp.copy(repetitionCount = "8 each"), DiamondPushUp.copy(repetitionCount = "15"),
        StraightBarDip.copy(repetitionCount = "12-15"), ExplosiveDip.copy(repetitionCount = "12"), Dip.copy(repetitionCount = "15")
    ),
    idetifier = 235
)
val Advanced_Push_workout3 = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "12"), SkullcrusherLvl3.copy(repetitionCount = "10-12"), PseudoPlanchePushUp.copy(repetitionCount = "12-15"),
        ArcherPushUp.copy(repetitionCount = "8 each"), ExplosiveDip.copy(repetitionCount = "12"), TricepExtension.copy(repetitionCount = "15")
    ),
    idetifier = 236
)
val Advanced_Push_workout4 = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "12"), PikePushUp.copy(repetitionCount = "12-15"), PseudoPlanchePushUp.copy(repetitionCount = "12-15"),
        DeclinePushUp.copy(repetitionCount = "15"), StraightBarDip.copy(repetitionCount = "15"), Dip.copy(repetitionCount = "15-20")
    ),
    idetifier = 237
)
val Advanced_Push_workout5 = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "12"), PikePushUp.copy(repetitionCount = "12-15"), PseudoPlanchePushUp.copy(repetitionCount = "12-15"),
        ClapPushUp.copy(repetitionCount = "15"), StraightBarDip.copy(repetitionCount = "15"), Dip.copy(repetitionCount = "15-20")
    ),
    idetifier = 238
)
val Advanced_Push_workout6 = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        HsPushUp.copy(repetitionCount = "max"), CloseStraightBarDip.copy(repetitionCount = "12-15"), PseudoPlanchePushUp.copy(repetitionCount = "15"),
        WallHsPushUp.copy(repetitionCount = "12"), SkullcrusherLvl3.copy(repetitionCount = "12"), PikePushUp.copy(repetitionCount = "15")
    ),
    idetifier = 239
)
val Advanced_Push_workout7 = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ArcherPushUp.copy(repetitionCount = "10 each"), CloseStraightBarDip.copy(repetitionCount = "15"), DiamondPushUp.copy(repetitionCount = "15-20"),
        StraightBarDip.copy(repetitionCount = "15-20"), TricepExtension.copy(repetitionCount = "15-20"), PushUp.copy(repetitionCount = "20")
    ),
    idetifier = 240
)
val Advanced_Push_workout8 = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        HsPushUp.copy(repetitionCount = "max"), DeepPushUp.copy(repetitionCount = "15"), PseudoPlanchePushUp.copy(repetitionCount = "15"),
        WallHsPushUp.copy(repetitionCount = "12"), Dip.copy(repetitionCount = "20"), DeclinePushUp.copy(repetitionCount = "15-20")
    ),
    idetifier = 241
)
val Advanced_Push_workout9 = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ArcherPushUp.copy(repetitionCount = "10 each"), CloseStraightBarDip.copy(repetitionCount = "15"), StraightBarDip.copy(repetitionCount = "15-20"),
        Dip.copy(repetitionCount = "20"), SkullcrusherLvl3.copy(repetitionCount = "15-20"), PushUp.copy(repetitionCount = "15-20")
    ),
    idetifier = 242
)
val Advanced_Push_workout10 = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        HsPushUp.copy(repetitionCount = "max"), WallHsPushUp.copy(repetitionCount = "12-15"), PikePushUp.copy(repetitionCount = "15"),
        DeepPushUp.copy(repetitionCount = "15-20"), ExplosiveDip.copy(repetitionCount = "12-15"), Dip.copy(repetitionCount = "20")
    ),
    idetifier = 243
)
val Advanced_Push_workout1N = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "10-12"), PikePushUp.copy(repetitionCount = "12"), HinduPushUp.copy(repetitionCount = "12"),
        StraightBarDip.copy(repetitionCount = "12"), DeclinePushUp.copy(repetitionCount = "12"), PushUpHold.copy(repetitionCount = "45 sec")
    ),
    idetifier = 244
)
val Advanced_Push_workout2N = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        CloseStraightBarDip.copy(repetitionCount = "12"), TricepExtension.copy(repetitionCount = "15"), DiamondPushUp.copy(repetitionCount = "15"),
        StraightBarDip.copy(repetitionCount = "12-15"), DeepPushUp.copy(repetitionCount = "12-15"), Dip.copy(repetitionCount = "12-15")
    ),
    idetifier = 245
)
val Advanced_Push_workout3N = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "12"), CloseStraightBarDip.copy(repetitionCount = "12"), ClapPushUp.copy(repetitionCount = "12-15"),
        StraightBarDip.copy(repetitionCount = "12-15"), Dip.copy(repetitionCount = "15-20"), InclinePushUp.copy(repetitionCount = "20-25")
    ),
    idetifier = 246
)
val Advanced_Push_workout4N = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        HsPushUp.copy(repetitionCount = "max"), WallHsPushUp.copy(repetitionCount = "12"), PikePushUp.copy(repetitionCount = "15"),
        CloseStraightBarDip.copy(repetitionCount = "15"), TricepExtension.copy(repetitionCount = "15-20"), Dip.copy(repetitionCount = "20")
    ),
    idetifier = 247
)
val Advanced_Push_workout5N = Workout(
    name = "Push Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        HsPushUp.copy(repetitionCount = "max"), WallHsPushUp.copy(repetitionCount = "15"), PikePushUp.copy(repetitionCount = "15-20"),
        CloseStraightBarDip.copy(repetitionCount = "15-20"), SkullcrusherLvl3.copy(repetitionCount = "15"), Dip.copy(repetitionCount = "20")
    ),
    idetifier = 248
)
val Beginner_Ab_workout1N = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount = "10-15 sec"),Tuck_L_sit.copy(repetitionCount="10-15 sec"), SeatedLegRaises.copy(repetitionCount = "8-10"),
        BoatHold.copy(repetitionCount = "15-20 sec"),Plank.copy(repetitionCount = "1 min"), HipLift.copy(repetitionCount = "8-10"),
        LyingHyperExtension.copy(repetitionCount = "10-12")
    ),
    idetifier = 71
)
val Beginner_Ab_workout2N = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount = "10-15 sec"),Tuck_L_sit.copy(repetitionCount="15-20 sec"),
        HangingLegRaises.copy(repetitionCount = "10-12"), KneeRaises.copy(repetitionCount = "10-12"),
        BoatHold.copy(repetitionCount = "15-20 sec"),Plank.copy(repetitionCount = "45-60 sec"),
        LyingHyperExtensionHold.copy(repetitionCount = "15-20 sec")
    ),
    idetifier = 72
)
val Beginner_Ab_workout3N = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount = "15-20 sec"),Tuck_L_sit.copy(repetitionCount="15-20 sec"),
        HangingKneeRaises.copy(repetitionCount = "12-15"), LegRaises.copy(repetitionCount = "12-15"),
        BoatHold.copy(repetitionCount = "20-25 sec"),Plank.copy(repetitionCount = "1 min"),
        LyingHyperExtension.copy(repetitionCount = "12-15")
    ),
    idetifier = 73
)
val Beginner_Ab_workout4N = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount = "15-20 sec"),Tuck_L_sit.copy(repetitionCount="20-25 sec"), SeatedLegRaises.copy(repetitionCount = "12-15"),
        HangingLegRaises.copy(repetitionCount = "15"), LegRaises.copy(repetitionCount = "15"),
        KneeRaises.copy(repetitionCount = "15"),
        LyingHyperExtension.copy(repetitionCount = "20-25 sec")
    ),
    idetifier = 74
)
val Beginner_Ab_workout5N = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount = "20 sec"),Tuck_L_sit.copy(repetitionCount="25-30 sec"), LyingLegRaises.copy(repetitionCount = "20"),
        HangingLegRaises.copy(repetitionCount = "15"), LegRaises.copy(repetitionCount = "15"),
        KneeRaises.copy(repetitionCount = "15"),
        LyingHyperExtension.copy(repetitionCount = "25-30 sec")
    ),
    idetifier = 249
)
val Beginner_Ab_workout1 = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Tuck_L_sit.copy(repetitionCount="10-15 sec"), HangingLegRaises.copy(repetitionCount = "8"), BoatHold.copy(repetitionCount = "15-20 sec"),
        KneeRaises.copy(repetitionCount = "8-10"),Plank.copy(repetitionCount = "45-60 sec"), LyingHyperExtension.copy(repetitionCount = "8-10")
    ),
    idetifier = 75
)
val Beginner_Ab_workout2 = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Tuck_L_sit.copy(repetitionCount="10-15 sec"), HangingSideKneeRaises.copy(repetitionCount = "8-10 each"), BoatHold.copy(repetitionCount = "15-20 sec"),
        LegRaises.copy(repetitionCount = "10-12"),Plank.copy(repetitionCount = "45-60 sec"), LyingHyperExtensionHold.copy(repetitionCount = "15-20 sec")
    ),
    idetifier = 76
)
val Beginner_Ab_workout3 = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Tuck_L_sit.copy(repetitionCount="15-20 sec"), HangingKneeRaises.copy(repetitionCount = "10-12"), BoatHold.copy(repetitionCount = "15-20 sec"),
        LegRaises.copy(repetitionCount = "10-12"),Plank.copy(repetitionCount = "1 min"), LyingHyperExtension.copy(repetitionCount = "10-12")
    ),
    idetifier = 77
)
val Beginner_Ab_workout4 = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="10-15 sec"), HangingLegRaises.copy(repetitionCount = "8-10"),
        SideKneeRaises.copy(repetitionCount = "1 min"), LyingLegRaises.copy(repetitionCount = "8-10"), HipLift.copy(repetitionCount = "8-10" ),
        LyingHyperExtensionHold.copy(repetitionCount = "15-20 sec")
    ),
    idetifier = 78
)
val Beginner_Ab_workout5 = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="10-15 sec"), HangingLegRaises.copy(repetitionCount = "10-12"),
        HipLift.copy(repetitionCount = "10-12"),
        Plank.copy(repetitionCount = "1 min"), KneeRaises.copy(repetitionCount = "10-12"),
        LyingHyperExtension.copy(repetitionCount = "10-12")
    ),
    idetifier = 79
)
val Beginner_Ab_workout6 = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="15-20 sec"), HangingKneeRaises.copy(repetitionCount = "12-15"), BoatHold.copy(repetitionCount = "15-20 sec"),
        SeatedLegRaises.copy(repetitionCount = "8-10"),Plank.copy(repetitionCount = "1 min"), LyingHyperExtensionHold.copy(repetitionCount = "15-20 sec")
    ),
    idetifier = 80
)
val Beginner_Ab_workout7 = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="15-20 sec"), HangingSideKneeRaises.copy(repetitionCount = "10 each"),
        BoatHold.copy(repetitionCount = "20-30 sec"), LegRaises.copy(repetitionCount = "12"), HipLift.copy(repetitionCount = "10-12" ),
        LyingHyperExtension.copy(repetitionCount = "15")
    ),
    idetifier = 81
)
val Beginner_Ab_workout8 = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="15-20 sec"), HangingLegRaises.copy(repetitionCount = "12"),
        BoatHold.copy(repetitionCount = "20-30 sec"), SeatedLegRaises.copy(repetitionCount = "12-15"), Plank.copy(repetitionCount = "1 min" ),
        LyingHyperExtensionHold.copy(repetitionCount = "20-25 sec")
    ),
    idetifier = 82
)
val Beginner_Ab_workout9 = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Tuck_L_sit.copy(repetitionCount="20-25 sec"), HangingKneeRaises.copy(repetitionCount = "15"),
        SideKneeRaises.copy(repetitionCount = "12 each"), BoatHold.copy(repetitionCount = "25 sec"), SitUp.copy(repetitionCount = "30"),
        LyingHyperExtensionHold.copy(repetitionCount = "20-25 sec")
    ),
    idetifier = 153
)
val Beginner_Ab_workout10 = Workout(
    name = "Ab Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="15-20 sec"), HangingLegRaises.copy(repetitionCount = "12-15"),
        BoatHold.copy(repetitionCount = "20-30 sec"), SeatedLegRaises.copy(repetitionCount = "12-15"), Plank.copy(repetitionCount = "1 min" ),
        LyingHyperExtension.copy(repetitionCount = "15")
    ),
    idetifier = 154
)
val Inter_Ab_workout1N = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="30 sec"), SeatedLegRaises.copy(repetitionCount = "10-12"),HangingSideKneeRaises.copy(repetitionCount = "12 each"),
        Plank.copy(repetitionCount = "1 min 30 sec"), LegRaises.copy(repetitionCount = "12-15"), HipLift.copy(repetitionCount = "12-15" ),
        LyingHyperExtension.copy(repetitionCount = "20")
    ),
    idetifier = 261
)
val Inter_Ab_workout2N = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="30 sec"), Tuck_L_sit.copy(repetitionCount = "30 sec"), HangingKneeRaises.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "1 min 30 sec"), LegRaises.copy(repetitionCount = "15"), BoatHold.copy(repetitionCount = "40 sec" ),
        LyingHyperExtension.copy(repetitionCount = "25")
    ),
    idetifier = 262
)
val Inter_Ab_workout3N = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="15 sec"), Tuck_L_sit.copy(repetitionCount = "30 sec"), HangingLegRaises.copy(repetitionCount = "15-20"),
        Plank.copy(repetitionCount = "2 min"), SideKneeRaises.copy(repetitionCount = "12 each"), HipLift.copy(repetitionCount = "15-20" ),
        LyingHyperExtension.copy(repetitionCount = "25")
    ),
    idetifier = 263
)
val Inter_Ab_workout4N = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="20 sec"), SeatedLegRaises.copy(repetitionCount = "15"), SideToesToBar.copy(repetitionCount = "5 each"),
        Plank.copy(repetitionCount = "2 min"), LegRaises.copy(repetitionCount = "15-20"), BoatHold.copy(repetitionCount = "45 sec" ),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 264
)
val Inter_Ab_workout5N = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="20 sec"), ToesToBar.copy(repetitionCount = "8-10"), Clock.copy(repetitionCount = "5 each"),
        Plank.copy(repetitionCount = "2 min"), LegRaises.copy(repetitionCount = "15-20"), SideKneeRaises.copy(repetitionCount = "12 each" ),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 265
)
val Inter_Ab_workout1 = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Tuck_L_sit.copy(repetitionCount="30 sec"), HangingSideKneeRaises.copy(repetitionCount = "10 each"),
        Plank.copy(repetitionCount = "1 min"), LegRaises.copy(repetitionCount = "12-15"), BoatHold.copy(repetitionCount = "30 sec" ),
        LyingHyperExtension.copy(repetitionCount = "15")
    ),
    idetifier = 250
)
val Inter_Ab_workout2 = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="20-25 sec"), HangingLegRaises.copy(repetitionCount = "12"),
        Plank.copy(repetitionCount = "1 min"), SideKneeRaises.copy(repetitionCount = "12 each"), HipLift.copy(repetitionCount = "12"),
        LyingHyperExtensionHold.copy(repetitionCount = "30 sec")
    ),
    idetifier = 251
)
val Inter_Ab_workout3 = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="25 sec"), HangingKneeRaises.copy(repetitionCount = "15"),
        Plank.copy(repetitionCount = "1 min 30 sec"), LegRaises.copy(repetitionCount = "15"), BoatHold.copy(repetitionCount = "30 sec"),
        LyingHyperExtensionHold.copy(repetitionCount = "30 sec")
    ),
    idetifier = 252
)
val Inter_Ab_workout4 = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AlternateLegL_sit.copy(repetitionCount="30 sec"), SeatedLegRaises.copy(repetitionCount = "15"),
        Plank.copy(repetitionCount = "1 min 30 sec"), LegRaises.copy(repetitionCount = "15"), SitUp.copy(repetitionCount = "30-35"),
        LyingHyperExtension.copy(repetitionCount = "20")
    ),
    idetifier = 253
)
val Inter_Ab_workout5 = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="10-15 sec"), HangingSideKneeRaises.copy(repetitionCount = "12 each"),
        Plank.copy(repetitionCount = "1 min 30 sec"), KneeRaises.copy(repetitionCount = "20"), HipLift.copy(repetitionCount = "15"),
        LyingHyperExtension.copy(repetitionCount = "20")
    ),
    idetifier = 254
)
val Inter_Ab_workout6 = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="15 sec"), Tuck_L_sit.copy(repetitionCount = "30 sec"), HangingLegRaises.copy(repetitionCount = "12-15"),
        Plank.copy(repetitionCount = "2 min"), SideKneeRaises.copy(repetitionCount = "12 each"), BoatHold.copy(repetitionCount = "35-40 sec"),
        LyingHyperExtension.copy(repetitionCount = "25")
    ),
    idetifier = 255
)
val Inter_Ab_workout7 = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="15 sec"), ToesToBar.copy(repetitionCount = "6-8"),
        Plank.copy(repetitionCount = "2 min"), SideKneeRaises.copy(repetitionCount = "12 each"), LegRaises.copy(repetitionCount = "15"),
        LyingHyperExtension.copy(repetitionCount = "25")
    ),
    idetifier = 256
)
val Inter_Ab_workout8 = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="15 sec"), SideToesToBar.copy(repetitionCount = "4-5 each"),
        Plank.copy(repetitionCount = "2 min"), LegRaises.copy(repetitionCount = "15"), HipLift.copy(repetitionCount = "15-20"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 257
)
val Inter_Ab_workout9 = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="20 sec"), ToesToBar.copy(repetitionCount = "8"),
        Plank.copy(repetitionCount = "2 min"), HangingKneeRaises.copy(repetitionCount = "15-20"), BoatHold.copy(repetitionCount = "40-45 sec"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 258
)
val Inter_Ab_workout10 = Workout(
    name = "Ab Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="20 sec"), Clock.copy(repetitionCount = "5-6 each"),
        Plank.copy(repetitionCount = "2 min"), LegRaises.copy(repetitionCount = "15-20"), HipLift.copy(repetitionCount = "15-20"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 259
)
val Advanced_Ab_workout1 = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="20 sec"), ToesToBar.copy(repetitionCount = "8-10"),
        SideToesToBar.copy(repetitionCount = "5 each"), LegRaises.copy(repetitionCount = "20"), Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 260
)
val Advanced_Ab_workout2 = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="25 sec"), Clock.copy(repetitionCount = "6 each"),
        ToesToBar.copy(repetitionCount = "10"), KneeRaises.copy(repetitionCount = "25"), Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 266
)
val Advanced_Ab_workout3 = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="25 sec"), ToesToBar.copy(repetitionCount = "10-12"),
        HighLegRaises.copy(repetitionCount = "10"), SideKneeRaises.copy(repetitionCount = "12 each"), Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 267
)
val Advanced_Ab_workout4 = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="30 sec"), SeatedLegRaises.copy(repetitionCount = "20"),
        SideToesToBar.copy(repetitionCount = "6 each"), LegRaises.copy(repetitionCount = "20"), Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtensionHold.copy(repetitionCount = "1 min")
    ),
    idetifier = 268
)
val Advanced_Ab_workout5 = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        LegRaisesFromL_sit.copy(repetitionCount="8"), ToesToBar.copy(repetitionCount = "12"),
        Clock.copy(repetitionCount = "6 each"), KneeRaises.copy(repetitionCount = "25"), Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 269
)
val Advanced_Ab_workout6 = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        LegRaisesFromL_sit.copy(repetitionCount="8"), SideToesToBar.copy(repetitionCount = "6-8 each"),
        L_sit.copy(repetitionCount = "30 sec"), HighLegRaises.copy(repetitionCount = "12"), Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 270
)
val Advanced_Ab_workout7 = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="30 sec"), AlternateLegL_sit.copy(repetitionCount = "30 sec"),
        ToesToBar.copy(repetitionCount = "10-12"), Clock.copy(repetitionCount = "6-8 each"), Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 271
)
val Advanced_Ab_workout8 = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        V_sit.copy(repetitionCount="max"), LegRaisesFromL_sit.copy(repetitionCount = "8-10"),
        ToesToBar.copy(repetitionCount = "12-15"), HighLegRaises.copy(repetitionCount = "12"),
        BoatHold.copy(repetitionCount = "1 min"),   Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 272
)
val Advanced_Ab_workout9 = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        V_sit.copy(repetitionCount="max"), LegRaisesFromL_sit.copy(repetitionCount = "8-10"),
        L_sit.copy(repetitionCount = "30 sec"), ToesToBar.copy(repetitionCount = "12-15"),
        LegRaises.copy(repetitionCount = "25-30"),   Plank.copy(repetitionCount = "4 min"),
        LyingHyperExtension.copy(repetitionCount = "40")
    ),
    idetifier = 273
)
val Advanced_Ab_workout10 = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "1 min between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        V_sit.copy(repetitionCount="max"), LegRaisesFromL_sit.copy(repetitionCount = "10"),
        ToesToBar.copy(repetitionCount = "15"), Clock.copy(repetitionCount = "8 each"),
        SideKneeRaises.copy(repetitionCount = "20 each"),   Plank.copy(repetitionCount = "5 min"),
        LyingHyperExtension.copy(repetitionCount = "40")
    ),
    idetifier = 274
)
val Advanced_Ab_workout1N = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="25 sec"), SeatedLegRaises.copy(repetitionCount = "20"),
        ToesToBar.copy(repetitionCount = "10"), HangingLegRaises.copy(repetitionCount = "20"), Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 275
)
val Advanced_Ab_workout2N = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        L_sit.copy(repetitionCount="30 sec"), SideToesToBar.copy(repetitionCount = "6-8 each"),
        ToesToBar.copy(repetitionCount = "12"), HighLegRaises.copy(repetitionCount = "10"), Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 276
)
val Advanced_Ab_workout3N = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        LegRaisesFromL_sit.copy(repetitionCount="10"), AlternateLegL_sit.copy(repetitionCount = "30 sec"),
        Clock.copy(repetitionCount = "6-8 each"), LegRaises.copy(repetitionCount = "25"), Plank.copy(repetitionCount = "3 min"),
        LyingHyperExtension.copy(repetitionCount = "30")
    ),
    idetifier = 277
)
val Advanced_Ab_workout4N = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        V_sit.copy(repetitionCount="max"), LegRaisesFromL_sit.copy(repetitionCount = "10"), ToesToBar.copy(repetitionCount = "6-8 each"),
        BoatHold.copy(repetitionCount = "1 min"), Clock.copy(repetitionCount = "6-8 each"), Plank.copy(repetitionCount = "4 min"),
        LyingHyperExtension.copy(repetitionCount = "40")
    ),
    idetifier = 278
)
val Advanced_Ab_workout5N = Workout(
    name = "Ab Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        V_sit.copy(repetitionCount="max"), L_sit.copy(repetitionCount = "30 sec"), SeatedLegRaises.copy(repetitionCount = "25"),
        HipLift.copy(repetitionCount = "30"), SideToesToBar.copy(repetitionCount = "8-10 each"), Plank.copy(repetitionCount = "5 min"),
        LyingHyperExtension.copy(repetitionCount = "40")
    ),
    idetifier = 279
)
val Inter_Full_Body_workout1 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        PullUp.copy(repetitionCount="8-10"), Dip.copy(repetitionCount = "10-12"), JumpSquat.copy(repetitionCount = "15"),
        Plank.copy("2 min"), ChinUpHold.copy(repetitionCount = "25-30 sec"), PushUp.copy(repetitionCount = "12-15"),
        HangingKneeRaises.copy(repetitionCount = "10-12")
    ),
    idetifier = 160
)
val Inter_Full_Body_workout2 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        PullUp.copy(repetitionCount="8-10"), StraightBarDip.copy(repetitionCount = "8-10"), BulgSplitSquat.copy(repetitionCount = "10 each"),
        Plank.copy("2 min"), ChinUp.copy(repetitionCount = "8-10 sec"), PushUp.copy(repetitionCount = "12-15"),
        LegRaises.copy(repetitionCount = "10-12")
    ),
    idetifier = 161
)
val Inter_Full_Body_workout3 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount="8-10"), Dip.copy(repetitionCount = "12"), WallSit.copy(repetitionCount = "25-30 sec"),
        Plank.copy("2 min"), ChinUpHold.copy(repetitionCount = "25-30 sec"), DeclinePushUp.copy(repetitionCount = "10-12"),
        HangingKneeRaises.copy(repetitionCount = "12")
    ),
    idetifier = 162
)
val Inter_Full_Body_workout4 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        PullUp.copy(repetitionCount="10-12"), Dip.copy(repetitionCount = "10-12"), JumpSquat.copy(repetitionCount = "15"),
        Plank.copy("2 min"), CommandoPullUp.copy(repetitionCount = "4-5 each"), PushUp.copy(repetitionCount = "12-15"),
        LegRaises.copy(repetitionCount = "10-12")
    ),
    idetifier = 163
)
val Inter_Full_Body_workout5 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        L_sit.copy(repetitionCount = "10-15 sec"),PullUp.copy(repetitionCount="10-12"), SkullcrusherLvl2.copy(repetitionCount = "12"), Squat.copy(repetitionCount = "20-25"),
        Plank.copy("2 min"), CloseGripChinUp.copy(repetitionCount = "10-12"), DeclinePushUp.copy(repetitionCount = "12"),
        HangingKneeRaises.copy(repetitionCount = "10-12")
    ),
    idetifier = 164
)
val Inter_Full_Body_workout6 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        ExplosivePullUp.copy(repetitionCount="8-10"), Dip.copy(repetitionCount = "12"), WallSit.copy(repetitionCount = "30-35 sec"),
        Plank.copy("2 min"), CommandoPullUp.copy(repetitionCount = "4-5 each"), ExplosivePushUp.copy(repetitionCount = "10-12"),
        LyingLegRaises.copy(repetitionCount = "20-25")
    ),
    idetifier = 165
)
val Inter_Full_Body_workout7 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        WidePullUp.copy(repetitionCount="8-10"), ExplosivePushUp.copy(repetitionCount = "10-12"), BulgSplitSquat.copy(repetitionCount = "10 each"),
        Plank.copy("2 min"), CloseGripChinUp.copy(repetitionCount = "8-10"), StraightBarDip.copy(repetitionCount = "10-12"),
        HangingKneeRaises.copy(repetitionCount = "15")
    ),
    idetifier = 166
)
val Inter_Full_Body_workout8 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        PullUp.copy(repetitionCount="10-12"), ExplosivePushUp.copy(repetitionCount = "10-12"), Squat.copy(repetitionCount = "20-25"),
        LegRaises.copy("10-12"), BarCurl.copy(repetitionCount = "8-10"), Dip.copy(repetitionCount = "10-12"),
        HangingKneeRaises.copy(repetitionCount = "15")
    ),
    idetifier = 167
)
val Inter_Full_Body_workout9 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        DeclinePushUp.copy(repetitionCount = "10-12"), ExplosivePullUp.copy(repetitionCount = "8-10"), WallSit.copy(repetitionCount = "35-40 sec"),
        BoatHold.copy(repetitionCount = "30-35 sec"), StraightBarDip.copy(repetitionCount = "10-12"), WideChinUp.copy(repetitionCount = "8-10"),
        HangingLegRaises.copy(repetitionCount = "12")
    ),
    idetifier = 168
)
val Inter_Full_Body_workout10 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        DeclinePushUp.copy(repetitionCount = "12"), ChestTOBarPullUp.copy(repetitionCount = "8-10"), BulgSplitSquat.copy(repetitionCount = "12 each"),
        Plank.copy(repetitionCount = "2 min 30 sec"), TricepExtension.copy(repetitionCount = "12"), CommandoPullUp.copy(repetitionCount = "5-6 each"),
        HangingSideKneeRaises.copy(repetitionCount = "8 each")
    ),
    idetifier = 170
)
val Inter_Full_Body_workout11 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        LSitPullUp.copy(repetitionCount="8-10"), ClapPushUp.copy(repetitionCount = "10-12"), Squat.copy(repetitionCount = "20-25"),
        HangingKneeRaises.copy("15"), CloseGripChinUp.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "10-12"),
        LegRaises.copy(repetitionCount = "12")
    ),
    idetifier = 171
)
val Inter_Full_Body_workout12 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        DeepPushUp.copy(repetitionCount = "10-12"), PullUp.copy(repetitionCount = "12"), BulgSplitSquat.copy(repetitionCount = "12 each"),
        Plank.copy(repetitionCount = "2 min 30 sec"), DiamondPushUp.copy(repetitionCount = "12"), BarCurl.copy(repetitionCount = "8-10"),
        HangingLegRaises.copy(repetitionCount = "10-12")
    ),
    idetifier = 171
)
val Inter_Full_Body_workout13 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        ChestTOBarPullUp.copy(repetitionCount="10"), ExplosivePushUp.copy(repetitionCount = "12-15"), Squat.copy(repetitionCount = "25"),
        SideKneeRaises.copy("8-10 each"), WidePullUp.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "12-15"),
        HangingLegRaises.copy(repetitionCount = "12")
    ),
    idetifier = 172
)
val Inter_Full_Body_workout14 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        StraightBarDip.copy(repetitionCount = "12-15"), PullUp.copy(repetitionCount = "12-15"), WallSit.copy(repetitionCount = "40-45 sec"),
        BoatHold.copy(repetitionCount = "45-50 sec"), TricepExtension.copy(repetitionCount = "12-15"), CloseGripChinUp.copy(repetitionCount = "12"),
        HangingLegRaises.copy(repetitionCount = "12-15")
    ),
    idetifier = 173
)
val Inter_Full_Body_workout15 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        CommandoPullUp.copy(repetitionCount="6-8 each"), DeepPushUp.copy(repetitionCount = "12"), SemiPistolSquatLvl2.copy(repetitionCount = "6-8 each"),
        Plank.copy("3 min"), ChinUp.copy(repetitionCount = "12-15"), Dip.copy(repetitionCount = "15-20"),
        HangingLegRaises.copy(repetitionCount = "12")
    ),
    idetifier = 174
)
val Inter_Full_Body_workout16= Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        ArcherPushUp.copy(repetitionCount = "6 each"), WidePullUp.copy(repetitionCount = "12"), WallSit.copy(repetitionCount = "45-50 sec"),
        Plank.copy(repetitionCount = "3 min"), PseudoPlanchePushUp.copy(repetitionCount = "10-12"), CloseGripChinUp.copy(repetitionCount = "12"),
        HangingLegRaises.copy(repetitionCount = "12-15")
    ),
    idetifier = 169
)
val Inter_Full_Body_workout17 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        ChestTOBarPullUp.copy(repetitionCount="10-12"), DiamondPushUp.copy(repetitionCount = "15"), SemiPistolSquatLvl2.copy(repetitionCount = "8 each"),
        L_sit.copy("20-25 sec"), CommandoPullUp.copy(repetitionCount = "6-8 each"), Dip.copy(repetitionCount = "15-20"),
        ToesToBar.copy(repetitionCount = "8-10")
    ),
    idetifier = 175
)
val Inter_Full_Body_workout18 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        DeepPushUp.copy(repetitionCount = "12-15"), OaChinUpHold.copy(repetitionCount = "15 sec each"), Squat.copy(repetitionCount = "30"),
        Plank.copy(repetitionCount = "3 min"), StraightBarDip.copy(repetitionCount = "15-20"), LSitPullUp.copy(repetitionCount = "10-12"),
        SideToesToBar.copy(repetitionCount = "6-8 each")
    ),
    idetifier = 176
)
val Inter_Full_Body_workout19 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        ArcherPullUp.copy(repetitionCount="5-6 each"), Dip.copy(repetitionCount = "20"), WallSit.copy(repetitionCount = "45-50 sec"),
        BoatHold.copy("45-50 sec"), ExplosivePullUp.copy(repetitionCount = "12"), SkullcrusherLvl2.copy(repetitionCount = "20"),
        ToesToBar.copy(repetitionCount = "8-10")
    ),
    idetifier = 177
)
val Inter_Full_Body_workout20 = Workout(
    name = "Full Body Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        Dip.copy(repetitionCount = "20-25"), WidePullUp.copy(repetitionCount = "15"), SideLunges.copy(repetitionCount = "8-10 each"),
        Plank.copy(repetitionCount = "3 min 30 sec"), ClapPushUp.copy(repetitionCount = "15-20"), CloseGripChinUp.copy(repetitionCount = "15"),
        ToesToBar.copy(repetitionCount = "10")
    ),
    idetifier = 178
)
val Advanced_Full_Body_workout1 = Workout(
    name = "Full Body Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        StraightBarDip.copy(repetitionCount = "20-25"), ArcherPullUp.copy(repetitionCount = "5-6 each"), WallSit.copy(repetitionCount = "1 min"),
        Plank.copy(repetitionCount = "4 min"), DeepPushUp.copy(repetitionCount = "20-25"), ChestTOBarPullUp.copy(repetitionCount = "12-15"),
        ToesToBar.copy(repetitionCount = "12-15")
    ),
    idetifier = 179
)
val Advanced_Full_Body_workout2 = Workout(
    name = "Full Body Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        WidePullUp.copy(repetitionCount="15-20"), Dip.copy(repetitionCount = "25"), SemiPistolSquatLvl2.copy(repetitionCount = "10 each"),
        HighLegRaises.copy("12"), CloseGripChinUp.copy(repetitionCount = "15-20"), TricepExtension.copy(repetitionCount = "20-25"),
        ToesToBar.copy(repetitionCount = "12-15")
    ),
    idetifier = 180
)
val Advanced_Full_Body_workout3 = Workout(
    name = "Full Body Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "10-12"), CommandoPullUp.copy(repetitionCount = "7-8 each"), WallSit.copy(repetitionCount = "1 min"),
        L_sit.copy(repetitionCount = "30-35 sec"), SkullcrusherLvl3.copy(repetitionCount = "10-12"), ChestTOBarPullUp.copy(repetitionCount = "12-15"),
        ToesToBar.copy(repetitionCount = "15")
    ),
    idetifier = 181
)
val Advanced_Full_Body_workout4 = Workout(
    name = "Full Body Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        OaChinUpHold.copy(repetitionCount="15-20 sec each"), ClapPushUp.copy(repetitionCount = "20"), SemiPistolSquatLvl2.copy(repetitionCount = "10 each"),
        SideToesToBar.copy("7-8 each"), PullUp.copy(repetitionCount = "15-20"), StraightBarDip.copy(repetitionCount = "20-25"),
        BoatHold.copy(repetitionCount = "1 min")
    ),
    idetifier = 182
)
val Advanced_Full_Body_workout5 = Workout(
    name = "Full Body Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        MuscleUp.copy(repetitionCount = "8-10"), TricepExtension.copy(repetitionCount = "20-25"), WidePullUp.copy(repetitionCount = "15"), JumpSquat.copy(repetitionCount = "30"),
        L_sit.copy(repetitionCount = "30-35 sec"), DeepPushUp.copy(repetitionCount = "20"), ChestTOBarPullUp.copy(repetitionCount = "12-15"),
        ToesToBar.copy(repetitionCount = "15")
    ),
    idetifier = 183
)
val Advanced_Full_Body_workout6 = Workout(
    name = "Full Body Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        OaChinUpHold.copy(repetitionCount="20 sec each"), CloseStraightBarDip.copy(repetitionCount = "15-20"), PistolSquat.copy(repetitionCount = "8-10 each"),
        HangingKneeRaises.copy("20-25"), PullUp.copy(repetitionCount = "15-20"), PikePushUp.copy(repetitionCount = "15-20"),
        Plank.copy(repetitionCount = "4 min")
    ),
    idetifier = 184
)
val Advanced_Full_Body_workout7 = Workout(
    name = "Full Body Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        MuscleUp.copy(repetitionCount = "10"), ClapPushUp.copy(repetitionCount = "20-25"), LSitPullUp.copy(repetitionCount = "15"), BulgSplitSquat.copy(repetitionCount = "15 each"),
        LegRaises.copy(repetitionCount = "20"), SkullcrusherLvl3.copy(repetitionCount = "10-12"), PullUp.copy(repetitionCount = "15-20"),
        ToesToBar.copy(repetitionCount = "15")
    ),
    idetifier = 185
)
val Advanced_Full_Body_workout8 = Workout(
    name = "Full Body Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        ArcherPullUp.copy(repetitionCount="8 each"), WallHsPushUp.copy(repetitionCount = "12"), PistolSquat.copy(repetitionCount = "8-10 each"),
        ToesToBar.copy("15"), ChinUp.copy(repetitionCount = "15-20"), DiamondPushUp.copy(repetitionCount = "20-25"),
        Plank.copy(repetitionCount = "4 min")
    ),
    idetifier = 186
)
val Advanced_Full_Body_workout9 = Workout(
    name = "Full Body Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        WallHsPushUp.copy(repetitionCount = "12-15"), OaChinUpHold.copy(repetitionCount = "20-25 sec each"), JumpSquat.copy(repetitionCount = "30"),
        LegRaisesFromL_sit.copy(repetitionCount = "10-12"), Dip.copy(repetitionCount = "25-30"), PullUp.copy(repetitionCount = "20"),
        ToesToBar.copy(repetitionCount = "20")
    ),
    idetifier = 187
)
val Advanced_Full_Body_workout10 = Workout(
    name = "Full Body Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        MuscleUp.copy(repetitionCount = "10-12"), ArcherPushUp.copy(repetitionCount = "10-12 each"), LSitPullUp.copy(repetitionCount = "15"),
        PistolSquat.copy(repetitionCount = "10-12 each"), ToesToBar.copy(repetitionCount = "20"), Dip.copy(repetitionCount = "30"),
        PullUp.copy(repetitionCount = "20"), L_sit.copy(repetitionCount = "40-45 sec")
    ),
    idetifier = 203
)
val Beginner_Full_Body_workout1 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 1,
    exercises = listOf(
        JumpingPullUp.copy(repetitionCount="8-10"), InclinePushUp.copy(repetitionCount = "10"),
        Squat.copy(repetitionCount = "12"), Plank.copy(repetitionCount = "1 min"),
        AustralianChinUp.copy(repetitionCount = "10-12"), SkullcrusherLvl1.copy(repetitionCount = "10"), KneeRaises.copy(repetitionCount = "10")
    ),
    idetifier = 83
)
val Beginner_Full_Body_workout2 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 2,
    exercises = listOf(
        AustralianPullUp.copy(repetitionCount = "10-12"), KneePushUp.copy(repetitionCount = "12"), Squat.copy(repetitionCount = "12"),
        Plank.copy(repetitionCount = "1 min"),JumpingChinUp.copy(repetitionCount = "10"), BenchDip, SitUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 84
)
val Beginner_Full_Body_workout3 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 3,
    exercises = listOf(
        AustralianChinUp.copy(repetitionCount = "12"), NegativePushUp.copy(repetitionCount = "8-10"), Squat.copy(repetitionCount = "12-15"),
        Plank.copy(repetitionCount = "1 min"), JumpingPullUp.copy(repetitionCount = "10"), SkullcrusherLvl1.copy(repetitionCount = "12"), KneeRaises.copy(repetitionCount = "12")
    ),
    idetifier = 85
)
val Beginner_Full_Body_workout4 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 4,
    exercises = listOf(
        AustralianPullUp.copy(repetitionCount = "12"), KneePushUp.copy(repetitionCount = "12"), Squat.copy(repetitionCount = "12-15"),
        Plank.copy(repetitionCount = "1 min"),JumpingChinUp.copy(repetitionCount = "10-12"), BenchDip.copy(repetitionCount = "12"), SitUp.copy(repetitionCount = "12-15")
    ),
    idetifier = 86
)
val Beginner_Full_Body_workout5 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 5,
    exercises = listOf(
        JumpingPullUp.copy(repetitionCount = "12"), InclinePushUp.copy(repetitionCount = "10-12"), Squat.copy(repetitionCount = "15"),
        Plank.copy(repetitionCount = "1 min"), AustralianChinUp.copy(repetitionCount = "12-15"), BenchDip.copy(repetitionCount = "12"), HangingKneeRaises.copy(repetitionCount = "10-12")
    ),
    idetifier = 87
)
val Beginner_Full_Body_workout6 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AustralianPullUp.copy(repetitionCount = "12-15"), NegativePushUp.copy(repetitionCount = "8-10"), Squat.copy(repetitionCount = "15"),
        Plank.copy(repetitionCount = "1 min 30 sec"), JumpingChinUp.copy(repetitionCount = "12"), BenchDip.copy(repetitionCount = "12-15"), HangingKneeRaises.copy(repetitionCount = "12")
    ),
    idetifier = 88
)
val Beginner_Full_Body_workout7 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        NegativePullUp.copy(repetitionCount = "4-6"), PushUp.copy(repetitionCount = "8-10"), WallSit.copy(repetitionCount = "30 sec"),
        Plank.copy(repetitionCount = "1 min 30 sec"), ChinUpHold.copy(repetitionCount = "15 sec"), BenchDip.copy(repetitionCount = "12-15"), HangingKneeRaises.copy(repetitionCount = "12")
    ),
    idetifier = 89
)
val Beginner_Full_Body_workout8 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        NegativeChinUp.copy(repetitionCount = "4-6"), PushUp.copy(repetitionCount = "8-10"), Squat.copy(repetitionCount = "15-20"),
        Plank.copy(repetitionCount = "1 min 30 sec"), PullUpHold.copy(repetitionCount = "15 sec"), BenchDip.copy(repetitionCount = "12-15"), KneeRaises.copy(repetitionCount = "12-15")
    ),
    idetifier = 90
)
val Beginner_Full_Body_workout9 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        AustralianChinUp.copy(repetitionCount = "15-20"), KneePushUp.copy(repetitionCount = "15-20"), Squat.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "1 min 45 sec"), AustralianPullUp.copy(repetitionCount = "15-20"), BenchDip.copy(repetitionCount = "15-20"), KneeRaises.copy(repetitionCount = "15-20")
    ),
    idetifier = 91
)
val Beginner_Full_Body_workout10 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        NegativeChinUp.copy(repetitionCount = "6-8"), PushUp.copy(repetitionCount = "10"), Squat.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "1 min 45 sec"), PullUpHold.copy(repetitionCount = "15-20 sec"), NegativeDip.copy(repetitionCount = "8-10"), LegRaises.copy(repetitionCount = "8-10")
    ),
    idetifier = 92
)
val Beginner_Full_Body_workout11 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        NegativePullUp.copy(repetitionCount = "6-8"), PushUp.copy(repetitionCount = "10-12"), Squat.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "1 min 45 sec"), PullUpHold.copy(repetitionCount = "15-20 sec"), NegativeDip.copy(repetitionCount = "8-10"), LegRaises.copy(repetitionCount = "10")
    ),
    idetifier = 93
)
val Beginner_Full_Body_workout12 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        NegativeChinUp.copy(repetitionCount = "4-6"), PushUp.copy(repetitionCount = "10-12"), Squat.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "1 min 45 sec"), PullUpHold.copy(repetitionCount = "15 sec"), BenchDip.copy(repetitionCount = "15-20"), HangingKneeRaises.copy(repetitionCount = "15-20")
    ),
    idetifier = 94
)
val Beginner_Full_Body_workout13 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PullUp.copy(repetitionCount = "max"), PushUp.copy(repetitionCount = "max"), Squat.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "1 min 45 sec"), ChinUpHold.copy(repetitionCount = "15-20 sec"), NegativeDip.copy(repetitionCount = "10"), LegRaises.copy(repetitionCount = "10-12")
    ),
    idetifier = 95
)
val Beginner_Full_Body_workout14 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ChinUp.copy(repetitionCount = "max"), PushUp.copy(repetitionCount = "max"), Squat.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "1 min 45 sec"), PullUpHold.copy(repetitionCount = "15-20 sec"), NegativeDip.copy(repetitionCount = "10-12"), LegRaises.copy(repetitionCount = "10-12")
    ),
    idetifier = 96
)
val Beginner_Full_Body_workout15 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PullUp.copy(repetitionCount = "6-8"), PushUp.copy(repetitionCount = "12-15"), Squat.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "1 min 45 sec"), ChinUpHold.copy(repetitionCount = "15-20 sec"), NegativeDip.copy(repetitionCount = "10-12"), LegRaises.copy(repetitionCount = "12")
    ),
    idetifier = 97
)
val Beginner_Full_Body_workout16 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ChinUp.copy(repetitionCount = "6-8"), PushUp.copy(repetitionCount = "12-15"), Squat.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "2 min"), PullUpHold.copy(repetitionCount = "15-20 sec"), Dip.copy(repetitionCount = "6-8"), LegRaises.copy(repetitionCount = "12")
    ),
    idetifier = 98
)
val Beginner_Full_Body_workout17 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PullUp.copy(repetitionCount = "max"), Dip.copy(repetitionCount = "max"), Squat.copy(repetitionCount = "15-20"),
        Plank.copy(repetitionCount = "2 min"), NegativeChinUp.copy(repetitionCount = "6-8"), PushUp.copy(repetitionCount = "12-15"), HangingLegRaises.copy(repetitionCount = "10-12")
    ),
    idetifier = 99
)
val Beginner_Full_Body_workout18 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ChinUp.copy(repetitionCount = "8-10"), PushUp.copy(repetitionCount = "12-15"), Squat.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "2 min"), NegativePullUp.copy(repetitionCount = "6-8"), Dip.copy(repetitionCount = "8-10"), HangingLegRaises.copy(repetitionCount = "10-12")
    ),
    idetifier = 100
)
val Beginner_Full_Body_workout19 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PullUp.copy(repetitionCount = "8-10"), DeepPushUp.copy(repetitionCount = "8-10"), Squat.copy(repetitionCount = "20-25"),
        Plank.copy(repetitionCount = "2 min"), ChinUp.copy(repetitionCount = "8-10"), Dip.copy(repetitionCount = "10-12"), HangingLegRaises.copy(repetitionCount = "12")
    ),
    idetifier = 101
)
val Beginner_Full_Body_workout20 = Workout(
    name = "Full Body Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        ChinUp.copy(repetitionCount = "10-12"), PushUp.copy(repetitionCount = "15-20"), Squat.copy(repetitionCount = "20"),
        Plank.copy(repetitionCount = "2 min"), PullUp.copy(repetitionCount = "10-12 sec"), Dip.copy(repetitionCount = "10-12"), HangingLegRaises.copy(repetitionCount = "12-15")
    ),
    idetifier = 102
)
val Beginner_Leg_workout1 = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Squat.copy(repetitionCount = "10-12"), Lunges.copy(repetitionCount = "10-12 each"), BoxStepsLvl1.copy(repetitionCount = "10-12 each"),
        SemiSquat.copy(repetitionCount = "10-12"), GluteBridge.copy(repetitionCount = "10-12"), CalfRaise.copy(repetitionCount = "20-25")
    ),
    idetifier = 103
)
val Beginner_Leg_workout2 = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        BoxJumpsLvl1.copy(repetitionCount = "10-12"), Squat.copy(repetitionCount = "10-12"), WallSit.copy(repetitionCount = "10-15 sec"),
        SemiSquat.copy(repetitionCount = "10-12"), GluteBridgeHold.copy(repetitionCount = "20-25 sec"), CalfRaise.copy(repetitionCount = "25-30")
    ),
    idetifier = 104
)
val Beginner_Leg_workout3 = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 3,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Lunges.copy(repetitionCount = "10-12 each"), BoxJumpsLvl1.copy(repetitionCount = "10-12"), Squat.copy(repetitionCount = "10-12"),
        SemiSquat.copy(repetitionCount = "12-15"), GluteBridgeHold.copy(repetitionCount = "20-25 sec"), CalfRaise.copy(repetitionCount = "25-30")
    ),
    idetifier = 105
)
val Beginner_Leg_workout4 = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Squat.copy(repetitionCount = "10-12"), SemiSquat.copy(repetitionCount = "12-15"), BoxJumpsLvl1.copy(repetitionCount = "10-12"),
        BoxStepsLvl1.copy(repetitionCount = "10-12 each"), GluteBridge.copy(repetitionCount = "12-15"), SingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 106
)
val Beginner_Leg_workout5 = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Squat.copy(repetitionCount = "12-15"), Lunges.copy(repetitionCount = "12 each"), BoxJumpsLvl1.copy(repetitionCount = "10-12"),
        WallSit.copy(repetitionCount = "15-20 sec"), GluteBridge.copy(repetitionCount = "15-20"), SingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 107
)
val Beginner_Leg_workout6 = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WallSit.copy(repetitionCount = "15-20 sec"), Lunges.copy(repetitionCount = "12 each"), BoxJumpsLvl1.copy(repetitionCount = "10-12"),
        Squat.copy(repetitionCount = "10-12"), GluteBridgeHold.copy(repetitionCount = "20-25 sec"), ElevatedCalfRaise.copy(repetitionCount = "20-25")
    ),
    idetifier = 108
)
val Beginner_Leg_workout7 = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        JumpSquat.copy(repetitionCount = "10-12"), Lunges.copy(repetitionCount = "12 each"), Squat.copy(repetitionCount = "10-12"),
        WideSquat.copy(repetitionCount = "10-12"), GluteBridge.copy(repetitionCount = "20-25"), ElevatedCalfRaise.copy(repetitionCount = "20-25")
    ),
    idetifier = 109
)
val Beginner_Leg_workout8 = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Squat.copy(repetitionCount = "12-15"), SemiPistolSquatLvl1.copy(repetitionCount = "8-10 each"), WallSit.copy(repetitionCount = "15-20 sec"),
        BoxStepsLvl1.copy(repetitionCount = "12-15 each"), GluteBridgeHold.copy(repetitionCount = "25-30 sec"), SingleLegCalfRaise.copy(repetitionCount = "12-15 each")
    ),
    idetifier = 110
)
val Beginner_Leg_workout9 = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WallSit.copy(repetitionCount = "20-25 sec"), JumpSquat.copy(repetitionCount = "10-12"), BoxJumpsLvl1.copy(repetitionCount = "10-12"),
        Lunges.copy(repetitionCount = "12 each"), WideSquat.copy(repetitionCount = "8-10"), SingleLegCalfRaise.copy(repetitionCount = "12-15 each")
    ),
    idetifier = 111
)
val Beginner_Leg_workout10 = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        SemiPistolSquatLvl1.copy(repetitionCount = "10-12 each"), Squat.copy(repetitionCount = "12"), Lunges.copy(repetitionCount = "12 each"),
        BoxJumpsLvl1.copy(repetitionCount = "10-12"), WallSit.copy(repetitionCount = "20-25 sec"), SingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 112
)
val Beginner_Leg_workout1N = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        JumpSquat.copy(repetitionCount = "10-12"), SemiPistolSquatLvl1.copy(repetitionCount = "10-12 each"), WallSit.copy(repetitionCount = "20-25 sec"),
        BoxJumpsLvl1.copy(repetitionCount = "10-12"), GluteBridge.copy(repetitionCount = "20-25"), SingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 113
)
val Beginner_Leg_workout2N = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        SemiPistolSquatLvl1.copy(repetitionCount = "10-12 each"), BoxJumpsLvl1.copy(repetitionCount = "10-12"), WallSit.copy(repetitionCount = "20-25 sec"),
        Lunges.copy(repetitionCount = "10-12 each"), Squat.copy(repetitionCount = "10-12"), ElevatedCalfRaise.copy(repetitionCount = "20-25")
    ),
    idetifier = 114
)
val Beginner_Leg_workout3N = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        JumpSquat.copy(repetitionCount = "10-12"), WideSquat.copy(repetitionCount = "10-12"), Lunges.copy(repetitionCount = "12 each"),
        BoxStepsLvl1.copy(repetitionCount = "10-12 each"), WallSit.copy(repetitionCount = "20-25 sec"), ElevatedCalfRaise.copy(repetitionCount = "20-25")
    ),
    idetifier = 115
)
val Beginner_Leg_workout4N = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 5,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        BoxJumpsLvl1.copy(repetitionCount = "10-12"), WallSit.copy(repetitionCount = "20-25 sec"), Lunges.copy(repetitionCount = "12 each"),
        WideSquat.copy(repetitionCount = "10-12"), Squat.copy(repetitionCount = "12"), ElevatedCalfRaise.copy(repetitionCount = "25-30")
    ),
    idetifier = 116
)
val Beginner_Leg_workout5N = Workout(
    name = "Leg Workout",
    difficulty = "Beginner",
    type = "Normal",
    rounds = 5,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        SemiPistolSquatLvl1.copy(repetitionCount = "10-12 each"), JumpSquat.copy(repetitionCount = "10-12"), WallSit.copy(repetitionCount = "20-25 sec"),
        Squat.copy(repetitionCount = "10-12"), BoxStepsLvl1.copy(repetitionCount = "12-15 each"), SingleLegCalfRaise.copy(repetitionCount = "12-15 each")
    ),
    idetifier = 117
)
val Inter_Leg_workout1 = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        WideSquat.copy(repetitionCount = "12"), SemiPistolSquatLvl1.copy(repetitionCount = "10-12 each"), WallSit.copy(repetitionCount = "20-25 sec"),
        JumpSquat.copy(repetitionCount = "10-12"), SingleLegGluteBridge.copy(repetitionCount = "10-12 each"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 118
)
val Inter_Leg_workout2 = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        JumpSquat.copy(repetitionCount = "10-12"), BoxJumpsLvl2.copy(repetitionCount = "8-10 each"), WallSit.copy(repetitionCount = "20-25 sec"),
        Lunges.copy(repetitionCount = "12-15 each"), SingleLegGluteBridgeHold.copy(repetitionCount = "10-12 sec each"), SingleLegCalfRaise.copy(repetitionCount = "12-15 each")
    ),
    idetifier = 119
)
val Inter_Leg_workout3 = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Squat.copy(repetitionCount = "12-15"), BoxStepsLvl2.copy(repetitionCount = "8-10 each"), JumpSquat.copy(repetitionCount = "10-12 sec"),
        SemiPistolSquatLvl1.copy(repetitionCount = "10-12 each"), BoxStepsLvl1.copy(repetitionCount = "10-12"), SingleLegCalfRaise.copy(repetitionCount = "12-15 each")
    ),
    idetifier = 120
)
val Inter_Leg_workout4 = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        NarrowSquat.copy(repetitionCount = "10-12"), BoxJumpsLvl2.copy(repetitionCount = "8-10"), Lunges.copy(repetitionCount = "12 each"),
        JumpSquat.copy(repetitionCount = "10-12"), SemiPistolSquatLvl1.copy(repetitionCount = "10-12 each"), SingleLegCalfRaise.copy(repetitionCount = "12-15 each")
    ),
    idetifier = 121
)
val Inter_Leg_workout5 = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        BoxJumpsLvl2.copy(repetitionCount = "10-12"), SemiPistolSquatLvl1.copy(repetitionCount = "10-12 each"), JumpSquat.copy(repetitionCount = "10-12"),
        BoxStepsLvl2.copy(repetitionCount = "10-12 each"), WallSit.copy(repetitionCount = "25-30 sec"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 122
)
val Inter_Leg_workout6 = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        BulgSplitSquat.copy(repetitionCount = "8-10 each"), SemiPistolSquatLvl1.copy(repetitionCount = "10-12 each"), BoxJumpsLvl2.copy(repetitionCount = "10-12"),
        BoxStepsLvl2.copy(repetitionCount = "10-12 each"), SideWalk.copy(repetitionCount = "15-20 sec each"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 123
)
val Inter_Leg_workout7 = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        SemiPistolSquatLvl2.copy(repetitionCount = "8-10"), JumpSquat.copy(repetitionCount = "10-12"), WallSit.copy(repetitionCount = "30 sec"),
        BulgSplitSquat.copy(repetitionCount = "8-10 each"), WideSquat.copy(repetitionCount = "10-12"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 124
)
val Inter_Leg_workout8 = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        BoxJumpsLvl2.copy(repetitionCount = "10-12"), SideLunges.copy(repetitionCount = "6-8 each"), BulgSplitSquat.copy(repetitionCount = "10-12 each"),
        BoxStepsLvl2.copy(repetitionCount = "10-12 each"), SideWalk.copy(repetitionCount = "20-25 sec each"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 125
)
val Inter_Leg_workout9 = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        SemiPistolSquatLvl2.copy(repetitionCount = "8-10 each"), SemiPistolSquatLvl1.copy(repetitionCount = "8-10 each"), NarrowSquat.copy(repetitionCount = "10-12"),
        BoxStepsLvl2.copy(repetitionCount = "10-12 each"), WallSit.copy(repetitionCount = "25-30 sec"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "12-15 each")
    ),
    idetifier = 126
)
val Inter_Leg_workout10 = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Circuit",
    rounds = 5,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        Squat.copy(repetitionCount = "12-15"), SideLunges.copy(repetitionCount = "8-10 each"), BulgSplitSquat.copy(repetitionCount = "10-12 each"),
        WideSquat.copy(repetitionCount = "10-12"), WallSit.copy(repetitionCount = "30-35 sec"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "12-15 each")
    ),
    idetifier = 127
)
val Inter_Leg_workout1N = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        SemiPistolSquatLvl2.copy(repetitionCount = "8-10 each"), NarrowSquat.copy(repetitionCount = "10-12"), BoxStepsLvl2.copy(repetitionCount = "10-12 each"),
        JumpSquat.copy(repetitionCount = "10-12"), WallSit.copy(repetitionCount = "25-30 sec"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 128
)
val Inter_Leg_workout2N = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        SideLunges.copy(repetitionCount = "8-10 each"), BulgSplitSquat.copy(repetitionCount = "10-12 each"), BoxJumpsLvl2.copy(repetitionCount = "10-12"),
        WideSquat.copy(repetitionCount = "10-12"), SideWalk.copy(repetitionCount = "20-25 sec each"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 129
)
val Inter_Leg_workout3N = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        SemiPistolSquatLvl2.copy(repetitionCount = "10-12 each"), SemiPistolSquatLvl1.copy(repetitionCount = "10-12 each"), NarrowSquat.copy(repetitionCount = "10-12"),
        WallSit.copy(repetitionCount = "30-35 sec"), Squat.copy(repetitionCount = "12-15"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 130
)
val Inter_Leg_workout4N = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 5,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        SideLunges.copy(repetitionCount = "8-10 each"), BulgSplitSquat.copy(repetitionCount = "10-12 each"), BoxStepsLvl2.copy(repetitionCount = "10-12 each"),
        WideSquat.copy(repetitionCount = "10-12"), SideWalk.copy(repetitionCount = "20-25 sec each"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 131
)
val Inter_Leg_workout5N = Workout(
    name = "Leg Workout",
    difficulty = "Intermediate",
    type = "Normal",
    rounds = 5,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        SemiPistolSquatLvl2.copy(repetitionCount = "8-10 each"), BulgSplitSquat.copy(repetitionCount = "10-12 each"), NarrowSquat.copy(repetitionCount = "10-12"),
        BoxStepsLvl2.copy(repetitionCount = "10-12 each"), WallSit.copy(repetitionCount = "30-35 sec"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "10-12 each")
    ),
    idetifier = 132
)
val Advanced_Leg_workout1 = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PistolSquat.copy(repetitionCount = "6-8 each"), BoxJumpsLvl2.copy(repetitionCount = "10-12"), BulgSplitSquat.copy(repetitionCount = "10-12 each"),
        SideLunges.copy(repetitionCount = "6-8 each"), WallSit.copy(repetitionCount = "30-35 sec"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "12-15 each")
    ),
    idetifier = 133
)
val Advanced_Leg_workout2 = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        SideLunges.copy(repetitionCount = "6-8 each"), BoxStepsLvl2.copy(repetitionCount = "10-12 each"), BulgSplitSquat.copy(repetitionCount = "10-12 each"),
        SideWalk.copy(repetitionCount = "25-30 each"), NarrowSquat.copy(repetitionCount = "12-15"), JumpSquat.copy(repetitionCount = "12-15")
    ),
    idetifier = 134
)
val Advanced_Leg_workout3 = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PistolSquat.copy(repetitionCount = "6-8 each"), WideSquat.copy(repetitionCount = "12-15"), WallSit.copy(repetitionCount = "30-35 sec"),
        Lunges.copy(repetitionCount = "12-15 each"), Squat.copy(repetitionCount = "12-15"), BoxStepsLvl2.copy(repetitionCount = "10 each"),
        ElevatedSingleLegCalfRaise.copy(repetitionCount = "12-15 each")
    ),
    idetifier = 135
)
val Advanced_Leg_workout4 = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        BulgSplitSquat.copy(repetitionCount = "10-12 each"), JumpSquat.copy(repetitionCount = "12-15"), SissySquat.copy(repetitionCount = "6-8"),
        SideLunges.copy(repetitionCount = "6-8 each"), SideWalk.copy(repetitionCount = "25-30 sec each"),
        ElevatedSingleLegCalfRaise.copy(repetitionCount = "15 each")
    ),
    idetifier = 136
)
val Advanced_Leg_workout5 = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PistolSquat.copy(repetitionCount = "6-8 each"), BoxStepsLvl2.copy(repetitionCount = "10-12 each"), BulgSplitSquat.copy(repetitionCount = "10-12 each"),
        WideSquat.copy(repetitionCount = "10-12"), Squat.copy(repetitionCount = "12-15"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "15 each")
    ),
    idetifier = 137
)
val Advanced_Leg_workout7 = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        SemiPistolSquatLvl2.copy(repetitionCount = "12-15 each"), SideLunges.copy(repetitionCount = "8 each"), WallSit.copy(repetitionCount = "40-45 sec"),
        BoxJumpsLvl2.copy(repetitionCount = "12-15"), Squat.copy(repetitionCount = "15-20"), SingleLegGluteBridge.copy(repetitionCount = "20 each"),
        ElevatedSingleLegCalfRaise.copy(repetitionCount = "15 each")
    ),
    idetifier = 199
)
val Advanced_Leg_workout6 = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PistolSquat.copy(repetitionCount = "8 each"), SideLunges.copy(repetitionCount = "8-10 each"), BulgSplitSquat.copy(repetitionCount = "12 each"),
        JumpSquat.copy(repetitionCount = "15"), BoxStepsLvl2.copy(repetitionCount = "15 each"), SingleLegGluteBridge.copy(repetitionCount = "15 each"),
        ElevatedSingleLegCalfRaise.copy(repetitionCount = "15 each")
    ),
    idetifier = 198
)
val Advanced_Leg_workout8 = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        SissySquat.copy(repetitionCount = "8-10"), BulgSplitSquat.copy(repetitionCount = "12-15 each"), NarrowSquat.copy(repetitionCount = "15"),
        SideWalk.copy(repetitionCount = "30-35 sec"), Squat.copy(repetitionCount = "15-20"), SingleLegGluteBridgeHold.copy(repetitionCount = "30 sec each"),
        ElevatedSingleLegCalfRaise.copy(repetitionCount = "20 each")
    ),
    idetifier = 200
)
val Advanced_Leg_workout9 = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PistolSquat.copy(repetitionCount = "10-12 each"), BoxJumpsLvl2.copy(repetitionCount = "15"), WallSit.copy(repetitionCount = "45 sec"),
        SideWalk.copy(repetitionCount = "30-35 sec"), Squat.copy(repetitionCount = "15-20"), SingleLegGluteBridge.copy(repetitionCount = "20 each"),
        ElevatedSingleLegCalfRaise.copy(repetitionCount = "20 each")
    ),
    idetifier = 201
)
val Advanced_Leg_workout10 = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Circuit",
    rounds = 4,
    rest = "60 sec between exercises, 3 min between rounds",
    position = 0,
    exercises = listOf(
        PistolSquat.copy(repetitionCount = "10-12 each"), SissySquat.copy(repetitionCount = "10-12"), WallSit.copy(repetitionCount = "50 sec"),
        NarrowSquat.copy(repetitionCount = "15-20"), BoxJumpsLvl2.copy(repetitionCount = "15-20"), SingleLegGluteBridgeHold.copy(repetitionCount = "40 sec each"),
        ElevatedSingleLegCalfRaise.copy(repetitionCount = "25 each")
    ),
    idetifier = 202
)
val Advanced_Leg_workout1N = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        PistolSquat.copy(repetitionCount = "6-8 each"), SemiPistolSquatLvl2.copy(repetitionCount = "10-12 each"), WallSit.copy(repetitionCount = "40 sec"),
        SideLunges.copy(repetitionCount = "8 each"), Squat.copy(repetitionCount = "15"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "15 each")
    ),
    idetifier = 280
)
val Advanced_Leg_workout2N = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        SissySquat.copy(repetitionCount = "6-8"), NarrowSquat.copy(repetitionCount = "15"), WallSit.copy(repetitionCount = "45 sec"),
        SideLunges.copy(repetitionCount = "8 each"), JumpSquat.copy(repetitionCount = "15"), ElevatedSingleLegCalfRaise.copy(repetitionCount = "15 each")
    ),
    idetifier = 281
)
val Advanced_Leg_workout3N = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        PistolSquat.copy(repetitionCount = "10 each"), SideLunges.copy(repetitionCount = "8-10 each"), SideWalk.copy(repetitionCount = "35 sec"),
        BoxJumpsLvl2.copy(repetitionCount = "15"), WideSquat.copy(repetitionCount = "15"), SingleLegGluteBridgeHold.copy(repetitionCount = "45 sec each"),
        ElevatedSingleLegCalfRaise.copy(repetitionCount = "20 each")
    ),
    idetifier = 282
)
val Advanced_Leg_workout4N = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        PistolSquat.copy(repetitionCount = "10 each"), SemiPistolSquatLvl2.copy(repetitionCount = "15 each"), WallSit.copy(repetitionCount = "50 sec"),
        BulgSplitSquat.copy(repetitionCount = "15 each"), JumpSquat.copy(repetitionCount = "15-20"), SingleLegGluteBridgeHold.copy(repetitionCount = "20-25 each"),
        ElevatedSingleLegCalfRaise.copy(repetitionCount = "20 each")
    ),
    idetifier = 283
)
val Advanced_Leg_workout5N = Workout(
    name = "Leg Workout",
    difficulty = "Advanced",
    type = "Normal",
    rounds = 4,
    rest = "1-2 min",
    position = 0,
    exercises = listOf(
        PistolSquat.copy(repetitionCount = "12 each"), SissySquat.copy(repetitionCount = "12"), WallSit.copy(repetitionCount = "60 sec"),
        BoxJumpsLvl2.copy(repetitionCount = "20"), Squat.copy(repetitionCount = "25"), SingleLegGluteBridgeHold.copy(repetitionCount = "25 each"),
        ElevatedSingleLegCalfRaise.copy(repetitionCount = "25 each")
    ),
    idetifier = 284
)

