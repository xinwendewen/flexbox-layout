package com.google.android.flexbox.test.items

import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.PositionAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.FlakyTest
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.test.IsEqualAllowingError
import com.google.android.flexbox.test.LayoutConfiguration
import com.google.android.flexbox.test.R
import com.google.android.flexbox.test.container.FlexboxTestBase
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Test

class FlexBasisTest : FlexboxTestBase() {
    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexBasisPercent_wrap() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_flex_basis_percent_test)

        // The text1 length is 50%, the text2 length is 60% and the wrap property is WRAP,
        // the text2 should be on the second flex line.
        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.WRAP))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val lp1 = textView1.layoutParams as FlexboxLayout.LayoutParams
        val lp2 = textView2.layoutParams as FlexboxLayout.LayoutParams
        Assert.assertThat(textView1.width, Is.`is`(Math.round(flexboxLayout.width * lp1.flexBasisPercent)))
        Assert.assertThat(textView2.width, Is.`is`(Math.round(flexboxLayout.width * lp2.flexBasisPercent)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexBasisPercent_nowrap() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_basis_percent_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexWrap = FlexWrap.NOWRAP
                    }
                })

        // The text1 length is 50%, the text2 length is 60% and the text3 has the fixed width,
        // but the flex wrap attribute is NOWRAP, and flexShrink attributes for all
        // children are the default value (1), three text views are shrank to fit in a single flex
        // line.
        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.NOWRAP))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val totalWidth = textView1.width + textView2.width + textView3.width
        // Allowing minor different length with the flex container since the sum of the three text
        // views width is not always the same as the flex container's main size caused by round
        // errors in calculating the percent lengths.
        Assert.assertThat(flexboxLayout.width, IsEqualAllowingError.isEqualAllowingError(totalWidth))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexBasisPercent_wrap_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_basis_percent_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        // The text1 length is 50%, the text2 length is 60% and the wrap property is WRAP,
        // the text2 should be on the second flex line.
        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.WRAP))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val lp1 = textView1.layoutParams as FlexboxLayout.LayoutParams
        val lp2 = textView2.layoutParams as FlexboxLayout.LayoutParams
        Assert.assertThat(textView1.height, Is.`is`(Math.round(flexboxLayout.height * lp1.flexBasisPercent)))
        Assert.assertThat(textView2.height, Is.`is`(Math.round(flexboxLayout.height * lp2.flexBasisPercent)))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testFlexBasisPercent_nowrap_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_flex_basis_percent_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexWrap = FlexWrap.NOWRAP
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        // The text1 length is 50%, the text2 length is 60% and the text3 has the fixed height,
        // but the flex wrap attribute is NOWRAP, and flexShrink attributes for all
        // children are the default value (1), three text views are shrank to fit in a single
        // flex line.
        Assert.assertThat(flexboxLayout.flexWrap, Is.`is`(FlexWrap.NOWRAP))
        Assert.assertThat(flexboxLayout.flexDirection, Is.`is`(FlexDirection.COLUMN))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))

        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val totalHeight = textView1.height + textView2.height + textView3.height
        // Allowing minor different length with the flex container since the sum of the three text
        // views width is not always the same as the flex container's main size caused by round
        // errors in calculating the percent lengths.
        Assert.assertThat(flexboxLayout.height, IsEqualAllowingError.isEqualAllowingError(totalHeight))
    }
}