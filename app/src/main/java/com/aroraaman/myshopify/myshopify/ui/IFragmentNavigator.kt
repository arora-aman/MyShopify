package com.aroraaman.myshopify.myshopify.ui

import android.support.v4.app.Fragment

interface IFragmentNavigator {
    /**
     * Navigate to another fragment without a transition.
     *
     * @param fragment Fragment to navigate to
     */
    fun navigateToFragment(fragment: Fragment)

    /**
     * Navigate to another fragment with a slide transition.
     *
     * @param fragment Fragment to navigate to
     */
    fun slideToFragment(fragment: Fragment)
}