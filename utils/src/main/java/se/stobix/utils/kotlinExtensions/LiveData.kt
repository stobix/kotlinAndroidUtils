package se.stobix.utils.kotlinExtensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations

fun <A,B> LiveData<List<A>>.switchMap(f: (a:List<A>) -> LiveData<List<B>>) =
        Transformations.switchMap(this,f)


fun <A,B> LiveData<List<A>>.map(f: (a:List<A>) -> List<B>) =
        Transformations.map(this,f)


fun <A,B> LiveData<List<A>>.fmap(f: (a:A) -> B) =
        Transformations.map(this){it.map{a -> f(a)}}

fun <A,B> LiveData<List<A>>.frun(f: A.() -> B) =
        Transformations.map(this){it.map{a -> a.run(f)}}

