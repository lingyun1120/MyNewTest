package com.example.newtest.test

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newtest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @Author XTP
 * @CreateTime 2023/3/10
 * @Description:
 */
class CoroutineActivity : AppCompatActivity() {
    private lateinit var tvLog: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corontines)
        tvLog = findViewById(R.id.tvLog)
        begin()
    }

    private fun begin() {
        var log = "开始 @Thread " + Thread.currentThread().name

        val scope = CoroutineScope(Dispatchers.Main + Job())
        tvLog.text = log
        scope.launch {
            log = log + "\n" + "模拟请求 @Thread " + Thread.currentThread().name
            tvLog.text = log
            withContext(Dispatchers.IO) {
                //模拟网络请求
                log = log + "\n" + "等待3秒钟 @Thread " + Thread.currentThread().name
                withContext(Dispatchers.Main) {
                    tvLog.text = log
                }
                Thread.sleep(3000)
                log = log + "\n" + "请求结束 @Thread " + Thread.currentThread().name
            }
            log = log + "\n" + "刷新UI @Thread " + Thread.currentThread().name
            tvLog.text = log
        }
    }

}