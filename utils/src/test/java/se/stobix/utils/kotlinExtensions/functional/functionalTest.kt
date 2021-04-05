package se.stobix.utils.kotlinExtensions.functional

import org.junit.Assert
import org.junit.Test

private infix fun <A> A.asEq(a: A) = Assert.assertEquals(this, a)
private infix fun <A> A.asNEq(a: A) = Assert.assertNotEquals(this, a)

class functionalTest {
    @Test
    fun eitherInit(){
        var e: Either<Int, String> = Either.right(3)
        Assert.assertNull(e.left)
        e.right asEq 3
        e= Either.left("7")
        Assert.assertNull(e.right)
        e.left asEq "7"
    }
}