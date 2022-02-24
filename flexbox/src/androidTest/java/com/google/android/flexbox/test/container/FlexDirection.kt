package com.google.android.flexbox.test.container

import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.PositionAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.FlakyTest
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.test.IsEqualAllowingError
import com.google.android.flexbox.test.LayoutConfiguration
import com.google.android.flexbox.test.R
import com.google.android.flexbox.test.dpToPixel
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Test

class FlexDirection : FlexboxTestBase() {
    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexDirection_row_alignItems_center_margin_oneSide() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_direction_row_align_items_center_margin_oneside)

        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.ROW))

        val text1 = activity.findViewById<TextView>(R.id.text1)
        Assert.assertThat(text1.top, IsEqualAllowingError.isEqualAllowingError(activity.dpToPixel(30)))
        Assert.assertThat(flexboxLayout.bottom - text1.bottom, IsEqualAllowingError.isEqualAllowingError(activity.dpToPixel(50)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexDirection_column_alignItems_center_margin_oneSide() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_direction_column_align_items_center_margin_oneside)

        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))

        val text1 = activity.findViewById<TextView>(R.id.text1)
        Assert.assertThat(text1.left, IsEqualAllowingError.isEqualAllowingError(activity.dpToPixel(30)))
        Assert.assertThat(flexboxLayout.right - text1.right, IsEqualAllowingError.isEqualAllowingError(activity.dpToPixel(50)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexDirection_row_reverse() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_wrap_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.ROW_REVERSE
                    }
                })

        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.ROW_REVERSE))

        // The layout direction should be right to left
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isRightAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isRightAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexDirection_column_reverse() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_wrap_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN_REVERSE
                    }
                })

        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN_REVERSE))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
    }
}