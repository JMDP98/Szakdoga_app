import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class WeightClass( val weight: Double, val date: Long) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(weight)
        parcel.writeLong(date)
    }

    override fun describeContents(): Int {
        return 0
    }
    fun formattedDate(): String {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        return dateFormat.format(Date(date))
    }

    companion object CREATOR : Parcelable.Creator<WeightClass> {
        override fun createFromParcel(parcel: Parcel): WeightClass {
            return WeightClass(parcel)
        }

        override fun newArray(size: Int): Array<WeightClass?> {
            return arrayOfNulls(size)
        }
    }
}
