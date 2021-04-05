package se.stobix.utils.kotlinExtensions.functional

/**
 * The quintessential id function, that evaluate its parameter [a] to itself.
 */
fun <A> id(a: A) = a

/**
 * Returns a constant function that returns [b] on each call regardless of input.
 */
fun <B> const(b: B):(Any?)->B = {_ -> b}

/**
 * [const] as an infix function.
 */
infix fun <B> Any?.const(b: B):(Any?)->B = {_ -> b}



class Either<R, L> private constructor(l: L?, r: R?) {

    constructor(r:R):this(null as L?, r as R)

    private var pr:R? = r
    private var pl:L? = l

    var right:R?
        get() =
            pr

        set(value) {
            pl=null
            pr=value
        }

    var left:L?
        get() =
            pl
        set(value) {
            pr= null
            pl=value
        }

    val isLeft:Boolean = left != null

    val isRight:Boolean = right != null

    fun runIfLeft(f:(L) -> Unit) =
            this.also { left ?. let {f(it)} }

    fun runIfRight(f:(R) -> Unit) =
            this.also { right ?. let {f(it)} }

    companion object{
        fun <R,L>left(l: L): Either<R,L> =
                Either(l,null)

        fun <R,L>right(r: R): Either<R,L> =
                Either(null,r)
    }
}



