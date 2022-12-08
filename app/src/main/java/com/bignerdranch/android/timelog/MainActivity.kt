package com.bignerdranch.android.timelog


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.bignerdranch.android.timelog.adapter.FragmentAdapter
import com.bignerdranch.android.timelog.fragment.NoteFragment
import com.bignerdranch.android.timelog.fragment.CrimeListFragment
import com.bignerdranch.android.timelog.fragment.TodoFragment
import com.bignerdranch.android.timelog.fragment.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(),
    CrimeListFragment.Callbacks, NoteFragment.Callbacks{

    private lateinit var bnView: BottomNavigationView
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        viewPager = findViewById(R.id.view_pager)
        bnView = findViewById(R.id.bottom_nav_view)

        val fragments: MutableList<Fragment> = ArrayList<Fragment>()
        fragments.add(CrimeListFragment())
        fragments.add(TodoFragment())
        fragments.add(SettingFragment())

        val adapter = FragmentAdapter(fragments, supportFragmentManager)
        viewPager.adapter = adapter

        val currentFragment = supportFragmentManager.findFragmentById(R.id.view_pager)
        if(currentFragment == null) {
            val fragment = CrimeListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.view_pager,fragment)
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()

        // 导航栏的控制
        bnView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.tab_one -> viewPager.currentItem = 0
                R.id.tab_two -> viewPager.currentItem = 1
                R.id.tab_three -> viewPager.currentItem = 2
            }
            false
        })

        // ViewPager 滑动事件监听,BottomNavigationView跟随滑动切换
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}

            // 将滑动到的页面对应的 menu 设置为选中状态
            override fun onPageSelected(i: Int) {
                bnView.menu.getItem(i).isChecked = true
            }
            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    override fun onCrimeSelected(crimeId: UUID) {
        val fragment = NoteFragment.newInstance(crimeId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.view_pager, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCrimeDeleted(noteFragment: NoteFragment) {
        supportFragmentManager.beginTransaction()
            .remove(noteFragment)
            .commit()
    }

}