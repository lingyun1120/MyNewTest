package com.example.newtest.ku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newtest.R
import com.example.newtest.test.TestAdapter

/**
 * @Author XTP
 * @CreateTime 2023/3/27
 * @Description:
 */
class HomeFragment : Fragment() {
    private lateinit var rvList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvList = view.findViewById(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(requireContext())
        val list = arrayListOf<String>()
        for (i in 0 .. 50) {
            list.add("Item " + i)
        }
        rvList.adapter = TestAdapter(list)
    }
}