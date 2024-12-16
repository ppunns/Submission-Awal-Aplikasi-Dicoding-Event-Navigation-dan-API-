package com.dicoding.test.ui.DetailEvent

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.test.databinding.ActivityDetailEventBinding
import com.dicoding.test.data.local.entity.EventEntity
import com.dicoding.test.data.local.room.EventDatabase
import com.dicoding.test.data.local.room.EventDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import androidx.activity.R

class DetailEvent : AppCompatActivity() {
    private var _binding: ActivityDetailEventBinding? = null
    private val binding get() = _binding!!
    private  val DetailEventViewModel by viewModels<DetailEventViewModel>()
    private lateinit var eventDatabase: EventDatabase
    private lateinit var eventDao: EventDao
    private var isFavorite = false
    companion object {
        const val EVENT_ID = "233123123"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = intent.getIntExtra(EVENT_ID,0)
        DetailEventViewModel.getEvents(eventId)

        DetailEventViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        }

        DetailEventViewModel.eventDetail.observe(this) { eventDetail ->
            val availableQuota = eventDetail.quota - eventDetail.registrants
            with(binding) {
                eventName.text = eventDetail.name
                ownerName.text = eventDetail.ownerName
                quota.text = availableQuota.toString()
                beginTime.text = eventDetail.beginTime
                description.text = HtmlCompat.fromHtml(
                    eventDetail.description,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

            }
            Glide.with(this)
                .load(eventDetail.mediaCover)
                .into(binding.eventImage)
        }
        binding.openLinkButton.setOnClickListener{
            val registrasi = Intent(Intent.ACTION_VIEW).apply {
                data = DetailEventViewModel.eventDetail.value?.link?.toUri()
            }
            startActivity(registrasi)
        }

        // Inisialisasi database
        eventDatabase = EventDatabase.getInstance(this)
        eventDao = eventDatabase.eventDao()
        
        // Tambahkan pengecekan status favorit saat activity dibuat
        CoroutineScope(Dispatchers.IO).launch {
            val existingEvent = eventDao.getEventById(eventId.toString())
            withContext(Dispatchers.Main) {
                isFavorite = existingEvent != null
                updateFavoriteIcon()
            }
        }

        // Setup click listener untuk tombol favorite
        binding.favoriteButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val existingEvent = eventDao.getEventById(eventId.toString())
                
                val event = EventEntity(
                    id = eventId.toString(),
                    name = binding.eventName.text.toString(),
                    mediaCover = DetailEventViewModel.eventDetail.value?.mediaCover ?: "",
                    bookmarked = true
                )
                
                if (existingEvent == null) {
                    eventDao.insertEvent(listOf(event))
                    withContext(Dispatchers.Main) {
                        isFavorite = true
                        updateFavoriteIcon()
                        Toast.makeText(this@DetailEvent, "Event berhasil disimpan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    eventDao.deleteEvent(eventId.toString())
                    withContext(Dispatchers.Main) {
                        isFavorite = false
                        updateFavoriteIcon()
                        Toast.makeText(this@DetailEvent, "Event dihapus dari favorit", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Tambahkan fungsi untuk mengupdate ikon
    private fun updateFavoriteIcon() {
        binding.favoriteButton.setImageResource(
            if (isFavorite) com.dicoding.test.R.drawable.favorite
            else com.dicoding.test.R.drawable.favorite_border
        )
    }
}