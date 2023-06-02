package com.dev.template.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev.template.R

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {

    }
    val text : LiveData<String> = _text
}