package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_coroutine_exception_handler.*
import kotlinx.coroutines.*
import java.lang.NullPointerException

class CoroutineExceptionHandlerActivity : AppCompatActivity() {
    private val TAG = "Simon"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_exception_handler)

        normal_Handle.setOnClickListener {
            CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
                Log.i(TAG, "异常处理：$e")
            }).launch {
                launch {
                    Log.i(TAG, "job start")
                    delay(100)
                    throw IndexOutOfBoundsException()

                }
            }
        }

        catch_case1.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                //不在根协程，无法捕获
                launch(CoroutineExceptionHandler { _, e ->
                    Log.i(TAG, "异常处理：$e")
                }) {
                    launch {
                        Log.i(TAG, "case1 job start")
                        delay(100)
                        throw IndexOutOfBoundsException()

                    }
                }
            }
        }

        catch_case2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                launch(CoroutineExceptionHandler { _, e ->
                    Log.i(TAG, "异常处理：$e")
                }) {
                    Log.i(TAG, "case2 job start")
                    delay(100)
                    throw IndexOutOfBoundsException()

                }
            }
        }

        catch_case3.setOnClickListener {
            CoroutineScope(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
                Log.i(TAG, "异常处理：$e")
            }).launch(SupervisorJob()) {
                launch {
                    Log.i(TAG, "case3 job1 start")
                    delay(100)
                    Log.i(TAG, "case3 job1 end")
                    throw IndexOutOfBoundsException()

                }

                launch {
                    Log.i(TAG, "case3 job2 start")
                    delay(200)
                    Log.i(TAG, "case3 job2 end")
                }
            }
        }

        catch_case4.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                launch {
                    val job1 = async {
                        Log.i(TAG, "case4 job1 start")
                        delay(100)
                        Log.i(TAG, "case4 job1 end")
                        throw IndexOutOfBoundsException()

                    }

                    val job2 = async {
                        Log.i(TAG, "case4 job2 start")
                        delay(200)
                        Log.i(TAG, "case4 job2 end")
                    }

//                    runCatching {
//                        job1.await()
//                        job2.await()
//                    }
                }
            }
        }

        catch_case5.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                launch {
                    val job1 = async(SupervisorJob()) {
                        Log.i(TAG, "case5 job1 start")
                        delay(100)
                        Log.i(TAG, "case5 job1 end")
                        throw IndexOutOfBoundsException()

                    }

                    val job2 = async {
                        Log.i(TAG, "case5 job2 start")
                        delay(200)
                        Log.i(TAG, "case5 job2 end")
                    }

                    try {
                        job1.await()
                        job2.await()
                    } catch (e: Exception) {
                        Log.i(TAG, "异常：$e")
                    }
                }
            }
        }

        catch_case6.setOnClickListener {
            GlobalScope.launch {
                launch(SupervisorJob() + CoroutineExceptionHandler { _, e ->
                    Log.i(TAG, "异常处理$e")
                }) {
                    Log.i(TAG, "case6 job1 start")
                    throw  NullPointerException()
                    Log.i(TAG, "case6 job1 end")
                }

                launch(SupervisorJob() + CoroutineExceptionHandler { _, e ->
                    Log.i(TAG, "异常处理$e")
                }) {
                    Log.i(TAG, "case6 job2 start")
                    delay(100)
                    Log.i(TAG, "case6 job2 end")
                }
            }
        }

        exception_aggregation.setOnClickListener {
            GlobalScope.launch(CoroutineExceptionHandler { _, e ->
                Log.i(TAG, "异常处理:$e---${e.suppressed.contentToString()}")
            }) {
                launch {
                    Log.i(TAG, "aggregation job1 start")
                    delay(1000)
                    throw  NullPointerException()
                    Log.i(TAG, "aggregation job1 end")
                }

                launch {
                    try {
                        Log.i(TAG, "aggregation job2 start")
                        delay(Long.MAX_VALUE)
                    } finally {
                        throw  IndexOutOfBoundsException()
                        Log.i(TAG, "aggregation job2 end")
                    }


                }

                launch {
                    try {
                        Log.i(TAG, "aggregation job3 start")
                        delay(Long.MAX_VALUE)
                    } finally {
                        throw  IllegalAccessException()
                        Log.i(TAG, "aggregation job3 end")
                    }

                }

            }
        }

        global_coroutineExceptionHandler.setOnClickListener {
            GlobalScope.launch {
                throw NullPointerException()
            }
        }
    }
}