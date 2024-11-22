package com.dicoding.test.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.test.databinding.FragmentDashboardBinding
import com.dicoding.test.ui.EventAdapter

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventsAdapter: EventAdapter
    private  val DetailDashboredViewModel by viewModels<DashboardViewModel>()
    companion object {
        private const val TAG = "MainActivity"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvReview.layoutManager = LinearLayoutManager(context)
        eventsAdapter = EventAdapter()
        binding.rvReview.adapter = eventsAdapter

        DetailDashboredViewModel.getEvents(0)

        DetailDashboredViewModel.eventList.observe(viewLifecycleOwner) { eventList ->
            eventsAdapter.submitList(eventList)
        }
        DetailDashboredViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
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