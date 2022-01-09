package ru.sasha77.orthotester


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.lang.Exception

@LargeTest
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MyTest {

    private var dialogMustDie = true

    @Before
    fun before () {
        clearDB(mActivityTestRule)
        try {onView(allOf(withId(R.id.fab), isDisplayed())).perform(click())} catch (e : Exception) {}
    }

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MA::class.java)

    @Test
    fun tst001_testWrong () {
        listOf(
            Triple("38","39",mActivityTestRule.activity.resources.getString(R.string.pulse_low)),
            Triple("122","121",mActivityTestRule.activity.resources.getString(R.string.pulse_high)),
            Triple("100","95",mActivityTestRule.activity.resources.getString(R.string.delta_high)),
            Triple("100","134",mActivityTestRule.activity.resources.getString(R.string.delta_high))
        ).forEach {
            onView(allOf(withId(R.id.editPulse1), isDisplayed())).perform(replaceText(it.first))
            onView(allOf(withId(R.id.editPulse2), isDisplayed())).perform(replaceText(it.second))
            onView(allOf(withId(R.id.computeButton), isDisplayed())).perform(click())
            //<editor-fold desc="Error text check">
            val textView = onView(
                allOf(
                    withId(R.id.textResult), withText(it.third),
                    childAtPosition(
                        childAtPosition(
                            IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                            0
                        ),
                        2
                    ),
                    isDisplayed()
                )
            )
            textView.check(ViewAssertions.matches(withText(it.third)))
            //</editor-fold>
        }
    }

    @Test
    fun tst002_testRight () {
        listOf("50", "55", "80", "100", "120").zipWithNext { a, b ->
            if (!dialogMustDie) onView(allOf(withId(R.id.fab), isDisplayed())).perform(click()) else dialogMustDie = false

            onView(allOf(withId(R.id.editPulse1), isDisplayed())).perform(replaceText(a))
            onView(allOf(withId(R.id.editPulse2), isDisplayed())).perform(replaceText(b))
            onView(allOf(withId(R.id.computeButton), isDisplayed())).perform(click())

            Thread.sleep(3000)
        }
        testCheckTable(listOf("-0.5","3.0","6.0","11.0"))
    }


}
