package com.example.secure_it_all.data.models

data class UrlScanResult(
    val url: String,
    val isSuspicious: Boolean,
    val confidencePercent: Int,
    val signals: List<UrlSignal>,
    val recommendation: String
)

