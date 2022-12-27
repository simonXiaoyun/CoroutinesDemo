package com.example.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PrincipleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principle)

        request()
    }

    private suspend fun login(name: String): String {
        delay(1000)
        return "success"
    }

    private suspend fun getUserInfo(token: String): String {
        delay(1000)
        return "Simon"
    }

    private fun request(){
        GlobalScope.launch(Dispatchers.Main) {
            val state = login("sxiao")
            Toast.makeText(this@PrincipleActivity, state, Toast.LENGTH_SHORT)
            val name = getUserInfo(state)
            Toast.makeText(this@PrincipleActivity, name, Toast.LENGTH_SHORT)
        }
    }
}


