package com.harrydev.onesteptoday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.harrydev.onesteptoday.data.StepEntity
import com.harrydev.onesteptoday.repository.StepRepository
import kotlinx.coroutines.launch

class CreateStepViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repo = StepRepository(application)

    fun save(title: String, shortDesc: String, longDesc: String) {
        viewModelScope.launch {
            repo.addStep(
                StepEntity(
                    title = title,
                    shortDesc = shortDesc,
                    longDesc = longDesc
                )
            )
        }
    }
    fun update(id: Int, title: String, shortDesc: String, longDesc: String) {
        viewModelScope.launch {
            repo.updateStep(id, title, shortDesc, longDesc)
        }
    }
}
