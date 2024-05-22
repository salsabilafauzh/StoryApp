package com.example.storyapp.ui.detailStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storyapp.data.remote.response.result.ResponseGetDetailStory
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.ui.StoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private val detailStoryViewModel: DetailStoryViewModel by viewModels {
        StoryViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.topAppBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val id = intent.getStringExtra(EXTRA_ID)
        if (id != null) {
            detailStoryViewModel.apply {
                getDetail(id).observe(this@DetailStoryActivity) { res ->
                    showData(res)
                }
                isLoading().observe(this@DetailStoryActivity) {
                    showLoading(it)
                }
            }
        }
    }

    private fun showData(res: ResponseGetDetailStory?) {
        if (res != null) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val dateFormat = SimpleDateFormat("EEEE, dd MMMM, yyyy", Locale.getDefault())

            val dateParse = res.story?.createdAt?.let { inputFormat.parse(it) }
            val formattedDate = dateParse?.let { dateFormat.format(it) }

            binding.apply {
                name.text = res.story?.name
                description.text = res.story?.description
                date.text = formattedDate
                Glide.with(root)
                    .load(res.story?.photoUrl)
                    .into(image)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}