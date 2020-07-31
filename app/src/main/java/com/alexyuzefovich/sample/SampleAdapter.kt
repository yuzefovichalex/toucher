package com.alexyuzefovich.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alexyuzefovich.sample.databinding.ItemSampleBinding

class SampleAdapter : ListAdapter<SampleEntity, SampleAdapter.ViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<SampleEntity>() {
        override fun areItemsTheSame(
            oldItem: SampleEntity,
            newItem: SampleEntity
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: SampleEntity,
            newItem: SampleEntity
        ): Boolean = oldItem == newItem
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSampleBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    class ViewHolder(
        private val binding: ItemSampleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: SampleEntity) {
            binding.itemLabel.text = entity.text
        }

    }

}