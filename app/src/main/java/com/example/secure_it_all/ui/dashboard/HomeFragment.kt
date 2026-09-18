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
import com.example.secure_it_all.model.AlertStatus
import com.example.secure_it_all.model.AppAlert
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Alerts
        val alerts = listOf(
            AppAlert("PhotoBlur Pro", "", AlertStatus.DANGER, "PB"),
            AppAlert("Fast VPN Free", "", AlertStatus.WARNING, "FV"),
            AppAlert("Coupon Saver", "", AlertStatus.WARNING, "CS")
        )

        val rvAlerts =
            view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvAlerts)

        rvAlerts.layoutManager = LinearLayoutManager(requireContext())
        rvAlerts.adapter = AppAlertAdapter(alerts)

        view.findViewById<android.widget.TextView>(R.id.tvGreeting)
            .text = "Good morning, user!"

        // Network Connections
        val rvConnections =
            view.findViewById<androidx.recyclerview.widget.RecyclerView>(
                R.id.rvConnections
            )

        networkAdapter = NetworkConnectionAdapter()

        rvConnections.layoutManager =
            LinearLayoutManager(requireContext())

        rvConnections.adapter = networkAdapter

        // Observe database
        viewLifecycleOwner.lifecycleScope.launch {
            networkViewModel.connections.collect { connections ->
                networkAdapter.submitList(connections)
            }
        }
    }
}