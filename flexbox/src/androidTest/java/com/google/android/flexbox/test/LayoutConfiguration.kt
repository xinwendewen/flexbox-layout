package com.google.android.flexbox.test

import com.google.android.flexbox.FlexboxLayout

interface LayoutConfiguration {

    fun apply(flexboxLayout: FlexboxLayout)

    companion object {
        val EMPTY: LayoutConfiguration = object : LayoutConfiguration {
            override fun apply(flexboxLayout: FlexboxLayout) = Unit
        }
    }
}