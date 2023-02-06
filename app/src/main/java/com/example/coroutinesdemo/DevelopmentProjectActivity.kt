package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_development_project.*
import kotlinx.coroutines.*

class DevelopmentProjectActivity : AppCompatActivity() {
    private val TAG = "Simon"
    private var designerJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_development_project)
        rx_way.setOnClickListener {
            rxjavaPattern()
        }

        cr_way.setOnClickListener {
            coroutinePattern()
        }

        stop_designer.setOnClickListener {
            designerJob?.cancel()
        }
    }

    private fun rxjavaPattern() {
        //PM 需求管理
        rxDemandManage().flatMap {
            Log.i(TAG, "需求管理完成")
            //设计
            Single.zip(
                rxDesign("设计师1"),
                rxDesign("设计师2")
            ) { result1, result2 ->
                Log.i(TAG, "设计完成")
                result1 + result2
            }

        }.flatMap {
            //开发
            Single.zip(
                rxDevelop("开发人员1"),
                rxDevelop("开发人员2"),
                rxDevelop("开发人员3"),
            ) { result1, result2, result3 ->

                Log.i(TAG, "开发完成")
                result1 + result2 + result3
            }
        }.flatMap {
            //测试
            rxQa()
        }.flatMap {
            //验收
            pmCheck()
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Boolean> {
                override fun onSubscribe(d: Disposable) = Unit

                override fun onSuccess(t: Boolean) {
                    if (t) {
                        Log.i(TAG, "项目开发完工")
                    } else {
                        Log.i(TAG, "项目验收未通过")
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i(TAG, "发生异常：${e.message}")
                }

            })
    }

    private fun coroutinePattern() {
        CoroutineScope(Dispatchers.Main + CoroutineExceptionHandler { _, e ->
            Log.i(TAG, "发生异常：$e")
        }).launch {
            //需求管理
            crDemandManage()
            Log.i(TAG, "需求管理完成")
            async {
                launch {
                    val result = crDesign("设计师1")
                    Log.i(TAG, result)
                }
                designerJob = launch {
                    val result = crDesign("设计师2")
                    Log.i(TAG, result)
                }
            }.await()
            async {
                supervisorScope {
                    launch { crDevelop("开发者1") }
                    launch {
                        crDevelop("开发者2")
                        throw NullPointerException()
                    }
                    launch { crDevelop("开发者3") }
                }
            }.await()
            Log.i(TAG, "开发完成")
            async { crQa() }.await()
            Log.i(TAG, "测试完成")
            val result = async { crCheck() }.await()
            Log.i(TAG, "验收完成")
            if (result) {
                Log.i(TAG, "项目开发完工")
            } else {
                Log.i(TAG, "项目验收未通过")
            }

        }
    }

    private fun rxDemandManage(): Single<String> {
        return Single.create<String> { emitter ->
            Log.i(TAG, "需求开始管理")
            Thread.sleep(500)
            emitter.onSuccess("需求管理完成")
        }.subscribeOn(Schedulers.io())
    }

    private fun rxDesign(designer: String): Single<String> {
        return Single.create<String?> { emitter ->
            Log.i(TAG, "${designer}开始设计")
            Thread.sleep(5000)
            emitter.onSuccess("${designer}设计完成")
        }.subscribeOn(Schedulers.io())
    }

    private fun rxDevelop(developer: String): Single<String> {
        return Single.create<String?> { emitter ->
            Log.i(TAG, "${developer}开始开发")
            Thread.sleep(5000)
            emitter.onSuccess("${developer}开发完成")
//            emitter.onError(NullPointerException("开发者缺失"))
        }.subscribeOn(Schedulers.io())
    }

    private fun rxQa(): Single<String> {
        return Single.create<String?> { emitter ->
            Log.i(TAG, "开始测试")
            Thread.sleep(5000)
            Log.i(TAG, "测试完成")
            emitter.onSuccess("测试完成")
        }.subscribeOn(Schedulers.io())
    }

    private fun pmCheck(): Single<Boolean> {
        return Single.create<Boolean> { emitter ->
            Log.i(TAG, "开始验收")
            Thread.sleep(5000)
            Log.i(TAG, "验收完成")
            emitter.onSuccess(true)
        }.subscribeOn(Schedulers.io())
    }


    private suspend fun crDemandManage(): String {
        Log.i(TAG, "需求开始管理")
        delay(5000)
        return "需求管理完成"
    }

    private suspend fun crDesign(designer: String): String {
        Log.i(TAG, "${designer}开始设计")
        delay(5000)
        return "${designer}设计完成"
    }

    private suspend fun crDevelop(developer: String): String {
        Log.i(TAG, "${developer}开始开发")
        delay(5000)
        return "${developer}开发完成"
    }

    private suspend fun crQa(): String {
        Log.i(TAG, "开始测试")
        delay(5000)
        return "测试完成"
    }

    private suspend fun crCheck(): Boolean {
        Log.i(TAG, "开始验收")
        delay(5000)
        return true
    }
}