package com.example.storyapp.ui.home.utils

import com.example.storyapp.data.local.database.story.StoryEntity

object DataDummy {
    fun generateDataStoryEntity(): List<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        for (i in 1..5) {
            val story = StoryEntity(
                "story-r09G0gnldi_8pZc3",
                "https://story-api.dicoding.dev/images/stories/photos-1716304387219_c90b1ae45d3f38984f7b.jpg",
                "2024-05-21T15:13:07.220Z",
                "testingbro",
                "pake lokasi dikit",
                106.87030642297793,
                -6.342342342342342
            )
            storyList.add(story)
        }
        return storyList
    }
}