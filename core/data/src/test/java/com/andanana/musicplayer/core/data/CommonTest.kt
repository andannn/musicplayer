package com.andanana.musicplayer.core.data

import com.andanana.musicplayer.core.data.model.LibraryRootCategory
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CommonTest {
    @Test
    fun addition_isCorrect() {
        val (type, id) = LibraryRootCategory.getMatchedChildTypeAndId("album_12343") ?: error("")

        assertEquals(LibraryRootCategory.ALBUM, type)
        assertEquals(12343, id)

        assertEquals(null,  LibraryRootCategory.getMatchedChildTypeAndId("XXXalbum_12343") )
    }
}