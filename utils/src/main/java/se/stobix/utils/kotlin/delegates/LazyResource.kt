package se.stobix.utils.kotlin.delegates

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KProperty

/**
 * A delegate that executes [f] once, sending its return value to the [LiveData] backing field
 * @param f the suspend function to call
 * @param default the default value until [f] has run
 * @param store the [MutableLiveData] backend. (Automagically created.)
 */
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
                    // Log.d("LazyResource","Got ${property.name}")
                } catch (err: Throwable) {
                    gotData = false
                    Log.e("LazyResource","Error when getting ${property.name}: ${err.stackTraceToString()}")
                }
            }
        }
        return store
    }
}

/**
 * Creates a [LazyResource] delegate with a backing [LiveData],
 *  executing [f] on the first access, asynchronously returning its value to the [LiveData]
 *  @param f A suspend function called exactly once.
 */
fun <A>lazyResource(f: suspend () -> A) = LazyResource(f)

/**
 * Creates a [LazyResource] delegate with a backing [LiveData],
 *  executing [f] on the first access, asynchronously returning its value to the [LiveData]
 *  @param f A suspend function called exactly once.
 *  @param default The default value for the [LiveData] until [f] has been successfully executed.
 */
fun <A>lazyResource(default:A, f: suspend () -> A) = LazyResource(f,default)
