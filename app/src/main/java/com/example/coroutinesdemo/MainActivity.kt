package com.example.coroutinesdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val TAG = "Simon"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        GlobalScope.launch {
            Log.i(TAG, "Hello World")
            printWorld("Hello World delay")
            Log.i(TAG, "Hello World end")
        }

        go_dispatchers.setOnClickListener {
            startActivity(Intent(this, DispatchersActivity::class.java))
        }

        go_coroutineScope.setOnClickListener {
            startActivity(Intent(this, CoroutineScopeActivity::class.java))
        }

        go_coroutineContext.setOnClickListener {
            startActivity(Intent(this, CoroutineContextActivity::class.java))
        }

        go_coroutineCanCel.setOnClickListener {
            startActivity(Intent(this, CoroutineCancelActivity::class.java))
        }

        go_coroutineException.setOnClickListener {
            startActivity(Intent(this, CoroutineExceptionActivity::class.java))
        }

        go_coroutineExceptionHandler.setOnClickListener {
            startActivity(Intent(this, CoroutineExceptionHandlerActivity::class.java))
        }

        go_coroutineStart.setOnClickListener {
            startActivity(Intent(this, CoroutineStartPatternActivity::class.java))
        }

        GlobalScope.launch(Dispatchers.Main) {
            Log.i(TAG, "主线程的代码start：${Thread.currentThread().name}")
            user_name.text = async {
                //模拟网络请求
                getUserName()
            }.await()
            //继续主线代码
            Log.i(TAG, "主线程的代码：${Thread.currentThread().name}")
            //……
        }

    }


    private suspend fun getUserName(): String {
        delay(2000)
        return "Simon"
    }

    private suspend fun printWorld(string: String) {
        delay(2000)
        Log.i(TAG, string)
    }
}