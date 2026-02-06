package com.harrydev.onesteptoday.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.harrydev.onesteptoday.R
import com.harrydev.onesteptoday.adapter.StepAdapter
import com.harrydev.onesteptoday.databinding.FragmentHistoryBinding
import com.harrydev.onesteptoday.viewmodel.HomeViewModel

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var binding: FragmentHistoryBinding
    private val vm: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)

        val adapter = StepAdapter(
            onDone = { vm.toggleDone(it) },
            onDelete = { vm.delete(it) },
            onEdit = { step ->
                val intent =
                    Intent(requireContext(), CreateStepActivity::class.java)
                intent.putExtra("STEP_ID", step.id)
                intent.putExtra("TITLE", step.title)
                intent.putExtra("SHORT", step.shortDesc)
                intent.putExtra("LONG", step.longDesc)
                startActivity(intent)
            },
            onCelebrate = {
                showCelebration()
            }
        )

        binding.recyclerHistory.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerHistory.adapter = adapter

        vm.steps.observe(viewLifecycleOwner) { steps ->
            adapter.submitList(steps)
        }
    }
    private fun showCelebration() {
        Toast.makeText(
            requireContext(),
            "🎉 Step completed!",
            Toast.LENGTH_SHORT
        ).show()
    }
}
