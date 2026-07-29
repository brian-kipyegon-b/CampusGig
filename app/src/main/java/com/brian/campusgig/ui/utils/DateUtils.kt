package com.brian.campusgig.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDate(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}

fun Long.toDateTime(): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return formatter.format(Date(this))
}