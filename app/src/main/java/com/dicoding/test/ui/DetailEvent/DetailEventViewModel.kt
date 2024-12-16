package com.dicoding.test.ui.DetailEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.test.data.remote.responese.DetailEventResponse
import com.dicoding.test.data.remote.responese.Event
import com.dicoding.test.data.remote.retrofit.ApiConfig
import com.dicoding.test.databinding.ActivityDetailEventBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel: ViewModel() {
    private lateinit var binding : ActivityDetailEventBinding
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eventDetail = MutableLiveData<Event>()
    val eventDetail: LiveData<Event> = _eventDetail

    companion object {
        private const val TAG = "MainActivity"
    }
    fun getEvents(active: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(active.toString())
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null){
                    val responseBody =  response.body()!!
                    _eventDetail.value = responseBody?.event
                }else{
                    Log.e(TAG,"onFailure: ${response.message()}" )
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}