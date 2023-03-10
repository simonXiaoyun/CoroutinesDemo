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
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_scope)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[TestViewModel::class.java]


        globalScope_btn.setOnClickListener {
            GlobalScope.launch {
                Log.i(TAG,"GlobalScope-start")
                delay(2000)
                Log.i(TAG, "${Thread.currentThread()}:GlobalScope-end")
                launch {
                    Log.i(TAG,"GlobalScope-child-start")
                    delay(2000)
                    Log.i(TAG,"GlobalScope-child-end")
                }
                //是否可以取消？
//                cancel()

            }
            //是否可以取消？
            GlobalScope.cancel()
        }

        mainScope_btn.setOnClickListener {
            mainScope.launch {
                delay(2000)
                Log.i(TAG, "${Thread.currentThread()}:mainScope-end")
                launch {
                    Log.i(TAG,"MainScope-child-start")
                    delay(2000)
                    Log.i(TAG,"MainScope-child-end")
                }
            }
            //可以取消
            mainScope.cancel()
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


    //测试Scope传job,取消相关
    private fun demoWithLifecycleScope() {
        lifecycleScope.launch {
            delay(3000)
            Log.i(TAG, "${Thread.currentThread()}:lifecycleScope -end")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    //关于MainScope 和 lifecycleScope的应用场景及区别
    // GlobalScope 与 CoroutineScope  CoroutineScope是接口，而GlobalScope是实现的子类，传入的context是EmptyCoroutineContext
}