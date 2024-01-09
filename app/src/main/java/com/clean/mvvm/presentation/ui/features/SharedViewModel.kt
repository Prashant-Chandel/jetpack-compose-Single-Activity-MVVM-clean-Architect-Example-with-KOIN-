package com.clean.mvvm.presentation.ui.features

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedViewModel : ViewModel() {
    private val _updateState = MutableStateFlow(false)
    val hasUpdatedState: StateFlow<Boolean> = _updateState

    fun updateCounterState(value: Boolean) {
        _updateState.value = value
    }
}