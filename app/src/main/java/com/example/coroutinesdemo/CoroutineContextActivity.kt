package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_coroutine_context.*
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

class CoroutineContextActivity : AppCompatActivity() {
    private val TAG = "Simon"

    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_context)

        get_coroutineContext.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val context = getCoroutineContext()
                val dispatcher = context[CoroutineDispatcher]
                val name = context[CoroutineName]
                val job = context[Job]
                Log.i(TAG, "dispatcher:$dispatcher")
                Log.i(TAG, "name:$name")
                Log.i(TAG, "job:$job")
            }

        }
        use_coroutineContext.setOnClickListener {
            useCoroutineContext()
        }

        extend_coroutineContext.setOnClickListener {
            coroutineContextExtend()
        }
    }


    private suspend fun getCoroutineContext() = coroutineContext

    @OptIn(ExperimentalStdlibApi::class)
    private fun useCoroutineContext() {

        CoroutineScope(Dispatchers.IO + CoroutineName("demo") + CoroutineExceptionHandler { _, _ -> } + Job()).launch {
            delay(1000)
            val context = this.coroutineContext
            val dispatcher = context[CoroutineDispatcher]
            val name = context[CoroutineName]
            val job = context[Job]
            Log.i(TAG, "dispatcher:$dispatcher")
            Log.i(TAG, "name:$name")
            Log.i(TAG, "job:$job")
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun coroutineContextExtend() {
        val jo = Job()
        val job = CoroutineScope(CoroutineName("Parent") + Dispatchers.Main + jo)
            .launch(CoroutineName("extend")) {
                val context = this.coroutineContext
                val dispatcher = context[CoroutineDispatcher]
                val name = context[CoroutineName]
                val job = context[Job]
                val handler = context[CoroutineExceptionHandler]
                Log.i(TAG, "dispatcher:$dispatcher")
                Log.i(TAG, "name:$name")
                Log.i(TAG, "job:$job")
                Log.i(TAG, "handler:$handler")
            }
        Log.i(TAG, "jo:$jo")
        Log.i(TAG, "job:$job")
    }
}