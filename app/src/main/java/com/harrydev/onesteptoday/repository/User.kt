package com.harrydev.onesteptoday.repository

data class User(
    val name: String = "",
    val createdAt: Long = System.currentTimeMillis()
)