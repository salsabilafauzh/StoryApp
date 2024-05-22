package com.example.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.data.local.database.story.StoryEntity
import com.example.storyapp.databinding.FragmentHomeBinding
import com.example.storyapp.ui.StoryViewModelFactory
import com.example.storyapp.ui.adapter.LoadingStateAdapter
import com.example.storyapp.ui.adapter.StoriesAdapter
import com.example.storyapp.ui.detailStory.DetailStoryActivity
import com.example.storyapp.ui.submitStory.SubmitStoryActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val mainViewModel: HomeViewModel by viewModels {
        StoryViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        binding.apply {
            val layoutManager = LinearLayoutManager(requireContext())
            rcStoryList.layoutManager = layoutManager
            val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
            rcStoryList.addItemDecoration(itemDecoration)

            swiperefresh.setOnRefreshListener {
                swiperefresh.isRefreshing = false
                loadData()
            }
            fabAddStory.setOnClickListener {
                startActivity(Intent(requireContext(), SubmitStoryActivity::class.java))
            }
        }
    }

    private fun loadData() {
        val adapter = StoriesAdapter()
        binding.rcStoryList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.loadStories().observe(requireActivity()) {
            adapter.submitData(lifecycle, it)
        }
        adapter.setOnClickListener(object : StoriesAdapter.OnItemClickListener {
            override fun onItemClick(story: StoryEntity) {
                val intent = Intent(requireContext(), DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_ID, story.id)
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}