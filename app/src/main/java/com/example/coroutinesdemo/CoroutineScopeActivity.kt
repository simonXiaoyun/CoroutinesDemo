package com.example.coroutinesdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_coroutine_scope.*
import kotlinx.coroutines.*

class CoroutineScopeActivity : AppCompatActivity() {

    private val TAG = "Simon"
    private var mainScope = MainScope()
    private lateinit var viewModel: TestViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_scope)
        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(application))[TestViewModel::class.java]
        mainScope_btn.setOnClickListener {
            demoWithMainScope()
        }

        viewModelScope_btn.setOnClickListener {
            viewModel.getData()
        }

        lifecycleScope_btn.setOnClickListener {
            demoWithLifecycleScope()
        }

        viewModel.stringData.observe(this){
            head_title.text = it
        }

    }


    private fun demoWithMainScope(){

        mainScope.launch {
            delay(5000)
            Log.i(TAG,"${this@CoroutineScopeActivity}:mainScope-end")
        }
    }

    private fun demoWithLifecycleScope(){
        lifecycleScope.launch {
            delay(5000)
            Log.i(TAG,"${this@CoroutineScopeActivity}:lifecycleScope -end")
        }
    }

    private suspend fun printWord(){
        delay(3000)
        Log.i(TAG,"print word")
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}