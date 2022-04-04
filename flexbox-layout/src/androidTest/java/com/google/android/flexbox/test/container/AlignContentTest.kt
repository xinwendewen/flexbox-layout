package com.google.android.flexbox.test.container

import android.view.ViewGroup
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.PositionAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.FlakyTest
import com.google.android.flexbox.AlignContent
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.test.IsEqualAllowingError
import com.google.android.flexbox.test.LayoutConfiguration
import com.google.android.flexbox.test.R
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Test

class AlignContentTest : FlexboxTestBase() {
    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_stretch() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test)

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.STRETCH))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        // the third TextView is wrapped to the next flex line
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))

        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val flexLineCrossSize = flexboxLayout.height / 2
        // Two flex line's cross sizes are expanded to the half of the height of the FlexboxLayout.
        // The third textView's top should be aligned width the second flex line.
        Assert.assertThat(textView3.top, Is.`is`(flexLineCrossSize))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_flexStart() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.FLEX_START
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.FLEX_START))

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        // the third TextView is wrapped to the next flex line
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView3.top, Is.`is`(textView1.height))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_flexEnd() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.FLEX_END
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.FLEX_END))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text3)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text3)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView1.bottom, Is.`is`(flexboxLayout.bottom - textView3.height))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_flexEnd_parentPadding() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.FLEX_END
                        flexboxLayout.setPadding(32, 32, 32, 32)
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.FLEX_END))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text3)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text3)))

        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView3.bottom, Is.`is`(flexboxLayout.bottom - flexboxLayout.paddingBottom))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_flexEnd_parentPadding_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.FLEX_END
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                        flexboxLayout.setPadding(32, 32, 32, 32)
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.FLEX_END))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text3)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text3)))

        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView3.right, Is.`is`(flexboxLayout.right - flexboxLayout.paddingRight))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_center() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.CENTER
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.CENTER))

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        var spaceAboveAndBottom = flexboxLayout.height - textView1.height - textView3.height
        spaceAboveAndBottom /= 2

        Assert.assertThat(textView1.top, IsEqualAllowingError.isEqualAllowingError(spaceAboveAndBottom))
        Assert.assertThat(flexboxLayout.bottom - textView3.bottom, IsEqualAllowingError.isEqualAllowingError(spaceAboveAndBottom))
        Assert.assertThat(flexboxLayout.flexLines.size, Is.`is`(2))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_spaceBetween() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.SPACE_BETWEEN
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.SPACE_BETWEEN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Assert.assertThat(flexboxLayout.flexLines.size, Is.`is`(2))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_spaceBetween_withPadding() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.SPACE_BETWEEN
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.SPACE_BETWEEN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_spaceAround() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.SPACE_AROUND
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.SPACE_AROUND))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)

        var spaceAround = flexboxLayout.height - textView1.height - textView3.height
        spaceAround /= 4 // Divide by the number of flex lines * 2

        Assert.assertThat(textView1.top, IsEqualAllowingError.isEqualAllowingError(spaceAround))
        val spaceInMiddle = textView1.bottom + spaceAround * 2
        Assert.assertThat(textView3.top, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
        Assert.assertThat(flexboxLayout.flexLines.size, Is.`is`(2))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_stretch_parentWrapContent() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        val parentLp = flexboxLayout.layoutParams
                        parentLp.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        flexboxLayout.layoutParams = parentLp
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.STRETCH))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        // the third TextView is wrapped to the next flex line
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))

        // alignContent is only effective if the parent's height/width mode is MeasureSpec.EXACTLY.
        // The size of the flex lines don't change even if the alignContent is set to
        // ALIGN_CONTENT_STRETCH
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView3.top, Is.`is`(textView1.height))
        Assert.assertThat(flexboxLayout.flexLines.size, Is.`is`(2))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_stretch_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.STRETCH))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        // the third TextView is wrapped to the next flex line
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))

        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val flexLineCrossSize = flexboxLayout.width / 2
        // Two flex line's cross sizes are expanded to the half of the width of the FlexboxLayout.
        // The third textView's left should be aligned with the second flex line.
        Assert.assertThat(textView3.left, Is.`is`(flexLineCrossSize))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_flexStart_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.FLEX_START
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.FLEX_START))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        // the third TextView is wrapped to the next flex line
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView3.left, Is.`is`(textView1.width))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_flexEnd_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.FLEX_END
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.FLEX_END))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isRightAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text3)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text3)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView1.right, Is.`is`(flexboxLayout.right - textView3.width))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_center_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.CENTER
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.CENTER))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val spaceLeftAndRight = (flexboxLayout.width - textView1.width - textView3.width) / 2

        Assert.assertThat(textView1.left, IsEqualAllowingError.isEqualAllowingError(spaceLeftAndRight))
        Assert.assertThat(textView3.right, IsEqualAllowingError.isEqualAllowingError(flexboxLayout.right - spaceLeftAndRight))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_spaceBetween_flexDirection_column() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.SPACE_BETWEEN
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.SPACE_BETWEEN))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isRightAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_spaceAround_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.SPACE_AROUND
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.SPACE_AROUND))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)

        var spaceAround = flexboxLayout.width - textView1.width - textView3.width
        spaceAround /= 4 // Divide by the number of flex lines * 2

        Assert.assertThat(textView1.left, IsEqualAllowingError.isEqualAllowingError(spaceAround))
        val spaceInMiddle = textView1.right + spaceAround * 2
        Assert.assertThat(textView3.left, IsEqualAllowingError.isEqualAllowingError(spaceInMiddle))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_stretch_parentWrapContent_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        val parentLp = flexboxLayout.layoutParams
                        parentLp.width = ViewGroup.LayoutParams.WRAP_CONTENT
                        flexboxLayout.layoutParams = parentLp
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.STRETCH))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        // the third TextView is wrapped to the next flex line
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))

        // alignContent is only effective if the parent's height/width mode is MeasureSpec.EXACTLY.
        // The size of the flex lines don't change even if the alignContent is set to
        // ALIGN_CONTENT_STRETCH
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView3.left, Is.`is`(textView1.width))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_flexEnd_wrapReverse_contentOverflowed() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test_overflowed,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.FLEX_END
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                    }
                })
        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.FLEX_END))
        Espresso.onView(ViewMatchers.withId(R.id.text6)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text5)).check(PositionAssertions.isCompletelyLeftOf(ViewMatchers.withId(R.id.text6)))
        Espresso.onView(ViewMatchers.withId(R.id.text4)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text6)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_flexStart_wrapReverse_contentOverflowed() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test_overflowed,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.FLEX_START
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                    }
                })
        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.FLEX_START))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text1)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_spaceBetween_wrapReverse_contentOverflowed() {
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test_overflowed,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.SPACE_BETWEEN
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                    }
                })
        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.SPACE_BETWEEN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isBottomAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyAbove(ViewMatchers.withId(R.id.text1)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_center_wrapReverse_contentOverflowed() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test_overflowed,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.CENTER
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                    }
                })
        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.CENTER))
        val textView6 = activity.findViewById<TextView>(R.id.text6)
        val textView4 = activity.findViewById<TextView>(R.id.text4)
        val textView2 = activity.findViewById<TextView>(R.id.text2)

        Assert.assertThat(textView6.top - flexboxLayout.top, IsEqualAllowingError.isEqualAllowingError(
                (flexboxLayout.height - textView6.height - textView4.height - textView2.height) / 2))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignContent_spaceAround_wrapReverse_contentOverflowed() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_content_test_overflowed,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.alignContent = AlignContent.SPACE_AROUND
                        flexboxLayout.flexWrap = FlexWrap.WRAP_REVERSE
                    }
                })
        Assert.assertThat(flexboxLayout.alignContent, Is.`is`(AlignContent.SPACE_AROUND))
        val textView6 = activity.findViewById<TextView>(R.id.text6)
        val textView4 = activity.findViewById<TextView>(R.id.text4)
        val textView2 = activity.findViewById<TextView>(R.id.text2)

        Assert.assertThat(textView6.top - flexboxLayout.top, IsEqualAllowingError.isEqualAllowingError(
                (flexboxLayout.height - textView6.height - textView4.height - textView2.height) / 2))
    }
}