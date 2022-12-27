package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dispatchers.*
import kotlinx.coroutines.*

class DispatchersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatchers)
        change.setOnClickListener {
            changeWorld()
        }

    }


    private suspend fun generateWorld(): String{
        delay(500)
        return "change world"
    }

    private fun changeWorld(){
        GlobalScope.launch(Dispatchers.Main) {
            val str = generateWorld()
            head_title.text = str
        }
    }

    private fun changeWorldWithIODispatchers(){
        GlobalScope.launch(Dispatchers.IO) {
            val str = generateWorld()
            head_title.text = str
        }
    }

    private fun changeWorldDefaultIODispatchers(){
        GlobalScope.launch(Dispatchers.IO) {
            val str = generateWorld()
            withContext(Dispatchers.Main){
                head_title.text = str
            }

        }
    }
}