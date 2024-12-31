package com.laorencel.uilibrary.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class KtViewModel: ViewModel() {

//    fun run() = viewModelScope.launch(Dispatchers.IO) {
//        withContext(Dispatchers.Main){
//            // 更新 UI
//        }
//    }
}