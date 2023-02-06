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
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[TestViewModel::class.java]


        globalScope_btn.setOnClickListener {
            GlobalScope.launch {
                delay(2000)
                Log.i(TAG, "${Thread.currentThread()}:GlobalScope-end")
            }
        }

        mainScope_btn.setOnClickListener {
            mainScope.launch {
                delay(2000)
                Log.i(TAG, "${Thread.currentThread()}:mainScope-end")
            }
        }

        viewModelScope_btn.setOnClickListener {
            viewModel.getData()
        }

        lifecycleScope_btn.setOnClickListener {
            demoWithLifecycleScope()
        }

        viewModel.stringData.observe(this) {
            head_title.text = it
        }

        coroutineScope_btn.setOnClickListener {
           val scope =  CoroutineScope(Dispatchers.Main)
            scope.launch {
                delay(2000)
                Log.i(TAG, "${Thread.currentThread()}:CoroutineScope-end")
            }
        }

    }

    private fun demoWithLifecycleScope() {
        lifecycleScope.launch {
            delay(3000)
            Log.i(TAG, "${Thread.currentThread()}:lifecycleScope -end")
        }
    }

    private suspend fun printWord() {
        delay(3000)
        Log.i(TAG, "print word")
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}