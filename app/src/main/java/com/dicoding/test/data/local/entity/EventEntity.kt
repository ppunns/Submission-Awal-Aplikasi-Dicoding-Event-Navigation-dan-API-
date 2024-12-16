package com.dicoding.test.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "event")
data class EventEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var name: String = "",
    var mediaCover: String? = null,
    var bookmarked: Boolean = false
)