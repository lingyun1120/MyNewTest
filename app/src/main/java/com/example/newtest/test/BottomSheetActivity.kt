package com.example.newtest.test

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.newtest.R
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetActivity : AppCompatActivity() {

    private lateinit var btShow: Button
    private lateinit var llBottom: LinearLayout
    private lateinit var rvList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet)

        rvList = findViewById(R.id.rvList)
        rvList.layoutManager = LinearLayoutManager(this)
        val list = arrayListOf<String>()
        for (i in 0 .. 100) {
            list.add("Item " + i)
        }
        rvList.adapter = TestAdapter(list)

        llBottom = findViewById(R.id.llBottom)
        btShow = findViewById(R.id.btShow)
        btShow.setOnClickListener {
             val bottomSheetBehavior = BottomSheetBehavior.from(llBottom)
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                //展开状态，隐藏
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                //其他的状态展开
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }
}

class TestAdapter(data: List<String>) : BaseQuickAdapter<String, QuickViewHolder>(data) {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        // 返回一个 ViewHolder
        return QuickViewHolder(R.layout.item_text, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: String?) {
        // 设置item数据
        val button = holder.getView<Button>(R.id.btItem)
        button.text = item
    }
}

