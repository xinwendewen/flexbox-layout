package com.google.android.flexbox.test.items

import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.FlakyTest
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.test.R
import com.google.android.flexbox.test.container.FlexboxTestBase
import org.junit.Assert
import org.junit.Test

class FlexShrinkTest : FlexboxTestBase() {
    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testMinWidth_works_as_lower_bound_shrink_to() {
        val activity = activityRule.activity

        // This test case verifies if the minWidth attribute works as a lower bound
        // when the view would shrink less than the minWidth if the minWidth weren't set
        val flexboxLayout = createFlexboxLayout(R.layout.activity_minwidth_lower_bound_test)
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val textView4 = activity.findViewById<TextView>(R.id.text4)
        val minWidth = (textView1.layoutParams as FlexboxLayout.LayoutParams).minWidth

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(hasWidth(minWidth))
        Assert.assertEquals(flexboxLayout.width, textView1.width + textView2.width + textView3.width +
                textView4.width)
    }

    @Test
    @FlakyTest
    @Throws(Throwable::class)
    fun testMinHeight_works_as_lower_bound_shrink_to() {
        val activity = activityRule.activity

        // This test case verifies if the minHeight attribute works as a lower bound
        // when the view would shrink less than the minHeight if the minHeight weren't set
        val flexboxLayout = createFlexboxLayout(R.layout.activity_minheight_lower_bound_test)
        val textView1 = activity.findViewById<TextView>(R.id.text1)
        val textView2 = activity.findViewById<TextView>(R.id.text2)
        val textView3 = activity.findViewById<TextView>(R.id.text3)
        val textView4 = activity.findViewById<TextView>(R.id.text4)
        val minHeight = (textView1.layoutParams as FlexboxLayout.LayoutParams).minHeight

        Espresso.onView(ViewMatchers.withId(R.id.text1)).check(hasHeight(minHeight))
        Assert.assertEquals(flexboxLayout.height, textView1.height + textView2.height + textView3.height +
                textView4.height)
    }
}