package com.example.newtest.rx

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newtest.R

class RxActivity : AppCompatActivity() {

    private lateinit var tvLog: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx)

        tvLog = findViewById(R.id.tvLog)

        Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: Emitter<String>) {
                Log.e("---", "-----> create subscribe HELLO")
                emitter.onNext("HELLO")
            }
        }).map(object : Function<String, Int> {
            override fun apply(t: String): Int {
                Log.e("---", "-----> map apply $t")
                return 1000
            }
        }).subscribe(object : Observer<Int> {
            override fun onSubscribe() {
            }

            override fun onNext(t: Int) {
                Log.e("---", "-----> onNext $t")
            }

            override fun onError(t: Throwable) {
            }

            override fun onComplete() {
            }
        })
    }
}