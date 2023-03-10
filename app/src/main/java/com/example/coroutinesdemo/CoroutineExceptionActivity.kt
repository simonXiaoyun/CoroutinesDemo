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
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_exception)

        automatic_transmission.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                runCatching {
                    Log.i(TAG, "start")
                    var list = emptyList<String>()
                    list[0]
                }
            }
        }

        automatic_transmission2.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                    Log.i(TAG, "start")
                    var list = emptyList<String>()
                    list[0]

            }
        }

        receive_transmission.setOnClickListener {
            val deferred = CoroutineScope(Dispatchers.Main).async {
                    Log.i(TAG, "start")
                    var list = emptyList<String>()
                    list[0]
            }

            GlobalScope.launch {
                kotlin.runCatching {
                    deferred.await()
                }
            }
        }


        receive_transmission2.setOnClickListener {
            val deferred = CoroutineScope(Dispatchers.Main).async {
                Log.i(TAG, "start")
                var list = emptyList<String>()
                list[0]
            }

            GlobalScope.launch {
                deferred.await()
            }
        }

        not_root_transmission.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
               launch {
                   delay(1000)
                   Log.i(TAG, "launch 协程")
               }

                async {
                    delay(1000)
                    Log.i(TAG, "async 协程 并抛出异常")
                    throw  NullPointerException()
                }
            }
        }

        child_coroutine_exception.setOnClickListener {
            CoroutineScope(CoroutineName("test") + CoroutineExceptionHandler { _, e ->
                Log.i(TAG, "异常：${e}")
            }).launch {
                launch {
                    launch {
                        delay(1000)
                        Log.i(TAG, "child1")
                        throw IndexOutOfBoundsException()
                    }.invokeOnCompletion {
                        Log.i(TAG, "child1 finish")
                    }
                    launch {
                        try {
                            repeat(1000) { i ->
                                Log.i(TAG, "child2-$i")
                                delay(500)
                            }
                        } finally {
                            Log.i(TAG, "child2 finish")
                        }
                    }

                    delay(500)
                    Log.i(TAG, "二级父协程 执行代码")
                    delay(1100)
                    Log.i(TAG, "二级父协程 继续执行代码")
                }.invokeOnCompletion {
                    Log.i(TAG, "二级父协程 finish")
                }
                delay(500)
                Log.i(TAG,"一级父协程执行代码")
                delay(1000)
                Log.i(TAG,"一级父协程执行继续代码")
            }
        }

        apply_supervisorJob.setOnClickListener {
            runBlocking {
                    CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, e ->
                        Log.i(TAG, "异常：$e")
                    }).apply {
                        launch {
                            delay(100)
                            Log.i(TAG, "child1")
                            throw NullPointerException()
                        }
                        launch {
                            try {
                                repeat(5) { i ->
                                    Log.i(TAG, "child2-$i")
                                    delay(500)
                                }
                            } finally {
                                Log.i(TAG, "child2 finish")
                            }
                        }
                    }

            }
        }

        apply_supervisorJob2.setOnClickListener {
            runBlocking {
                CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, e ->
                    Log.i(TAG, "异常：$e")
                }).launch {
                    launch {
                        delay(100)
                        Log.i(TAG, "child1")
                        throw NullPointerException()
                    }
                    launch {
                        try {
                            repeat(5) { i ->
                                Log.i(TAG, "child2-$i")
                                delay(500)
                            }
                        } finally {
                            Log.i(TAG, "child2 finish")
                        }
                    }
                }

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

                launch {
                    delay(1000)
                    Log.i(TAG, "协程2执行")
                    delay(3000)
                    Log.i(TAG, "协程2继续执行")
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