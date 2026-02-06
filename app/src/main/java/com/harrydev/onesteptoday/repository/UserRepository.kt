package com.harrydev.onesteptoday.repository

import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveUserName(
        userId: String,
        name: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val user = User(name)
        db.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }
}