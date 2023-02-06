package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_dispatchers.*
import kotlinx.coroutines.*

class CoroutineDispatchersActivity : AppCompatActivity() {
    private val TAG = "Simon"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatchers)
        change.setOnClickListener {
            changeWorld()
            changeWorldWithIODispatchers()
            changeWorldWithDefaultDispatchers()
        }

    }


    private suspend fun generateWorld(): String{
        delay(500)
        return "change world"
    }

    private fun changeWorld(){
        CoroutineScope(Dispatchers.Main).launch {
            Log.i(TAG,"Main线程：${Thread.currentThread().name}")
            val str = generateWorld()
            head_title.text = str
        }
    }

    private fun changeWorldWithIODispatchers(){
        CoroutineScope(Dispatchers.IO).launch {
            Log.i(TAG,"IO线程：${Thread.currentThread().name}")
            val str = generateWorld()
            head_title.text = str
        }
    }

    private fun changeWorldWithDefaultDispatchers(){
        CoroutineScope(Dispatchers.Default).launch {
            Log.i(TAG,"Default线程：${Thread.currentThread().name}")
            val str = generateWorld()
            withContext(Dispatchers.Main){
                head_title.text = str
            }

        }
    }
}