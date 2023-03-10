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

        var parentJob: Job? = null
        var job1: Job? = null
        var job2: Job? = null
        var job3: Job? = null
        start_coroutine.setOnClickListener {
            parentJob = GlobalScope.launch {
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
            parentJob?.cancel()
        }

        cancel_child.setOnClickListener {
            job2?.cancel()
        }

        resource_close.setOnClickListener {
            GlobalScope.launch {
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

        //??????????????????????????????
        cancel_nonCancel.setOnClickListener {
            runBlocking {
                val job = launch {
                    try {
                        repeat(1000) { i ->
                            Log.i(TAG, "job sleep$i")
                            delay(500)
                        }
                    } finally {
                        //??????????????????????????????????????????????????????
                        withContext(NonCancellable) {
                            Log.i(TAG, "????????????????????????")
                            delay(2000)
                            Log.i(TAG, "??????????????????")
                        }
                    }
                }
                delay(2000)
                job.cancelAndJoin()
                Log.i(TAG, "?????????????????????????????????")
            }
        }
        //????????????
        time_out.setOnClickListener {
            runBlocking {
//                withTimeoutOrNull() //??????null?????????????????????
                withTimeout(3000){
                    repeat(1000){
                        Log.i(TAG, "job sleep$it")
                        delay(500)
                    }
                }
            }
        }
    }



}