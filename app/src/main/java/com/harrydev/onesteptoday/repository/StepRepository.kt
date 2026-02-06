package com.harrydev.onesteptoday.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.harrydev.onesteptoday.data.*

class StepRepository(context: Context) {

    private val dao = AppDatabase.get(context).stepDao()

    fun getSteps(): LiveData<List<StepEntity>> =
        dao.getAllLive()

    fun getCompletedSteps(): LiveData<List<StepEntity>> =
        dao.getCompletedLive()

    suspend fun addStep(step: StepEntity) {
        dao.insert(step)
    }

    suspend fun deleteStep(step: StepEntity) {
        dao.delete(step)
    }

    suspend fun deleteAllSteps() {
        dao.deleteAll()
    }

    suspend fun markDone(step: StepEntity) {
        dao.updateDone(
            step.id,
            !step.isCompleted,
            if (!step.isCompleted) System.currentTimeMillis() else null
        )
    }

    suspend fun updateStep(
        id: Int,
        title: String,
        shortDesc: String,
        longDesc: String
    ) {
        dao.update(id, title, shortDesc, longDesc)
    }
}
