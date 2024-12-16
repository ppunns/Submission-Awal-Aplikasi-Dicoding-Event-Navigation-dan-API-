package com.dicoding.test.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.test.data.remote.responese.ListEventsItem
import com.dicoding.test.databinding.ItemEventBinding
import com.dicoding.test.ui.DetailEvent.DetailEvent


class EventAdapter : ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener{
            val intentDetail = Intent(holder.itemView.context, DetailEvent::class.java)
            intentDetail.putExtra(DetailEvent.EVENT_ID, event.id)
            holder.itemView.context.startActivity(intentDetail)
        }
    }
    class MyViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(event : ListEventsItem){
            binding.titleTextView.text = event.name
            Glide.with(binding.imageView.context)
                .load(event.imageLogo)
                .into(binding.imageView)
        }
    }

    //    Default
    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>(){
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
