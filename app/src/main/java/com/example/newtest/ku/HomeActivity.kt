package com.example.newtest.ku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.newtest.R

/**
 * @Author XTP
 * @CreateTime 2023/3/10
 * @Description: 酷安首页
 */
class HomeActivity : AppCompatActivity() {
    private lateinit var mViewPager2: ViewPager2
    private lateinit var mAdapter: FragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mViewPager2 = findViewById(R.id.pager)
        mAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 10
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun containsItem(itemId: Long): Boolean {
                return false
            }

            override fun createFragment(position: Int): Fragment {
                return HomeFragment()
            }
        }
        mViewPager2.adapter = mAdapter
    }
}