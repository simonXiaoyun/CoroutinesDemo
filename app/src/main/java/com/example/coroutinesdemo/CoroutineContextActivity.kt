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
            GlobalScope.launch{
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
        GlobalScope.launch(Dispatchers.IO + CoroutineName("demo") + CoroutineExceptionHandler { _, _ -> } + Job()) {
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
    private fun coroutineContextExtend(){
        GlobalScope.launch(Dispatchers.Main) {
          launch(CoroutineName("extend")) {
                val context = this.coroutineContext
                val dispatcher = context[CoroutineDispatcher]
                val name = context[CoroutineName]
                val job = context[Job]
                Log.i(TAG, "dispatcher:$dispatcher")
                Log.i(TAG, "name:$name")
                Log.i(TAG, "job:$job")
            }
        }
    }
}