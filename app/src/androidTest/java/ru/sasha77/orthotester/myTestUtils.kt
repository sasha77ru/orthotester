package ru.sasha77.orthotester

import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import ru.sasha77.orthotester.OnlyDate.*

fun clearDB (mActivityTestRule : ActivityTestRule<MA>) {
    mActivityTestRule.activity.sq.tstClearDB()
}

enum class OnlyDate {ONLY_DATE,WHOLE_DATE}
fun Int.daysAgo (onlyDate : OnlyDate = WHOLE_DATE) = SimpleDateFormat(when (onlyDate) {
    ONLY_DATE -> "yyyy-MM-dd"
    WHOLE_DATE -> "yyyy-MM-dd HH:mm:ss"
}).format(Date(Date().time-86400000*this)) ?: throw IllegalArgumentException()


fun testCheckTable (probes : List<String>) {

    probes.forEachIndexed { i,s ->
        println("Go with $s")
        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.textMeasProbe), ViewMatchers.withText(s),
                childAtPosition(
                    childAtPosition(
                        childAtPosition(
                            IsInstanceOf.instanceOf(ListView::class.java),
                            i
                        ),
                        0
                    ),
                    3
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText(s)))
    }

//        Thread.sleep(10000)

}

fun childAtPosition(
    parentMatcher: Matcher<View>, position: Int
): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Child at position $position in parent ")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent
            return parent is ViewGroup && parentMatcher.matches(parent)
                    && view == parent.getChildAt(position)
        }
    }
}