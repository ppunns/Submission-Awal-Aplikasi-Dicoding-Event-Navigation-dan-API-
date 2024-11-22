package com.dicoding.test.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.dicoding.test.databinding.ActivityDetailEventBinding

class DetailEvent : AppCompatActivity() {
    private var _binding: ActivityDetailEventBinding? = null
    private val binding get() = _binding!!
    private  val DetailEventViewModel by viewModels<DetailEventViewModel>()
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

    }
}