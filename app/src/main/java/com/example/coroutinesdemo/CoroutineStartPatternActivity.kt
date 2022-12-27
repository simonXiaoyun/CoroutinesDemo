package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_coroutine_start_pattern.*
import kotlinx.coroutines.*

class CoroutineStartPatternActivity : AppCompatActivity() {
    private val TAG = "Simon"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_start_pattern)

        start_default.setOnClickListener {
            val job = GlobalScope.launch(start = CoroutineStart.DEFAULT) {
                Log.i(TAG,"job start")
                delay(1000)
                Log.i(TAG,"job end")
            }
            Log.i(TAG,"main code")
            job.cancel()
            Log.i(TAG,"main code and code")
        }

        start_atomic.setOnClickListener {
            val job = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
                Log.i(TAG,"job start")
                delay(1000)
                Log.i(TAG,"job end")
            }
            Log.i(TAG,"main code")
            job.cancel()
            Log.i(TAG,"main code and code")
        }

        start_lazy.setOnClickListener {
            runBlocking {
                val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
                    Log.i(TAG,"job start")
                    delay(1000)
                    Log.i(TAG,"job end")
                }
                Log.i(TAG,"main code")
                val job2 = GlobalScope.launch {
                    Log.i(TAG,"job2 start")
                    delay(1000)
                    Log.i(TAG,"job2 end")
                }
                job2.join()
                job.start()
            }
        }

        start_undispatched.setOnClickListener {
             GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
                    Log.i(TAG,"job start:${Thread.currentThread()}")
                    launch {
                        Log.i(TAG,"child job start:${Thread.currentThread()}")
                    }
                }
                Log.i(TAG,"main code")
        }
    }
}