package com.harrydev.onesteptoday.fragments

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.harrydev.onesteptoday.databinding.ActivityCreateStepBinding
import com.harrydev.onesteptoday.viewmodel.CreateStepViewModel
import com.harrydev.onesteptoday.utils.PrefManager
class CreateStepActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateStepBinding
    private val vm: CreateStepViewModel by viewModels()

    private var stepId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStepBinding.inflate(layoutInflater)
        setContentView(binding.root)
        stepId = intent.getIntExtra("STEP_ID", -1)

        if (stepId != -1) {
            binding.edtTitle.setText(intent.getStringExtra("TITLE"))
            binding.edtShort.setText(intent.getStringExtra("SHORT"))
            binding.edtLong.setText(intent.getStringExtra("LONG"))
        }

        binding.btnSave.setOnClickListener {

            val title = binding.edtTitle.text.toString()
            val short = binding.edtShort.text.toString()
            val long = binding.edtLong.text.toString()

            if (stepId == -1) {
                vm.save(title, short, long)
                PrefManager(this).increaseTodayCount()
            } else {
                vm.update(stepId, title, short, long)
            }
            setResult(RESULT_OK)
            finish()
        }
        binding.btnCancel.setOnClickListener { finish() }
        binding.btnBack.setOnClickListener { finish() }
    }
}
