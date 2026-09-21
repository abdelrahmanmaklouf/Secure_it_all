package com.example.secure_it_all.model

import com.example.secure_it_all.R

enum class AlertStatus { SAFE, WARNING, DANGER }

data class AppAlert(
    val name: String,
    val description: String,
    val status: AlertStatus,
    val iconInitials: String
)

fun AlertStatus.labelText() = when (this) {
    AlertStatus.DANGER -> "Needs action"
    AlertStatus.WARNING -> "Worth a look"
    AlertStatus.SAFE -> "All good"
}

fun AlertStatus.bgColorRes() = when (this) {
    AlertStatus.DANGER -> R.color.danger_bg
    AlertStatus.WARNING -> R.color.warning_bg
    AlertStatus.SAFE -> R.color.safe_bg
}

fun AlertStatus.textColorRes() = when (this) {
    AlertStatus.DANGER -> R.color.danger_red
    AlertStatus.WARNING -> R.color.warning_yellow
    AlertStatus.SAFE -> R.color.safe_green
}