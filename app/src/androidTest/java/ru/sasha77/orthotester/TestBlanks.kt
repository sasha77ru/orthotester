package ru.sasha77.orthotester


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.lang.Exception
import ru.sasha77.orthotester.OnlyDate.*

@LargeTest
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestBlanks {

    @Before
    fun before () {
        try {onView(allOf(withId(R.id.fab), isDisplayed())).perform(click())} catch (e : Exception) {}
        pressBack()
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MA::class.java)

    @Test
    fun tst001_inflateDB () {
        clearDB(mActivityTestRule)
        mActivityTestRule.activity.sq.tstInsert(8.daysAgo(ONLY_DATE),8.daysAgo(),50.0F,55.0F,14.0F)
        mActivityTestRule.activity.sq.tstInsert(5.daysAgo(ONLY_DATE),8.daysAgo(),50.0F,55.0F,13.0F)
        mActivityTestRule.activity.sq.tstInsert(4.daysAgo(ONLY_DATE),4.daysAgo(),50.0F,55.0F,12.0F)
        mActivityTestRule.activity.sq.tstInsert(2.daysAgo(ONLY_DATE),2.daysAgo(),50.0F,55.0F,11.0F)
    }

    @Test
    fun tst002_testCheckTable () {
        testCheckTable (listOf("","11.0","","12.0","13.0","","","14.0"))
    }

    @Test
    fun tst003_inflateDB () {
        clearDB(mActivityTestRule)
        mActivityTestRule.activity.sq.tstInsert(2.daysAgo(ONLY_DATE),8.daysAgo(),50.0F,55.0F,14.0F)
        mActivityTestRule.activity.sq.tstInsert(2.daysAgo(ONLY_DATE),8.daysAgo(),50.0F,55.0F,13.0F)
        mActivityTestRule.activity.sq.tstInsert(1.daysAgo(ONLY_DATE),4.daysAgo(),50.0F,55.0F,12.0F)
        mActivityTestRule.activity.sq.tstInsert(1.daysAgo(ONLY_DATE),2.daysAgo(),50.0F,55.0F,11.0F)
    }

    @Test
    fun tst004_testCheckTable () {
        testCheckTable (listOf("11.0","12.0","13.0","14.0"))
    }

    @Test
    fun tst005_inflateDB () {
        clearDB(mActivityTestRule)
        mActivityTestRule.activity.sq.tstInsert(2.daysAgo(ONLY_DATE),8.daysAgo(),50.0F,55.0F,14.0F)
        mActivityTestRule.activity.sq.tstInsert(2.daysAgo(ONLY_DATE),8.daysAgo(),50.0F,55.0F,13.0F)
        mActivityTestRule.activity.sq.tstInsert(2.daysAgo(ONLY_DATE),4.daysAgo(),50.0F,55.0F,12.0F)
        mActivityTestRule.activity.sq.tstInsert(0.daysAgo(ONLY_DATE),2.daysAgo(),50.0F,55.0F,11.0F)
    }

    @Test
    fun tst006_testCheckTable () {
        testCheckTable (listOf("11.0","","12.0","13.0","14.0"))
    }
}
