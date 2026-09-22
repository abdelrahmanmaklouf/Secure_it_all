package com.example.secure_it_all.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secure_it_all.R
import com.example.secure_it_all.data.database.AppDatabase
import com.example.secure_it_all.ui.network.NetworkConnectionAdapter
import com.example.secure_it_all.ui.network.NetworkConnectionViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var networkAdapter: NetworkConnectionAdapter

    private val networkViewModel: NetworkConnectionViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.getInstance(requireContext())
                return NetworkConnectionViewModel(db) as T
            }
        }
    }

    private val dashboardViewModel: DashboardViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.getInstance(requireContext())
                return DashboardViewModel(db) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAlerts =
            view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvAlerts)
        rvAlerts.layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<android.widget.TextView>(R.id.tvGreeting)
            .text = "Good morning!"

        val tvAppsWatched = view.findViewById<android.widget.TextView>(R.id.tvAppsWatched)
        val tvConnections = view.findViewById<android.widget.TextView>(R.id.tvConnections)
        val tvThreatsBlocked = view.findViewById<android.widget.TextView>(R.id.tvThreatsBlocked)
        val tvNeedALook = view.findViewById<android.widget.TextView>(R.id.tvNeedALook)

        viewLifecycleOwner.lifecycleScope.launch {
            dashboardViewModel.state.collect { dashState ->
                tvAppsWatched.text = dashState.monitoredAppsCount.toString()
                tvConnections.text = dashState.connectionsToday.toString()
                tvThreatsBlocked.text = dashState.threatsBlocked.toString()
                tvNeedALook.text = dashState.needALookCount.toString()
                rvAlerts.adapter = AppAlertAdapter(dashState.alerts)
            }
        }

        // Network Connections
        val rvConnections =
            view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvConnections)

        networkAdapter = NetworkConnectionAdapter()
        rvConnections.layoutManager = LinearLayoutManager(requireContext())
        rvConnections.adapter = networkAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            networkViewModel.connections.collect { connections ->
                networkAdapter.submitList(connections)
            }
        }
    }
}