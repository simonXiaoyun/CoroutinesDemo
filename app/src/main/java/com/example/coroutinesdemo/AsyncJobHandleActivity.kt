package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_async_job_handle.*
import kotlinx.coroutines.*

class AsyncJobHandleActivity : AppCompatActivity() {

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.arg1 == 1) {
                user_name.text = msg.obj.toString()
            } else if (msg.arg1 == 2) {
                user_info.text = msg.obj.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_job_handle)
        coroutineWay()
    }

    private fun threadWithHandleWay() {
        val thread = Thread {
            val name = login("123")
            val msg = Message()
            msg.arg1 = 1
            msg.obj = name
            handler.sendMessage(msg)
            val userInfo = getUserInfo(name)
            val msg2 = Message()
            msg2.arg1 = 2
            msg2.obj = userInfo
            handler.sendMessage(msg2)


        }
        thread.start()
    }

    private fun rxjavaWay() {
        Single.create<String> { emitter ->
            val name = login("123")
            emitter.onSuccess(name)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                user_name.text = it
            }
            .observeOn(Schedulers.io())
            .flatMap {
                Single.just(getUserInfo(it))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) = Unit

                override fun onSuccess(t: String) {
                    user_info.text = t
                }

                override fun onError(e: Throwable) = Unit

            })
    }

    private fun coroutineWay() {
        CoroutineScope(Dispatchers.Main).launch {
            val name = loginWithCoroutine("123")
            user_name.text = name
            val userInfo = getUserInfoWithCoroutine(name)
            user_info.text = userInfo

        }
    }

    private fun login(password: String): String {
        Thread.sleep(1000)
        return "simon"
    }

    private fun getUserInfo(userName: String): String {
        Thread.sleep(1000)
        return if (userName == "simon") {
            "userInfo"
        } else {
            "error"
        }
    }

    private suspend fun loginWithCoroutine(password: String): String {
        delay(1000)
        return "simon"
    }

    private suspend fun getUserInfoWithCoroutine(userName: String): String {
        delay(1000)
        return if (userName == "simon") {
            "userInfo"
        } else {
            "error"
        }
    }

}