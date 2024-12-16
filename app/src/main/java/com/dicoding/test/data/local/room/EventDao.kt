package com.dicoding.test.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.test.data.local.entity.EventEntity


@Dao
interface EventDao {

    @Query("SELECT * FROM event")
    fun getAllEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM event where bookmarked = 1")
    fun getBookmarkedEvent(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(news: List<EventEntity>)

    @Update
    fun updateEvent(event: EventEntity)

    @Query("DELETE FROM event WHERE bookmarked = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM event WHERE name = :title AND bookmarked = 1)")
    fun isNewsBookmarked(title: String): Boolean

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?

    @Query("DELETE FROM event WHERE id = :id")
    suspend fun deleteEvent(id: String)
}