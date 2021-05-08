package se.stobix.utils.kotlin.delegates

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.reflect.KProperty

class StringPreference(
    private val prefs: SharedPreferences,
    private val default: String = ""
){
    constructor(application: Application, prefsName: String = "delegatePrefs",
                default: String = ""
    ): this(
        application.getSharedPreferences(prefsName, Context.MODE_PRIVATE),
        default
    )
    operator fun getValue(thing: Any, property: KProperty<*>): String =
        prefs.getString(property.name, default) ?: default

    operator fun setValue(thing: Any, property: KProperty<*>, value: String) =
        prefs.edit { putString(property.name,value) }
}

class BoolPreference(
    private val prefs: SharedPreferences,
    private val default: Boolean = false
){
    constructor(application: Application, prefsName: String = "delegatePrefs",
                default: Boolean = false
    ): this(
        application.getSharedPreferences(prefsName, Context.MODE_PRIVATE),
        default
    )
    operator fun getValue(thing: Any, property: KProperty<*>) =
        prefs.getBoolean(property.name, default)

    operator fun setValue(thing: Any, property: KProperty<*>, value: Boolean) =
        prefs.edit { putBoolean(property.name,value) }
}

fun SharedPreferences.boolPreference(default: Boolean = false) = BoolPreference(this,default)
fun SharedPreferences.stringPreference(default: String = "") = StringPreference(this,default)

val SharedPreferences.stringPreference
    get() = StringPreference(this)

val SharedPreferences.boolPreference
    get() = BoolPreference(this)
