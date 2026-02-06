package com.harrydev.onesteptoday.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps")
data class StepEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val shortDesc: String,
    val longDesc: String,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)
