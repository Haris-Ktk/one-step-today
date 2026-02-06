package com.harrydev.onesteptoday.adapter

import android.view.*
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.*
import com.harrydev.onesteptoday.R
import com.harrydev.onesteptoday.data.StepEntity
import com.harrydev.onesteptoday.databinding.ItemStepBinding

class StepAdapter(
    val onDone: (StepEntity) -> Unit,
    val onDelete: (StepEntity) -> Unit,
    val onEdit: ((StepEntity) -> Unit)?,
    val onCelebrate: () -> Unit
) : ListAdapter<StepEntity, StepAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<StepEntity>() {
        override fun areItemsTheSame(a: StepEntity, b: StepEntity) = a.id == b.id
        override fun areContentsTheSame(a: StepEntity, b: StepEntity) = a == b
    }
    inner class VH(val b: ItemStepBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(step: StepEntity) {

            b.txtTitle.text = step.title
            b.txtDesc.text = step.shortDesc
            b.btnDone.visibility = View.VISIBLE

            if (step.isCompleted) {
                b.btnDone.setImageResource(R.drawable.ic_check)
                b.btnDone.isEnabled = false
            } else {
                b.btnDone.setImageResource(R.drawable.ic_unchecked)
                b.btnDone.isEnabled = true
                b.btnDone.setOnClickListener {
                    onDone(step)
                    onCelebrate()
                }
            }
            b.btnDelete.setOnClickListener {
                onDelete(step)
            }
            b.btnEdit.isVisible = onEdit != null
            b.btnEdit.setOnClickListener {
                onEdit?.invoke(step)
            }
        }
    }
    override fun onCreateViewHolder(p: ViewGroup, v: Int) =
        VH(ItemStepBinding.inflate(LayoutInflater.from(p.context), p, false))

    override fun onBindViewHolder(h: VH, p: Int) =
        h.bind(getItem(p))
}
