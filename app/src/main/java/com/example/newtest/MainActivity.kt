package com.example.newtest

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.newtest.ku.HomeActivity
import com.example.newtest.rx.RxActivity
import com.example.newtest.test.BottomSheetActivity
import com.example.newtest.test.CoroutineActivity
import com.example.newtest.test.EqActivity
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    var executor: ThreadPoolExecutor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btCoroutine).setOnClickListener { v: View? ->
            startActivity(Intent(this, CoroutineActivity::class.java))
        }

        findViewById<View>(R.id.btBottom).setOnClickListener { v: View? ->
            startActivity(Intent(this, BottomSheetActivity::class.java))
        }

        findViewById<View>(R.id.btRx).setOnClickListener { v: View? ->
            startActivity(Intent(this, RxActivity::class.java))
        }

        findViewById<View>(R.id.btHome).setOnClickListener { v: View? ->
            startActivity(Intent(this, HomeActivity::class.java))
        }

        findViewById<View>(R.id.btEQ).setOnClickListener { v: View? ->
            startActivity(Intent(this, EqActivity::class.java))
        }

//
//        val face = FaceRec()
//        face.init(assets)
//        Log.e("----> ", face.getFaceSimilarity(getBmp("1.png"), getBmp("1.png")).toString())

        threadTest()
    }

    private fun getBmp(fileName: String): Bitmap? {
        var image: Bitmap? = null
        val am = resources.assets
        try {
            val input = am.open(fileName)
            image = BitmapFactory.decodeStream(input)
            input.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return image
    }


    private fun threadTest() {
        val TAG = "ThreadPoolExecutor test"
        val corePoolSize = 5
        val maximumPoolSize = 7
        val workQueueSize = 40
        if (executor == null) {
            executor = ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, 0,
                TimeUnit.SECONDS, LinkedBlockingQueue(workQueueSize)
            ) { r, executor ->
            }
        }
        for (i in 0..99) {
            executor?.execute(Runnable {
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                Log.e(
                    TAG, "thread name：" + Thread.currentThread().name + " index：" + i
                            + "  hashCode: " + Thread.currentThread().hashCode()
                )
            })
        }
    }
}