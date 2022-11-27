package com.bignerdranch.android.criminalintent.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentAdapter(fragments: List<Fragment>, fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {

    private val mFragments = fragments

    override fun getItem(i: Int): Fragment {
        return mFragments[i]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

}