package com.dicoding.test.ui.Setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.test.R
import com.dicoding.test.data.SettingPreferences
import com.google.android.material.switchmaterial.SwitchMaterial
import androidx.lifecycle.ViewModelProvider
import com.dicoding.test.utils.dataStore
import com.dicoding.test.ui.Setting.SettingViewModelFactory
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dicoding.test.worker.DailyReminderWorker
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {

    private lateinit var viewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        
        val switchTheme = view.findViewById<SwitchMaterial>(R.id.switch_theme)
        val switchReminder = view.findViewById<SwitchMaterial>(R.id.switch_reminder)
        
        val pref = SettingPreferences.getInstance(requireActivity().application.dataStore)
        viewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }

        viewModel.getReminderSettings().observe(viewLifecycleOwner) { isReminderActive ->
            switchReminder.isChecked = isReminderActive
            if (isReminderActive) {
                startDailyReminder()
            } else {
                cancelDailyReminder()
            }
        }

        switchReminder.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveReminderSetting(isChecked)
        }
        
        return view
    }

    private fun startDailyReminder() {
        val workManager = WorkManager.getInstance(requireContext())
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        
        val dailyReminderRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "dailyReminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyReminderRequest
        )
    }

    private fun cancelDailyReminder() {
        WorkManager.getInstance(requireContext())
            .cancelUniqueWork("dailyReminder")
    }

    companion object {
        fun newInstance() = SettingFragment()
    }
}