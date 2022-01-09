package ru.sasha77.orthotester

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class del {
    @Test
    fun addition_isCorrect() {
        assert(true)
        assertEquals(-0.5F, compute(110F,123F))
        assertEquals(14.5F, compute(39F,35F))
        assertEquals(-6.5F, compute(121F,121F+33F))
        try {
            compute(38F,123F)
            assert(false)
        } catch (e : IllegalArgumentException) {
            assertEquals(e.message,"pulse-low")
        }
        try {
            compute(122F,123F)
            assert(false)
        } catch (e : IllegalArgumentException) {
            assertEquals(e.message,"pulse-high")
        }
        try {
            compute(39F,34F)
            assert(false)
        } catch (e : IllegalArgumentException) {
            assertEquals(e.message,"delta-low")
        }
        try {
            compute(121F,121F+34F)
            assert(false)
        } catch (e : IllegalArgumentException) {
            assertEquals(e.message,"delta-high")
        }
    }
}
