package se.stobix.utils

import android.os.Parcel
import android.os.Parcelable
import se.stobix.utils.kotlinExtensions.to
import java.text.DateFormat
import java.util.*

/**
 * A class to simplify and abstract time handling tasks
 */

@Suppress("DestructuringWrongName")
class DateHandler() : Parcelable {

    private val calendar: Calendar = Calendar.getInstance() // TODO Localize!

    init {
        calendar.firstDayOfWeek = Calendar.MONDAY
    }

    constructor(timestamp: Long) : this() {
        calendar.timeInMillis = timestamp
    }

    constructor(datestr: String) : this(){
        calendar.timeInMillis = DateFormat.getDateInstance().parse(datestr).time
    }

    /**
     * Set the time
     */
    fun setTime(hour: Int, minute: Int, second: Int = 0): DateHandler {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        return this
    }

    /**
     * Set the date, with a 0-indexed month because reasons
     */
    fun setDate0(year: Int, month0: Int, day: Int): DateHandler {
        calendar.set(year, month0, day)
        return this
    }

    /**
     * A <year,month 0-11,day> Triple
     */
    var date0
        get() = Triple(year, month0, day)
        set(value) {
            val (year, month, day) = value
            setDate0(year, month, day)
        }

    /**
     * A <year,month 1-12,day> Triple
     */
    var date
        get() = Triple(year, month, day)
        set(value) {
            val (year, month, day) = value
            setDate0(year, month-1, day)
        }

    private val Int.as2String
        get() = this.toString().padStart(2,'0').substring(0,2)

    private val Int.as4String
        get() = this.toString().padStart(4,'0').substring(0,4)

    /**
     * Returns a string of the form yy-mm-dd with 0 padded m and d
     */
    val asYymmddString
        get() = "${year.as2String}-${month.as2String}-${day.as2String}"

    /**
     * Returns a string of the form yy-mm-dd with 0 padded m and d
     * Not ready yet.
     */
    val asYyyymmddString
        get() = "${year.as4String}-${month.as2String}-${day.as2String}"
    /**
     * A <hour,minute> Pair
     */
    var time
        get() = Pair(hour , minute)
        set(value){
            val (hour, minute) = value
            setTime(hour, minute)
        }

    /**
     * A <hour,minute,second> Triple
     */
    var hms
        get() = hour to minute to second
        set(value){
            val (hour, minute,second) = value
            setTime(hour, minute,second)
        }

    /**
     * The day the week starts with. Monday is 0
     */
    var weekStartsWith
        get() = (calendar.firstDayOfWeek+5)%7
        set(weekDay) {
            calendar.firstDayOfWeek = (weekDay+2)%7
        }

    /**
     * Day of week, monday=0 to sunday=6
     */
    var weekDay //
        get() = calendar.get(Calendar.DAY_OF_WEEK)
                .minus(2) // The week does not begin with a saturday, but a monday
                .plus(7) // Ensure the values are positive, since rem is broken
                .rem(7) // Assign each week day a sensible, zero-indexed, value
        set(value) {
            calendar.set(
                    Calendar.DAY_OF_WEEK,
                    (value+2)%7
            )
        }

    var week
        get() = calendar.get(Calendar.WEEK_OF_YEAR)
        set(value) =
            calendar.set(Calendar.WEEK_OF_YEAR,value)

    /**
     * Gets the underlying Date object
     */
    val dateObject: Date
        get() = calendar.time

    /**
     * Gets the hour of the day
     */
    var hour
        get() = calendar.get(Calendar.HOUR_OF_DAY)
        set(value) {
            calendar.set(Calendar.HOUR_OF_DAY, value)
        }

    /**
     * Returns the minute of the hour of the day
     */
    var minute
        get() = calendar.get(Calendar.MINUTE)
        set(value) {
            calendar.set(Calendar.MINUTE, value)
        }

    /**
     * Return the second of the minute of the hour of the day
     */
    var second
        get() = calendar.get(Calendar.SECOND)
        set(value) {
            calendar.set(Calendar.SECOND, value)
        }

    /**
     * Return the millisecond of the second of the minute of the hour of the day
     */
    var ms
        get() = calendar.get(Calendar.MILLISECOND)
        set(value) {
            calendar.set(Calendar.MILLISECOND, value)
        }

    /**
     * Return the year
     */
    var year
        get() = calendar.get(Calendar.YEAR)
        set(value) {
            calendar.set(Calendar.YEAR, value)
        }

    /**
     * Return the month, 0-11!
     */
    var month0
        get() = calendar.get(Calendar.MONTH)
        set(value) {
            calendar.set(Calendar.MONTH, value)
        }

    /**
     * Return the month, 1-12
     */
    var month
        get() = calendar.get(Calendar.MONTH)+1
        set(value) {
            calendar.set(Calendar.MONTH, value-1)
        }

    /**
     * Return the day of the month
     */
    var day
        get() = calendar.get(Calendar.DAY_OF_MONTH)
        set(value) {
            calendar.set(Calendar.DAY_OF_MONTH, value)
        }

    /**
     * "this calendars time value in milliseconds"
     */
    var timestamp
        get() = calendar.timeInMillis
        set(value) {
            calendar.timeInMillis = value
        }


    /**
     * Returns a <year,month,day,hour,minute,second> sextuple
     */
    var datetime
        get() = year to month to day to hour to minute to second
        set(value) {
            val (year, month, day,hour,minute,second) = value
            date = year to month to day
            hms = hour to minute to second
        }

    constructor(parcel: Parcel) : this(parcel.readLong())

    /**
     *  Returns a new copy of the DateHandler
     */
    fun clone() = DateHandler(calendar.timeInMillis)

    // These are used in "dot sequences", eg dateHandler.subtractHours(3).addDays(6)

    @Suppress("unused")
    fun addHours(hours: Int) = addThing(Calendar.HOUR_OF_DAY, hours)

    @Suppress("unused")
    fun addMinutes(minutes: Int) = addThing(Calendar.MINUTE, minutes)

    @Suppress("unused")
    fun addDays(days: Int) = addThing(Calendar.DAY_OF_MONTH, days)

    @Suppress("unused")
    fun addMonths(months: Int) = addThing(Calendar.MONTH, months)

    @Suppress("unused")
    fun addYears(years: Int) = addThing(Calendar.YEAR, years)

    @Suppress("unused")
    fun subtractHours(hours: Int) = addThing(Calendar.HOUR_OF_DAY, -hours)

    @Suppress("unused")
    fun subtractMinutes(minutes: Int) = addThing(Calendar.MINUTE, -minutes)

    @Suppress("unused")
    fun subtractDays(days: Int) = addThing(Calendar.DAY_OF_MONTH, -days)

    @Suppress("unused")
    fun subtractMonths(months: Int) = addThing(Calendar.MONTH, -months)

    @Suppress("unused")
    fun subtractYears(years: Int) = addThing(Calendar.YEAR, -years)


    fun subtract(other: DateHandler) =
            DateHandler(this.timestamp-other.timestamp)

    fun add(other: DateHandler) =
            DateHandler(this.timestamp+other.timestamp)

    private fun addThing(thingField: Int, thing: Int): DateHandler {
        calendar.add(thingField, thing)
        return this
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    // TODO
    // operator fun compareTo

    companion object CREATOR : Parcelable.Creator<DateHandler> {
        override fun createFromParcel(parcel: Parcel): DateHandler {
            return DateHandler(parcel.readLong())
        }

        override fun newArray(size: Int): Array<DateHandler?> {
            return arrayOfNulls(size)
        }
    }
}
