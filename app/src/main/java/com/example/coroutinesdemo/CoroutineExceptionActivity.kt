package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_coroutine_exception.*
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope
import java.lang.NullPointerException

class CoroutineExceptionActivity : AppCompatActivity() {
    private val TAG = "Simon"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_exception)

        automatic_transmission.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    Log.i(TAG, "start")
                    var list = emptyList<String>()
                    list[0]
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        receive_transmission.setOnClickListener {
            val deferred = CoroutineScope(Dispatchers.Main).async {
                Log.i(TAG, "start")
                var list = emptyList<String>()
                list[0]
            }

            runBlocking {
                try {
//                    deferred.await()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

        child_coroutine_exception.setOnClickListener {
            runBlocking {
                val coroutineScope =
                    CoroutineScope(CoroutineName("test") + CoroutineExceptionHandler { _, e ->
                        Log.i(TAG, "异常：${e}")
                    })
                coroutineScope.launch {
                    delay(100)
                    Log.i(TAG, "child1")
                    throw IndexOutOfBoundsException()
                }
                coroutineScope.launch {
                    try {
                        repeat(1000) { i ->
                            Log.i(TAG, "child2-$i")
                            delay(500)
                        }
                    } finally {
                        Log.i(TAG, "child2 finish")
                    }
                }
            }

        }

        apply_supervisorJob.setOnClickListener {
            runBlocking {
                val supervisor =
                    CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, e ->
                        Log.i(TAG, "异常：$e")
                    })
                supervisor.launch {
                    delay(100)
                    Log.i(TAG, "child1")
                    throw NullPointerException()
                }
                supervisor.launch {
                    try {
                        repeat(1000) { i ->
                            Log.i(TAG, "child2$i")
                            delay(500)
                        }
                    } finally {
                        Log.i(TAG, "child2 finish")
                    }
                }
            }
        }

        exception_propagation.setOnClickListener {
            CoroutineScope(CoroutineExceptionHandler { _, e ->
                Log.i(TAG, "异常：$e")
            }).launch {
                Log.i(TAG,"parent start")
                launch {
                    Log.i(TAG,"childA start")
                    launch {
                        Log.i(TAG,"childA-a start")
                        delay(2000)
                        Log.i(TAG,"childA-a end")
                    }
                    delay(1000)
                    Log.i(TAG,"childA end")
                }
                launch {
                    Log.i(TAG,"childB start")
                    launch {
                        Log.i(TAG,"childB-a start")
                        delay(3000)
                        Log.i(TAG,"childB-a end")
                    }
                    delay(2000)
                    throw NullPointerException()
                    Log.i(TAG,"childB end")
                }

                launch {
                    Log.i(TAG,"childC start")
                    launch {
                        Log.i(TAG,"childC-a start")
                        delay(4000)
                        Log.i(TAG,"childC-a end")
                    }
                    delay(3000)
                    Log.i(TAG,"childC end")
                }

                Log.i(TAG,"parent continue")
                delay(3000)
                Log.i(TAG,"parent end")

            }
        }


        coroutineScope.setOnClickListener {
            CoroutineScope(CoroutineExceptionHandler { _, e ->
                Log.i(TAG, "处理异常：$e")
            }).launch {
                coroutineScope {
                    launch {
                        delay(1000)
                        Log.i(TAG, "job1")
                    }

                    launch {
                        delay(2000)
                        throw NullPointerException()
                        Log.i(TAG, "job2")
                    }

                    launch {
                        delay(3000)
                        Log.i(TAG, "job3")
                    }
                    launch {
                        delay(4000)
                        Log.i(TAG, "job4")
                    }
                }
            }
        }

        supervisorScope.setOnClickListener {
            CoroutineScope(CoroutineExceptionHandler { _, e ->
                Log.i(TAG, "处理异常：$e")
            }).launch {
                supervisorScope {
                    launch {
                        delay(1000)
                        Log.i(TAG, "job1")
                    }

                    launch {
                        delay(2000)
                        throw NullPointerException()
                        Log.i(TAG, "job2")
                    }

                    launch {
                        delay(3000)
                        Log.i(TAG, "job3")
                    }
                    launch {
                        delay(4000)
                        Log.i(TAG, "job4")
                    }
                }
            }
        }
    }
}