package com.example.coroutinesdemo

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dispatchers.*
import kotlinx.coroutines.*
import kotlinx.coroutines.android.asCoroutineDispatcher
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class CoroutineDispatchersActivity : AppCompatActivity() {
    private val TAG = "Simon"
    private val handlerThread = HandlerThread("handle_thread")
    private var handle: Handler = handlerThread.run {
        start()
        Handler(this.looper)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatchers)
        change_main.setOnClickListener {
            changeWorld()
        }

        change_IO.setOnClickListener {
            changeWorldWithIODispatchers()
        }

        change_default.setOnClickListener {
            changeWorldWithDefaultDispatchers()
        }

        use_unconfined.setOnClickListener {
            useUnconfined()
        }

        use_handlerThread.setOnClickListener {
            useHandlerThread()
        }

        use_thread_executor.setOnClickListener {
            useThreadExecutor()
        }

        use_custom_dispatcher.setOnClickListener {
            useCustomDispatcher()
        }

    }

    private suspend fun generateWorld(): String {
        delay(500)
        return "change world"
    }

    private fun changeWorld() {
        CoroutineScope(Dispatchers.Main).launch {
            Log.i(TAG, "Main线程：${Thread.currentThread().name}")
            val str = generateWorld()
            head_title.text = str
        }
    }

    private fun changeWorldWithIODispatchers() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i(TAG, "IO线程：${Thread.currentThread().name}")
            val str = generateWorld()
            head_title.text = str
        }
    }

    private fun changeWorldWithDefaultDispatchers() {
        CoroutineScope(Dispatchers.Default).launch {
            Log.i(TAG, "Default线程：${Thread.currentThread().name}")
            val str = generateWorld()
            withContext(Dispatchers.Main) {
                head_title.text = str
            }
        }
    }

    private fun useUnconfined(){
        Thread{
            CoroutineScope(Dispatchers.Unconfined).launch {
                Log.i(TAG, "Unconfined线程：${Thread.currentThread().name}")
            }
        }.start()
    }

    private fun useHandlerThread() {
        CoroutineScope(handle.asCoroutineDispatcher("handle_thread")).launch {
            Log.i(TAG, "HandlerThread线程：${Thread.currentThread().name}")
        }
    }

    private fun useThreadExecutor() {
        CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()).launch {
            Log.i(TAG, "线程池的线程：${Thread.currentThread().name}")
        }
    }

    private fun useCustomDispatcher() {
        CoroutineScope(SingleDispatcher()).launch {
            Log.i(TAG, "自定义的线程：${Thread.currentThread().name}")
        }
    }


     class SingleDispatcher : ExecutorCoroutineDispatcher() {
        private val myExecutor: Executor by lazy {
            Executors.newSingleThreadExecutor()
        }

        override val executor: Executor
            get() = myExecutor

        override fun close() = Unit

        override fun dispatch(context: CoroutineContext, block: Runnable) {
            executor.execute(block)
        }
    }
}