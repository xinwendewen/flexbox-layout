package com.google.android.flexbox.test.items

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
import com.google.android.flexbox.test.container.FlexboxTestBase
import org.hamcrest.core.IsNot
import org.junit.Assert
import org.junit.Test

class AlignSelfTest : FlexboxTestBase() {
    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignSelf_stretch() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(
                R.layout.activity_align_self_stretch_test)

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text2)))

        // There should be 2 flex lines in the layout with the given layout.
        // Only the first TextView's alignSelf is set to ALIGN_SELF_STRETCH
        val flexLineSize = flexboxLayout.height / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView1.height, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
        Assert.assertThat(textView2.height, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.height, IsNot.not(flexLineSize))
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testAlignSelf_stretch_flexDirection_column() {
        val activity = activityRule.activity
        val flexboxLayout = createFlexboxLayout(R.layout.activity_align_self_stretch_test,
                object : LayoutConfiguration {
                    override fun apply(flexboxLayout: FlexboxLayout) {
                        flexboxLayout.flexDirection = FlexDirection.COLUMN
                    }
                })

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isLeftAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text2)).check(PositionAssertions.isCompletelyBelow(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isTopAlignedWith(ViewMatchers.withId(R.id.flexbox_layout)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text1)))
        Espresso.onView(ViewMatchers.withId(R.id.text3)).check(PositionAssertions.isCompletelyRightOf(ViewMatchers.withId(R.id.text2)))

        // There should be 2 flex lines in the layout with the given layout.
        // Only the first TextView's alignSelf is set to ALIGN_SELF_STRETCH
        val flexLineSize = flexboxLayout.width / 2
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        Assert.assertThat(textView1.width, IsEqualAllowingError.isEqualAllowingError(flexLineSize))
        Assert.assertThat(textView2.width, IsNot.not(flexLineSize))
        Assert.assertThat(textView3.width, IsNot.not(flexLineSize))
    }
}