package com.dicoding.test.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.test.databinding.FragmentHomeBinding
import com.dicoding.test.ui.EventAdapter


class HomeFragment : Fragment(){
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventsAdapter: EventAdapter
    private  val DetailHomeViewModel by viewModels<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventsAdapter = EventAdapter()
        binding.rvReview.layoutManager = LinearLayoutManager(context)
        binding.rvReview.adapter = eventsAdapter


        DetailHomeViewModel.getEvents(1)

        DetailHomeViewModel.eventList.observe(viewLifecycleOwner) { eventList ->
            eventsAdapter.submitList(eventList)
        }
        DetailHomeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}