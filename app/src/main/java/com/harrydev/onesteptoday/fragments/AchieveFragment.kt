package com.harrydev.onesteptoday.fragments

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.harrydev.onesteptoday.R
import com.harrydev.onesteptoday.adapter.StepAdapter
import com.harrydev.onesteptoday.databinding.FragmentAchieveBinding
import com.harrydev.onesteptoday.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

class AchieveFragment : Fragment(R.layout.fragment_achieve) {
    private lateinit var binding: FragmentAchieveBinding
    private val vm: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAchieveBinding.bind(view)

        val adapter = StepAdapter(
            onDone = {},
            onDelete = { vm.delete(it) },
            onEdit = null,
            onCelebrate = {}
        )
        binding.recyclerAchieve.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recyclerAchieve.adapter = adapter
        vm.completedSteps.observe(viewLifecycleOwner) { steps ->

            val hasData = steps.isNotEmpty()

            binding.emptyState.isVisible = !hasData
            binding.lineChart.isVisible = hasData

            adapter.submitList(steps)

            if (hasData) {
                updateAnalyticsChart(steps)
            } else {
                binding.lineChart.clear()
            }
        }
    }
    private fun updateAnalyticsChart(
        steps: List<com.harrydev.onesteptoday.data.StepEntity>
    ) {

        val dayFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

        val grouped = steps
            .filter { it.completedAt != null }
            .groupBy {
                dayFormat.format(Date(it.completedAt!!))
            }
            .toSortedMap()

        val entries = mutableListOf<Entry>()
        val labels = mutableListOf<String>()
        entries.add(Entry(0f, 0f))
        labels.add("")

        var cumulative = 0
        grouped.entries.forEachIndexed { index, entry ->
            cumulative += entry.value.size
            entries.add(Entry((index + 1).toFloat(), cumulative.toFloat()))
            labels.add(entry.key)
        }

        val dataSet = LineDataSet(entries, "Achievements").apply {

            lineWidth = 4.5f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.25f
            color = requireContext().getColor(R.color.primaryPurple)

            setDrawCircles(true)
            circleRadius = 6f
            setCircleColor(requireContext().getColor(R.color.primaryPurple))
            setDrawCircleHole(true)
            circleHoleRadius = 3f

            setDrawValues(false)

            setDrawFilled(true)
            fillDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    requireContext().getColor(R.color.primaryPurple),
                    requireContext().getColor(android.R.color.transparent)
                )
            )

            setDrawHighlightIndicators(false)
        }

        binding.lineChart.apply {

            data = LineData(dataSet)
            animateY(
                1200,
                com.github.mikephil.charting.animation.Easing.EaseOutCubic
            )

            description.isEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = (cumulative + 1).toFloat()
                granularity = 1f
                setDrawGridLines(true)
                gridLineWidth = 0.5f
            }
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                labelCount = labels.size
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels.getOrNull(value.toInt()) ?: ""
                    }
                }
            }
            invalidate()
        }
    }
}
