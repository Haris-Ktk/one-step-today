package com.harrydev.onesteptoday.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.lifecycle.*
import com.harrydev.onesteptoday.data.StepEntity
import com.harrydev.onesteptoday.repository.StepRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repo = StepRepository(application)

    val steps: LiveData<List<StepEntity>> = repo.getSteps()

    val completedSteps: LiveData<List<StepEntity>> =
        MediatorLiveData<List<StepEntity>>().apply {
            addSource(steps) { list ->
                value = list
                    .filter { it.isCompleted && it.completedAt != null }
                    .sortedBy { it.completedAt }
            }
        }
    fun delete(step: StepEntity) {
        viewModelScope.launch {
            repo.deleteStep(step)
        }
    }
    fun toggleDone(step: StepEntity) {
        viewModelScope.launch {
            repo.markDone(step)
        }
    }
    fun resetAllSteps() {
        viewModelScope.launch {
            repo.deleteAllSteps()
        }
    }
    fun exportStepsAsBitmap(): Bitmap {
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 40f
        }

        val bitmap = Bitmap.createBitmap(900, 1200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        var y = 80
        canvas.drawText("One Step Today - Export", 40f, y.toFloat(), paint)

        steps.value?.forEach {
            y += 60
            canvas.drawText("• ${it.title}", 40f, y.toFloat(), paint)
        }

        return bitmap
    }
}
