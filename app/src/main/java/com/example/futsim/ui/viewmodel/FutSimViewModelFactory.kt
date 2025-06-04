package com.example.futsim.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.futsim.data.FutSimRepository

class FutSimViewModelFactory(
    private val repository: FutSimRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FutSimViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FutSimViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}