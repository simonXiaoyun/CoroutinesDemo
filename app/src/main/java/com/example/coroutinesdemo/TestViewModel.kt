package com.example.coroutinesdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestViewModel: ViewModel() {

    private val _stringData = MutableLiveData<String>()
    val stringData: LiveData<String> = _stringData

    fun getData(){
        viewModelScope.launch {
            delay(3000)
            _stringData.value = "use ViewModelScope"
        }
    }
}