package com.example.coroutinesdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

        cancel_scope.setOnClickListener {
            scope.cancel()
        }

        cancel_child.setOnClickListener {
            job2?.cancel()
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

        //取消一些不可取消任务
        cancel_nonCancel.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val job = launch {
                    try {
                        repeat(1000) { i ->
                            Log.i(TAG, "job sleep$i")
                            delay(500)
                        }
                    } finally {
                        //假设是释放资源，并且是一个耗时的操作,如果不切换这个调度，则资源释放不会完成，会造成内存泄漏
                        withContext(NonCancellable) {
                            Log.i(TAG, "开始释放资源……")
                            delay(2000)
                            Log.i(TAG, "释放资源完成")
                        }
                    }
                }
                delay(2000)
                job.cancelAndJoin()
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