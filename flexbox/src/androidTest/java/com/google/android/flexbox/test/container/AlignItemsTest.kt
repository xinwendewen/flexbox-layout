package com.google.android.flexbox.test.container

import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.PositionAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.FlakyTest
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.test.IsEqualAllowingError
import com.google.android.flexbox.test.LayoutConfiguration
import com.google.android.flexbox.test.R
import org.hamcrest.core.Is
import org.hamcrest.core.IsNot
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class AlignItemsTest : FlexboxTestBase() {
    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_flexStart() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_items_test)

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.FLEX_START))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.height / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView1.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView2.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.top, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_flexEnd() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_items_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignItems = AlignItems.FLEX_END
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.FLEX_END))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.height / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView1.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView2.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView1.bottom, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
        Assert.assertThat(textView2.bottom, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
        Assert.assertThat(textView3.bottom, Is.`is`(flexboxLayout.bottom))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_flexEnd_parentPadding() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_align_items_parent_padding_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignItems = AlignItems.FLEX_END
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.FLEX_END))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        Assert.assertThat(textView1.bottom, Is.`is`(flexboxLayout.bottom - flexboxLayout.paddingBottom))
        Assert.assertThat(textView2.bottom, Is.`is`(flexboxLayout.bottom - flexboxLayout.paddingBottom))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_flexEnd_parentPadding_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_align_items_parent_padding_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignItems = AlignItems.FLEX_END
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.FLEX_END))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        Assert.assertThat(textView1.right, Is.`is`(flexboxLayout.right - flexboxLayout.paddingRight))
        Assert.assertThat(textView2.right, Is.`is`(flexboxLayout.right - flexboxLayout.paddingRight))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_center() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_items_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignItems = AlignItems.CENTER
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.CENTER))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.height / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        // All TextView's heights are the same. No issues should be found if using the first
        // TextView to calculate the space above and below
        val spaceAboveAndBelow = (flexLineSize - textView1.height) / 2
        Assert.assertThat(textView1.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView2.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView1.top, IsEqualAllowingError.isEqualAllowingError(spaceAboveAndBelow))
        Assert.assertThat(textView2.top, IsEqualAllowingError.isEqualAllowingError(spaceAboveAndBelow))
        Assert.assertThat(textView3.top, IsEqualAllowingError.isEqualAllowingError(flexLineSize + spaceAboveAndBelow))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_flexEnd_wrapReverse() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_items_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                        flexboxLayout.alignItems = AlignItems.FLEX_END
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.FLEX_END))
        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.WRAP_REVERSE))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.height / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)

        Assert.assertThat(textView1.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView2.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView1.top, IsEqualAllowingError.isEqualAllowingError(flexboxLayout.height - flexLineSize))
        Assert.assertThat(textView2.top, IsEqualAllowingError.isEqualAllowingError(flexboxLayout.height - flexLineSize))
        Assert.assertThat(textView3.top, Is.`is`(0))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_center_wrapReverse() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_items_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                        flexboxLayout.alignItems = AlignItems.CENTER
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.CENTER))
        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.WRAP_REVERSE))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.height / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)

        // All TextView's heights are the same. No issues should be found if using the first
        // TextView to calculate the space above and below
        val spaceAboveAndBelow = (flexLineSize - textView1.height) / 2
        Assert.assertThat(textView1.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView2.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView1.bottom, IsEqualAllowingError.isEqualAllowingError(flexboxLayout.height - spaceAboveAndBelow))
        Assert.assertThat(textView2.bottom, IsEqualAllowingError.isEqualAllowingError(flexboxLayout.height - spaceAboveAndBelow))
        Assert.assertThat(textView3.bottom,
                IsEqualAllowingError.isEqualAllowingError(flexboxLayout.height - flexLineSize - spaceAboveAndBelow))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_flexStart_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_items_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.FLEX_START))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.width / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView1.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView2.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.left, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_flexEnd_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_items_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignItems = AlignItems.FLEX_END
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.FLEX_END))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isRightAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.height / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView1.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView2.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView1.right, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
        Assert.assertThat(textView2.right, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
        Assert.assertThat(textView3.right, Is.`is`(flexboxLayout.right))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_center_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_items_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignItems = AlignItems.CENTER
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.CENTER))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.width / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        // All TextView's widths are the same. No issues should be found if using the first
        // TextView to calculate the space left and right
        val spaceLeftAndRight = (flexLineSize - textView1.width) / 2
        Assert.assertThat(textView1.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView2.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView1.left, IsEqualAllowingError.isEqualAllowingError(spaceLeftAndRight))
        Assert.assertThat(textView2.left, IsEqualAllowingError.isEqualAllowingError(spaceLeftAndRight))
        Assert.assertThat(textView3.left, IsEqualAllowingError.isEqualAllowingError(flexLineSize + spaceLeftAndRight))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_flexEnd_wrapReverse_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_items_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                        flexboxLayout.alignItems = AlignItems.FLEX_END
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.FLEX_END))
        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.WRAP_REVERSE))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.width / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)

        Assert.assertThat(textView1.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView2.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView1.left, IsEqualAllowingError.isEqualAllowingError(flexboxLayout.width - flexLineSize))
        Assert.assertThat(textView2.left, IsEqualAllowingError.isEqualAllowingError(flexboxLayout.width - flexLineSize))
        Assert.assertThat(textView3.left, Is.`is`(0))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_center_wrapReverse_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_items_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                        flexboxLayout.alignItems = AlignItems.CENTER
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.CENTER))
        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.WRAP_REVERSE))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.width / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)

        // All TextView's widths are the same. No issues should be found if using the first
        // TextView to calculate the space above and below
        val spaceLeftAndRight = (flexLineSize - textView1.width) / 2
        Assert.assertThat(textView1.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView2.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView1.right, IsEqualAllowingError.isEqualAllowingError(flexboxLayout.width - spaceLeftAndRight))
        Assert.assertThat(textView2.right, IsEqualAllowingError.isEqualAllowingError(flexboxLayout.width - spaceLeftAndRight))
        Assert.assertThat(textView3.right,
                IsEqualAllowingError.isEqualAllowingError(flexboxLayout.width - flexLineSize - spaceLeftAndRight))
    }

    @Test
    @FlakyTest
    @Ignore
    @Throws(Throwable::class)
    fun testAlignItems_baseline() {
        val activity = activityRule.activity
        createFlexboxLayout(R.layout.activity_align_items_baseline_test)
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val topPluBaseline1 = textView1.top + textView1.baseline
        val topPluBaseline2 = textView2.top + textView2.baseline
        val topPluBaseline3 = textView3.top + textView3.baseline

        Assert.assertThat(topPluBaseline1, Is.`is`(topPluBaseline2))
        Assert.assertThat(topPluBaseline2, Is.`is`(topPluBaseline3))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    @Ignore
    fun testAlignItems_baseline_wrapContent() {
        // This test verifies the issue that baseline calculation is broken on API level +24
        // https://github.com/google/flexbox-layout/issues/341
        val activity = activityRule.activity
        val layout = createFlexboxLayout(R.layout.activity_align_items_baseline_wrap_content)
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val topPluBaseline1 = textView1.top + textView1.baseline
        val topPluBaseline2 = textView2.top + textView2.baseline
        val topPluBaseline3 = textView3.top + textView3.baseline

        Assert.assertThat(topPluBaseline1, Is.`is`(topPluBaseline2))
        Assert.assertThat(topPluBaseline2, Is.`is`(topPluBaseline3))
        Assert.assertThat(layout.flexLines.size, Is.`is`(1))
        Assert.assertTrue(layout.flexLines[0].crossSize > textView1.height)
    }

    @Test
    @FlakyTest
    @Ignore
    @Throws(Throwable::class)
    fun testAlignItems_baseline_wrapReverse() {
        val activity = activityRule.activity
        createFlexboxLayout(R.layout.activity_align_items_baseline_test, object : LayoutConfiguration {
            override fun apply(flexboxLayout: FlexboxLayout) {
                flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
            }
        })
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val bottomPluBaseline1 = textView1.bottom + textView1.baseline
        val bottomPluBaseline2 = textView2.bottom + textView2.baseline
        val bottomPluBaseline3 = textView3.bottom + textView3.baseline

        Assert.assertThat(bottomPluBaseline1, Is.`is`(bottomPluBaseline2))
        Assert.assertThat(bottomPluBaseline2, Is.`is`(bottomPluBaseline3))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignItems_stretch() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_stretch_test)

        Assert.assertThat(flexboxLayout.alignItems, Is.`is`(AlignItems.STRETCH))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))

        // There should be 2 flex lines in the layout with the given layout.
        val flexLineSize = flexboxLayout.height / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView1.height, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
        Assert.assertThat(textView2.height, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
        Assert.assertThat(textView3.height, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
    }

}