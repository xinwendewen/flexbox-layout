package com.google.android.flexbox.test.container

import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.PositionAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.FlakyTest
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent
import com.google.android.flexbox.test.IsEqualAllowingError
import com.google.android.flexbox.test.LayoutConfiguration
import com.google.android.flexbox.test.R
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Test

class JustifyContentTest : FlexboxTestBase() {
    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_flexStart() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test)

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.FLEX_START))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_flexStart_withParentPadding() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_justify_content_with_parent_padding)

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.FLEX_START))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))
        val text1 = activity.findViewById<TextView>(R.id.text1)
        // Both the parent FrameLayout and the FlexboxLayout have different padding values
        // but the text1.getLeft should be the padding value for the FlexboxLayout, not including
        // the parent's padding value
        Assert.assertThat(text1.left, Is.`is`(flexboxLayout.paddingLeft))
        Assert.assertThat(text1.top, Is.`is`(flexboxLayout.paddingTop))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_flexEnd() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.FLEX_END
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.FLEX_END))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isRightAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text3)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text2)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_flexEnd_withParentPadding() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_justify_content_with_parent_padding,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.FLEX_END
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.FLEX_END))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text3)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text2)))
        val text3 = activity.findViewById<TextView>(R.id.text3)
        // Both the parent FrameLayout and the FlexboxLayout have different padding values
        // but the text3.getRight should be the padding value for the FlexboxLayout, not including
        // the parent's padding value
        Assert.assertThat(flexboxLayout.width - text3.right, Is.`is`(flexboxLayout.paddingRight))
        Assert.assertThat(text3.top, Is.`is`(flexboxLayout.paddingTop))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_center() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.CENTER
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.CENTER))

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val space = (flexboxLayout.width - textView1.width - textView2.width - textView3.width) / 2
        Assert.assertThat(textView1.left, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(flexboxLayout.right - textView3.right, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_center_withParentPadding() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_justify_content_with_parent_padding,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.CENTER
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.CENTER))

        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.width - textView1.width - textView2.width -
                textView3.width - flexboxLayout.paddingLeft - flexboxLayout.paddingRight
        space /= 2
        Assert.assertThat(textView1.left - flexboxLayout.paddingLeft, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(flexboxLayout.width - textView3.right - flexboxLayout.paddingRight,
                IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceBetween() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_BETWEEN
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_BETWEEN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isRightAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.width - textView1.width - textView2.width - textView3.width
        space /= 2
        Assert.assertThat(textView2.left - textView1.right, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView3.left - textView2.right, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceBetween_withPadding() {
        val activity = activityRule.activity
        val padding = 40
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_BETWEEN
                        flexboxLayout.setPadding(padding, padding, padding, padding)
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_BETWEEN))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.width - textView1.width - textView2.width - textView3.width -
                padding * 2
        space /= 2
        Assert.assertThat(textView1.left, Is.`is`(padding))
        Assert.assertThat(flexboxLayout.right - textView3.right, Is.`is`(padding))
        Assert.assertThat(textView2.left - textView1.right, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView3.left - textView2.right, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceAround() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_AROUND
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_AROUND))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.width - textView1.width - textView2.width - textView3.width
        space /= 6 // Divide by the number of children * 2
        Assert.assertTrue(space - 1 <= textView1.left && textView1.left <= space + 1)
        val spaceInMiddle = space * 2
        Assert.assertThat(textView2.left - textView1.right, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(textView3.left - textView2.right, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(flexboxLayout.right - textView3.right, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceEvenly() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_EVENLY
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_EVENLY))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.width - textView1.width - textView2.width - textView3.width
        space /= 4 // Divide by the number of children + 1
        Assert.assertThat(textView1.left, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView2.left - textView1.right, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView3.left - textView2.right, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(flexboxLayout.right - textView3.right, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceAround_withPadding() {
        val activity = activityRule.activity
        val padding = 40
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_AROUND
                        flexboxLayout.setPadding(padding, padding, padding, padding)
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_AROUND))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.width - textView1.width - textView2.width - textView3.width -
                padding * 2
        space /= 6 // Divide by the number of children * 2
        Assert.assertThat(textView1.left - padding, IsEqualAllowingError.isEqualAllowingError(space))

        val spaceInMiddle = space * 2
        Assert.assertThat(textView2.left - textView1.right, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(textView3.left - textView2.right, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(flexboxLayout.right - textView3.right - padding, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceEvenly_withPadding() {
        val activity = activityRule.activity
        val padding = 40
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_EVENLY
                        flexboxLayout.setPadding(padding, padding, padding, padding)
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_EVENLY))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.width - textView1.width - textView2.width - textView3.width -
                padding * 2
        space /= 4 // Divide by the number of children + 1
        Assert.assertThat(textView1.left - padding, IsEqualAllowingError.isEqualAllowingError(space))

        Assert.assertThat(textView2.left - textView1.right, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView3.left - textView2.right, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(flexboxLayout.right - textView3.right - padding, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_flexStart_flexDirection_column() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.FLEX_START))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_flexEnd_flexDirection_column() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.FLEX_END
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                        flexboxLayout.alignItems = AlignItems.STRETCH
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.FLEX_END))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isRightAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text3)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text2)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_center_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.CENTER
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.CENTER))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.height - textView1.height - textView2.height - textView3.height
        space /= 2
        Assert.assertThat(textView1.top, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(flexboxLayout.bottom - textView3.bottom, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceBetween_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_BETWEEN
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_BETWEEN))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.height - textView1.height - textView2.height - textView3.height
        space /= 2
        Assert.assertThat(textView2.top - textView1.bottom, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView3.top - textView2.bottom, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceBetween_flexDirection_column_withPadding() {
        val activity = activityRule.activity
        val padding = 40
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_BETWEEN
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                        flexboxLayout.setPadding(padding, padding, padding, padding)
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_BETWEEN))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.height - textView1.height - textView2.height - textView3.height - padding * 2
        space /= 2
        Assert.assertThat(textView1.top, Is.`is`(padding))
        Assert.assertThat(flexboxLayout.bottom - textView3.bottom, Is.`is`(padding))
        Assert.assertThat(textView2.top - textView1.bottom, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView3.top - textView2.bottom, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceAround_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_AROUND
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_AROUND))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.height - textView1.height - textView2.height - textView3.height
        space /= 6 // Divide by the number of children * 2
        Assert.assertThat(textView1.top, IsEqualAllowingError.isEqualAllowingError(space))
        val spaceInMiddle = space * 2
        Assert.assertThat(textView2.top - textView1.bottom, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(textView3.top - textView2.bottom, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(flexboxLayout.bottom - textView3.bottom, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceEvenly_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_EVENLY
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_EVENLY))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.height - textView1.height - textView2.height - textView3.height
        space /= 4 // Divide by the number of children + 1
        Assert.assertThat(textView1.top, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView2.top - textView1.bottom, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView3.top - textView2.bottom, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(flexboxLayout.bottom - textView3.bottom, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceAround_flexDirection_column_withPadding() {
        val activity = activityRule.activity
        val padding = 40
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_AROUND
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                        flexboxLayout.setPadding(padding, padding, padding, padding)
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_AROUND))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.height - textView1.height - textView2.height - textView3.height -
                padding * 2
        space /= 6 // Divide by the number of children * 2
        Assert.assertThat(textView1.top - padding, IsEqualAllowingError.isEqualAllowingError(space))
        val spaceInMiddle = space * 2
        Assert.assertThat(textView2.top - textView1.bottom, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(textView3.top - textView2.bottom, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(flexboxLayout.bottom - textView3.bottom - padding, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceEvenly_flexDirection_column_withPadding() {
        val activity = activityRule.activity
        val padding = 40
        val flexboxLayout = createFlexboxLayout(R.layout.activity_justify_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_EVENLY
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                        flexboxLayout.setPadding(padding, padding, padding, padding)
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_EVENLY))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.height - textView1.height - textView2.height - textView3.height -
                padding * 2
        space /= 4 // Divide by the number of children + 1
        Assert.assertThat(textView1.top - padding, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView2.top - textView1.bottom, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(textView3.top - textView2.bottom, IsEqualAllowingError.isEqualAllowingError(space))
        Assert.assertThat(flexboxLayout.bottom - textView3.bottom - padding,
                IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceAround_including_gone_views() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_justify_content_with_gone,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_AROUND
                    }
                }
        )

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_AROUND))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.width - textView1.width - textView3.width
        space /= 4 // Divide by the number of visible children * 2
        Assert.assertThat(textView1.left, IsEqualAllowingError.isEqualAllowingError(space))
        val spaceInMiddle = space * 2
        Assert.assertThat(textView3.left - textView1.right, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(flexboxLayout.right - textView3.right, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceBetween_including_gone_views() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_justify_content_with_gone,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.justifyContent = JustifyContent.SPACE_BETWEEN
                    }
                })

        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_BETWEEN))

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isRightAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val space = flexboxLayout.width - textView1.width - textView3.width
        Assert.assertThat(textView3.left - textView1.right, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceAround_including_gone_views_direction_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_justify_content_with_gone,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                        flexboxLayout.justifyContent = JustifyContent.SPACE_AROUND
                    }
                })

        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_AROUND))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var space = flexboxLayout.height - textView1.height - textView3.height
        space /= 4 // Divide by the number of visible children * 2
        Assert.assertThat(textView1.top, IsEqualAllowingError.isEqualAllowingError(space))
        val spaceInMiddle = space * 2
        Assert.assertThat(textView3.top - textView1.bottom, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(flexboxLayout.bottom - textView3.bottom, IsEqualAllowingError.isEqualAllowingError(space))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testJustifyContent_spaceBetween_including_gone_views_direction_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_justify_content_with_gone,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                        flexboxLayout.justifyContent = JustifyContent.SPACE_BETWEEN
                    }
                })

        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Assert.assertThat(flexboxLayout.justifyContent, Is.`is`(JustifyContent.SPACE_BETWEEN))

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val space = flexboxLayout.height - textView1.height - textView3.height
        Assert.assertThat(textView3.top - textView1.bottom, IsEqualAllowingError.isEqualAllowingError(space))
    }
}