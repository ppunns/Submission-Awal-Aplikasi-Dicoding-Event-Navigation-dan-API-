package com.dicoding.test.ui.Favorite

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.test.databinding.FragmentFavoriteBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.test.data.remote.responese.ListEventsItem
import com.dicoding.test.ui.EventAdapter
import com.dicoding.test.utils.dataStore

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    
    private val adapter: EventAdapter by lazy {
        EventAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeFavorites()
    }

    private fun setupRecyclerView() {
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@FavoriteFragment.adapter
        }
    }

    private fun observeFavorites() {
        binding.progressBar.visibility = View.VISIBLE
        
        viewModel.getFavoriteEvents().observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE
            
            val items = arrayListOf<ListEventsItem>()
            events.map {
                val item = ListEventsItem(
                    id = it.id.toInt(),
                    name = it.name,
                    imageLogo = it.mediaCover.toString()
                )
                items.add(item)
            }
            adapter.submitList(items)
            binding.emptyState.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}