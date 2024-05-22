package com.example.storyapp.data.local.database.story

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey val id: String,
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lon: Double,
    val lat: Double
)