package com.example.coroutinesdemo

import kotlinx.coroutines.*

class TestDemo {

    private suspend fun dealA():Int {
        withContext(Dispatchers.IO) {
            delay(3000)
        }
        return 1
    }
}