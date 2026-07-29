package com.brian.campusgig.data.models

data class studentNavItem<T: Any>(
    val name: String,
    val route: T,
    val icon: Int
)

