package com.google.android.flexbox.test.container

import android.view.View
import androidx.annotation.LayoutRes
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.test.FlexboxTestActivity
import com.google.android.flexbox.test.LayoutConfiguration
import com.google.android.flexbox.test.R
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule

open class FlexboxTestBase {
    @JvmField
    @Rule
    var activityRule = ActivityTestRule(FlexboxTestActivity::class.java)

    @Throws(Throwable::class)
    fun createFlexboxLayout(@LayoutRes activityLayoutResId: Int,
                            configuration: LayoutConfiguration = LayoutConfiguration.EMPTY): FlexboxLayout {
        val activity = activityRule.activity
        activityRule.runOnUiThread {
            activity.setContentView(activityLayoutResId)
            val flexboxLayout = activity.findViewById<FlexboxLayout>(R.id.flexbox_layout)
            configuration.apply(flexboxLayout)
        }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        return activity.findViewById<View>(R.id.flexbox_layout) as FlexboxLayout
    }

    fun hasWidth(width: Int): ViewAssertion {
        return ViewAssertions.matches(object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("expected width: $width")
            }

            override fun describeMismatchSafely(item: View, mismatchDescription: Description) {
                mismatchDescription.appendText("actual width: " + item.width)
            }

            override fun matchesSafely(item: View) = item.width == width
        })
    }

    fun hasHeight(height: Int): ViewAssertion {
        return ViewAssertions.matches(object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("expected height: $height")
            }

            override fun describeMismatchSafely(item: View, mismatchDescription: Description) {
                mismatchDescription.appendText("actual height: " + item.height)
            }

            override fun matchesSafely(item: View) = item.height == height
        })
    }
}