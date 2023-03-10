package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_coroutine_builder.*
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.math.log10

class CoroutineBuilderActivity : AppCompatActivity() {
    private val TAG = "Simon"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_builder)

        launch_builder.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                var data1 = "init data1"
                var data2 = "init data2"

                val job1 = launch { data1 = getData(1) }
                val job2 = launch {
                    data2 = getData(2)
                    Log.i(TAG, "data2:$data2")
                }
                job1.join()
                Log.i(TAG, "data1:$data1")
            }
        }

        async_builder.setOnClickListener {
            CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()).launch {
                var data1 = "init data1"
                var data2 = "init data2"

                val job1 = async { getData(1) }
                val job2 = async {
                    data2 = getData(2)
                    Log.i(TAG, "data2:$data2")
                }
                Log.i(TAG, "print data1 before")
                data1 = job1.await()
                Log.i(TAG, "data1:$data1")
                Log.i(TAG, "print data1 after")
            }
        }

        life_cycle.setOnClickListener {
            var isCancel = false
            CoroutineScope(Dispatchers.Default).launch {
                val job = launch {
                    var i = 0
                    while (true) {

                        if (isCancel) {
                            this.cancel()
//                            break
                        }

                        delay(200)
                        Log.i(TAG, "print data$i")
                        i++
                        if (i == 10) {
                            isCancel = true
                        }
                    }
                }

                launch {
                    while (true) {
                        delay(1000)
                        if (job.isActive) {
                            Log.i(TAG, "job is Active")
                        }
                        if (job.isCancelled) {
                            Log.i(TAG, "job is Cancelled")
                        }
                        if (job.isCompleted) {
                            Log.i(TAG, "job is Completed")
                            break
                        }
                    }
                }

            }
        }

        concurrence_demo1.setOnClickListener {
            concurrenceDemo1()
        }

        concurrence_demo2.setOnClickListener {
            concurrenceDemo2()
        }

    }

    private suspend fun getData(index: Int): String {
        delay(2000)
        return "data is $index"
    }

    private fun concurrenceDemo1() {
        val startTime = System.currentTimeMillis()
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                delay(1000)
                Log.i(TAG, "A在运行在:${Thread.currentThread().name}")
            }

            launch {
                delay(2000)
                Log.i(TAG, "B在运行:${Thread.currentThread().name}")
            }

            launch {
                delay(1000)
                Log.i(TAG, "C在运行:${Thread.currentThread().name}")
            }
        }.invokeOnCompletion {
            val endTime = System.currentTimeMillis()
            Log.i(TAG, "协程运行时间：${endTime - startTime}")
        }
    }

    private fun concurrenceDemo2() {
        var num = 0
        var num2 = 0
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                while (true) {
                    num++
                    if (num == 10) {
//                        yield()
                    }

                    if (num == 20) {
                        break
                    }
                    if (num % 2 == 0) {
                        Log.i(TAG, "A在运行在:${Thread.currentThread().name}")
                    }
                }

            }

            launch {
                while (true) {
                    num2++
                    if (num2 == 10) {
//                        yield()
                    }

                    if (num2 == 20) {
                        break
                    }
                    if (num2 % 2 == 0) {
                        Log.i(TAG, "B在运行在:${Thread.currentThread().name}")
                    }
                }
            }

            launch {
                Log.i(TAG, "C在运行:${Thread.currentThread().name}")
            }
        }
    }
    //协程并发与线程异步并发
}