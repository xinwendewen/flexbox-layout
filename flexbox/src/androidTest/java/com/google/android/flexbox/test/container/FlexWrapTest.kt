package com.google.android.flexbox.test.container

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.PositionAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.FlakyTest
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.test.LayoutConfiguration
import com.google.android.flexbox.test.R
import com.google.android.flexbox.test.dpToPixel
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlexWrapTest : FlexboxTestBase() {
    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexWrap_wrap() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_wrap_test)

        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.WRAP))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        // The width of the FlexboxLayout is not enough for placing the three text views.
        // The third text view should be placed below the first one
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        val flexLines = flexboxLayout.flexLines
        Assert.assertThat(flexLines.size, Is.`is`(2))
        val flexLine1 = flexLines[0]
        val activity = activityRule.activity
        Assert.assertThat(flexLine1.mainSize, Is.`is`(activity.dpToPixel(320)))
        Assert.assertThat(flexLine1.crossSize, Is.`is`(activity.dpToPixel(120)))
        val flexLine2 = flexLines[1]
        Assert.assertThat(flexLine2.mainSize, Is.`is`(activity.dpToPixel(160)))
        Assert.assertThat(flexLine2.crossSize, Is.`is`(activity.dpToPixel(120)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexWrap_nowrap() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_wrap_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexWrap = FlexWrap.NOWRAP
                        flexboxLayout.alignItems = AlignItems.FLEX_START
                    }
                })

        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.NOWRAP))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        // The width of the FlexboxLayout is not enough for placing the three text views.
        // But the flexWrap attribute is set to NOWRAP, the third text view is placed
        // to the right of the second one and overflowing the parent FlexboxLayout.
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Assert.assertThat(flexboxLayout.flexLines.size, Is.`is`(1))
        val flexLines = flexboxLayout.flexLines
        Assert.assertThat(flexLines.size, Is.`is`(1))
        val flexLine = flexLines[0]
        val activity = activityRule.activity
        Assert.assertThat(flexLine.mainSize, Is.`is`(activity.dpToPixel(480)))
        Assert.assertThat(flexLine.crossSize, Is.`is`(activity.dpToPixel(300)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexWrap_wrap_reverse() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_wrap_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                    }
                })

        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.WRAP_REVERSE))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        // The width of the FlexboxLayout is not enough for placing the three text views.
        // There should be two flex lines same as WRAP, but the layout starts from bottom
        // to top in FlexWrap.WRAP_REVERSE
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Assert.assertThat(flexboxLayout.flexLines.size, Is.`is`(2))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexWrap_wrap_flexDirection_column() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_wrap_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.WRAP))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        // The height of the FlexboxLayout is not enough for placing the three text views.
        // The third text view should be placed right of the first one
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Assert.assertThat(flexboxLayout.flexLines.size, Is.`is`(2))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexWrap_nowrap_flexDirection_column() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_wrap_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                        flexboxLayout.flexWrap = FlexWrap.NOWRAP
                    }
                })

        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.NOWRAP))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        // The height of the FlexboxLayout is not enough for placing the three text views.
        // But the flexWrap attribute is set to NOWRAP, the third text view is placed
        // below the second one and overflowing the parent FlexboxLayout.
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Assert.assertThat(flexboxLayout.flexLines.size, Is.`is`(1))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexWrap_wrap_reverse_flexDirection_column() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_wrap_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                    }
                })

        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.WRAP_REVERSE))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isRightAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        // The width of the FlexboxLayout is not enough for placing the three text views.
        // There should be two flex lines same as WRAP, but the layout starts from right
        // to left in FlexWrap.WRAP_REVERSE
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Assert.assertThat(flexboxLayout.flexLines.size, Is.`is`(2))
    }
}