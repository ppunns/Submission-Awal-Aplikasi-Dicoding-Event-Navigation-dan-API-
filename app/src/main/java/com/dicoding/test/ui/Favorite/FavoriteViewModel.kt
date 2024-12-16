package com.dicoding.test.ui.Favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.test.data.local.entity.EventEntity
import com.dicoding.test.data.local.room.EventDao

class FavoriteViewModel(private val eventDao: EventDao) : ViewModel() {
    
    fun getFavoriteEvents(): LiveData<List<EventEntity>> {
        return eventDao.getBookmarkedEvent()
    }
}