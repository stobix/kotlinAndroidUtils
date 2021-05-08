package se.stobix.utils.kotlin.delegates

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KProperty

class LazyResource<A>(
    val f: suspend () -> A,
    private val default:A? = null,
    private val store: MutableLiveData<A> =
        when(default){
            null -> MutableLiveData<A>()
            else -> MutableLiveData(default)
        }){

    private var gotData = false

    operator fun getValue(thing: Any,property: KProperty<*>): LiveData<A> {
        if(!gotData){
            gotData = true
            runBlocking {
                try {
                    store.postValue(f())
                    Log.d("LazyResource","Got ${property.name}")
                } catch (err: Throwable) {
                    gotData = false
                    Log.e("LazyResource","Error when getting ${property.name}: ${err.stackTraceToString()}")
                }
            }
        }
        return store
    }
}

fun <A>lazyResource(f: suspend () -> A) = LazyResource(f)

fun <A>lazyResource(default:A, f: suspend () -> A) = LazyResource(f,default)
