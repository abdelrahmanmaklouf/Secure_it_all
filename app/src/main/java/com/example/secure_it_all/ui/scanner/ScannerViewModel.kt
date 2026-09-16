package com.example.secure_it_all.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secure_it_all.data.models.UrlScanResult
import com.example.secure_it_all.detection.phishing.PhishingChecker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScannerViewModel : ViewModel() {
    private val checker = PhishingChecker()

    private val _scanState = MutableStateFlow<ScanState>(ScanState.Idle)
    val scanState: StateFlow<ScanState> = _scanState

    fun scanUrl(url: String) {
        _scanState.value = ScanState.Loading
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) { checker.scan(url) }
            _scanState.value = ScanState.Result(result)
        }
    }

    fun reset() {
        _scanState.value = ScanState.Idle
    }
}

sealed class ScanState {
    object Idle : ScanState()
    object Loading : ScanState()
    data class Result(val result: UrlScanResult) : ScanState()
}

