package com.example.storyapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.local.database.story.StoryEntity
import com.example.storyapp.data.remote.response.result.ListStoryItem
import com.example.storyapp.databinding.ItemStoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class StoriesAdapter :
    PagingDataAdapter<StoryEntity, StoriesAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var itemClickListener: OnItemClickListener
    fun setOnClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(story: StoryEntity)
    }

    class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoryEntity) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val dateFormat = SimpleDateFormat("EEEE, dd MMMM, yyyy", Locale.getDefault())

            val date = story.createdAt.let { inputFormat.parse(it) }
            val formattedDate = date?.let { dateFormat.format(it) }

            binding.apply {
                tvName.text = story.name
                tvCreatedAt.text = formattedDate
                Glide.with(root)
                    .load(story.photoUrl)
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
            holder.itemView.setOnClickListener {
                itemClickListener.onItemClick(story)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryEntity,
                newItem: StoryEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}