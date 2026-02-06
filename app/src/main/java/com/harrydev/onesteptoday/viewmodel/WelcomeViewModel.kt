package com.harrydev.onesteptoday.viewmodel

import androidx.lifecycle.ViewModel
import com.harrydev.onesteptoday.repository.UserRepository
import java.util.UUID

class WelcomeViewModel : ViewModel() {

    private val repo = UserRepository()
    val userId = UUID.randomUUID().toString()

    fun saveName(
        name: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        repo.saveUserName(userId, name, onSuccess, onError)
    }
}