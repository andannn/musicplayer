package com.andanana.musicplayer

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun lambda_equals() {
        val lambdaA = { string: String ->
            "stringA" + string
        }
        val lambdaB = { string: String ->
            "stringA" + string
        }
        assertEquals(true, lambdaA == lambdaB)
    }
}
