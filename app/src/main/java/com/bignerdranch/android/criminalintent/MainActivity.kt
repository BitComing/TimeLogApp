package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.bnuz.example.criminalintentRoom.CrimeFragment
import java.util.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(),
    CrimeListFragment.Callbacks, CrimeFragment.Callbacks{

//    private lateinit var mTabLayout: TabLayout
    private lateinit var mFragments: MutableList<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null) {
            val fragment = CrimeListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container,fragment)
                .commit()
        }
    }

//    override fun onCreateView(
//        parent: View?,
//        name: String,
//        context: Context,
//        attrs: AttributeSet
//    ): View? {
//        return super.onCreateView(parent, name, context, attrs)
//
//        mTabLayout = findViewById(R.id.bottom_tab_layout)
//        mTabLayout.addTab(mTabLayout.newTab().setText("ff"))
//        mTabLayout.addTab(mTabLayout.newTab().setText("cc"))
//        return parent
//    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCrimeSelected(crimeId: UUID) {
        val fragment = CrimeFragment.newInstance(crimeId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCrimeDeleted(crimeFragment: CrimeFragment) {
//        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .remove(crimeFragment)
            .commit()
    }

}