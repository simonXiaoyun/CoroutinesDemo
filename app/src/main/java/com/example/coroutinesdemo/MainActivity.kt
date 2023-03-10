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

        go_dispatchers.setOnClickListener {
            startActivity(Intent(this, CoroutineDispatchersActivity::class.java))
        }

        go_coroutineScope.setOnClickListener {
            startActivity(Intent(this, CoroutineScopeActivity::class.java))
        }

        go_coroutineBuilder.setOnClickListener {
            startActivity(Intent(this, CoroutineBuilderActivity::class.java))
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

        go_async_way.setOnClickListener {
            startActivity(Intent(this, AsyncJobHandleActivity::class.java))
        }

        project_management.setOnClickListener {
            startActivity(Intent(this, DevelopmentProjectActivity::class.java))
        }

        first_demo.setOnClickListener {
           CoroutineScope(Dispatchers.Main).launch {
                Log.i(TAG, "A-主线程的代码start：${Thread.currentThread().name}")
                user_name.text = getUserName()  //模拟网络请求
                //继续主线代码
                Log.i(TAG, "B-主线程的代码：${Thread.currentThread().name}")
                user_age.text = getAge().toString()  //模拟网络请求
                //继续主线程代码
                Log.i(TAG, "C-主线程的代码年龄之后：${Thread.currentThread().name}")
            }

            Log.i(TAG, "D-主线程协程代码块之外的代码")
        }

    }


    private suspend fun getUserName(): String {
        withContext(Dispatchers.IO){
            Log.i(TAG,"获取用户名：${Thread.currentThread().name}")
            delay(3000)
        }
        return "Simon"
    }

    private suspend fun getAge(): Int {
        withContext(Dispatchers.IO) {
            delay(2000)
        }
        return 18
    }

    private suspend fun printWorld(string: String) {
        delay(2000)
        Log.i(TAG, string)
    }
}