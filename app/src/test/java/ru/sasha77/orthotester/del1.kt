package ru.sasha77.orthotester

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class del1 {
    @Test
    fun addition_isCorrect() {
        val gc = GregorianCalendar()
        println(SimpleDateFormat("yyyy-MM-dd").format(Date(Date().time+86400000)))
        assert(true)
    }
}
