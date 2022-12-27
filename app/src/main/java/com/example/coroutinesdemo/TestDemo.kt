package com.example.coroutinesdemo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class TestDemo {

    private suspend fun dealA():Int {
        withContext(Dispatchers.IO) {
            delay(3000)
        }
        return 1
    }
}