package com.harrydev.onesteptoday.paywall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harrydev.onesteptoday.databinding.ItemPlanCardBinding

class PlanPagerAdapter(
    private val plans: List<PlanUi>
) : RecyclerView.Adapter<PlanPagerAdapter.PlanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding = ItemPlanCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(plans[position])
    }

    override fun getItemCount() = plans.size

    inner class PlanViewHolder(
        private val binding: ItemPlanCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: PlanUi) {
            binding.tvTitle.text = plan.title
            binding.tvPrice.text = plan.price
            binding.featuresContainer.removeAllViews()

            plan.features.forEach {
                val view = FeatureItemView(binding.root.context)
                view.setText(it)
                binding.featuresContainer.addView(view)
            }
        }
    }
}
