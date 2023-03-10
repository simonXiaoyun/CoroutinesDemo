package com.example.coroutinesdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_coroutine_cancel.*
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class CoroutineCancelActivity : AppCompatActivity() {
    private val TAG = "Simon"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_cancel)

        var scope = CoroutineScope(Dispatchers.Main)
        var job1: Job? = null
        var job2: Job? = null
        var job3: Job? = null
        start_coroutine.setOnClickListener {
            scope = CoroutineScope(Dispatchers.Main)
            scope.launch {
                job1 = launch {
                    delay(2000)
                    Log.i(TAG, "coroutine one")
                }
                job2 = launch {
                    delay(3000)
                    Log.i(TAG, "coroutine two")
                }

                job3 = launch {
                    delay(4000)
                    Log.i(TAG, "coroutine three")
                }
            }
        }

        //取消Scope
        cancel_scope.setOnClickListener {
            scope.cancel()
        }

        //取消子协程
        cancel_child.setOnClickListener {
            job2?.cancel()
        }

        //取消非挂起
        cancel_unsuspended.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                val startTime = System.currentTimeMillis()
                val job = launch {
                    var nextPrintTime = startTime
                    var i = 0
                    while (i < 5){
                        if(System.currentTimeMillis() >= nextPrintTime){
                            Log.i(TAG,"job: I'm sleeping ${i++} ...")
                            nextPrintTime += 500L
                        }
                    }
                }
                delay(1300L)
                Log.i(TAG,  "父协程执行")
                job.cancel()
                Log.i(TAG,  "父协程继续执行")
            }
        }

        //取消异常被捕获的情况，取消是否生效
        try_catch_cancel.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                val job = launch {
                    repeat(5) { i ->
                        try {
                            Log.i(TAG,"打印：$i-时间：${System.currentTimeMillis()}")
                            delay(500)
                        } catch (e: Exception) {
                            Log.i(TAG,"异常：$e")
                        }
                    }
                }
                delay(1300L)
                Log.i(TAG,  "父协程执行")
                job.cancel()
                Log.i(TAG,  "父协程继续执行")
            }
        }

        resource_close.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                var inputStream: InputStream? = null
                try {
                    inputStream = assets.open("test.txt")
                    val length = inputStream.available()
                    val buffer = ByteArray(length)
                    inputStream.read(buffer)
                    val s = String(buffer, Charset.forName("UTF-8"))
                    Log.i(TAG, s)
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    inputStream?.close()
                }
            }

            assets.open("test.txt").use {
                val length = it.available()
                val buffer = ByteArray(length)
                it.read(buffer)
                val s = String(buffer, Charset.forName("UTF-8"))
                Log.i(TAG, s)
            }
        }

        //取消协程时，指定等待不应该取消任务执行完毕
        cancel_nonCancel.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch(CoroutineExceptionHandler { context, throwable ->
                Log.i(TAG,"捕获异常：$throwable")
            }) {
                val job = launch {
                    try {
                        repeat(1000) { i ->
                            Log.i(TAG, "job sleep$i")
                            delay(500)
                        }
                    } catch (e: Exception) {
                        Log.i(TAG, "异常是：${e}")
                    } finally {
                        //假设是释放资源，并且是一个耗时的操作,如果不切换这个调度，则资源释放不会完成，会造成内存泄漏
                        //执行这个调度，其他子协程的异常会等待这段代码执行完成才处理异常
                        withContext(NonCancellable) {
                            Log.i(TAG, "开始释放资源……")
                            delay(2000)
                            Log.i(TAG, "释放资源完成")
                        }
                    }
                }

                launch {
                    Log.i(TAG, "第二个协程开始，并抛出异常")
                    delay(500)
                    throw NullPointerException()
                }
                delay(2000)
                job.cancel()
                Log.i(TAG, "取消任务，并且继续执行")
            }
        }
        //超时取消
        time_out.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
//                withTimeoutOrNull() //返回null，不会抛出异常
                withTimeout(3000) {
                    repeat(1000) {
                        Log.i(TAG, "job sleep$it")
                        delay(500)
                    }
                }
            }
        }
    }


}