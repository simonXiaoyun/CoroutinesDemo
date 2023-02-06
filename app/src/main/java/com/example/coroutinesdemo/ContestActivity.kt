package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_contest.*
import kotlinx.coroutines.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ContestActivity : AppCompatActivity() {
    private val TAG = "Simon"
    private val playerList = ArrayList<String>().apply {
        var char = 'A'
        while (char <= 'T') {
            add(char.toString())
            char++
        }
    }
    private var lucky: String? = null

    private var contestJob: Job? = null

    private val drawLotsMap = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contest)
        start.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                drawLots()
                contest()
            }
        }
        cancel.setOnClickListener {
            contestJob?.cancel()
        }

        resume.setOnClickListener {
           CoroutineScope(Dispatchers.Default).launch{
               contest()
           }
        }

    }

    private suspend fun drawLots() {
        Log.i(TAG, "参数成员：$playerList")
        var count = playerList.size
        if (count % 2 != 0) {
            lucky = playerList.removeAt((0 until count).random())
        }
        while (playerList.size > 0) {
            val player = playerList.removeAt(0)
            Log.i(TAG, "首轮人：$player")
            if (playerList.size > 0) {
                delay(1000)
                val rival = playerList.removeAt((0 until playerList.size).random())
                Log.i(TAG, "首轮对手：$rival")
                drawLotsMap[player] = rival
            } else {
                lucky?.let {
                    drawLotsMap[player] = it
                }

            }
        }
        Log.i(TAG, "抽签成员：$drawLotsMap")

    }

    private suspend fun contest() {
        coroutineScope {
            contestJob = launch {
                drawLotsMap.forEach {
                    launch {
                        delay(5000)
                        val list = ArrayList<String>()
                        list.add(it.key)
                        list.add(it.value)
                        val winner = list[(0..1).random()]
                        Log.i(TAG, "此轮比赛胜出者：$winner")
                        playerList.add(winner)
                    }
                }
                drawLotsMap.clear()
            }
            contestJob?.invokeOnCompletion {
                if(it!=null){
                    Log.i(TAG, "发生意外")
                    return@invokeOnCompletion
                }
                if (playerList.size > 1) {
                    launch {
                        drawLots()
                        contest()
                    }
                } else {
                    Log.i(TAG, "胜出者是：${playerList[0]}")
                }
            }
        }

    }
}