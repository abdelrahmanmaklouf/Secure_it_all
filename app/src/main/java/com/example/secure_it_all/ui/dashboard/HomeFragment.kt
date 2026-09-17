package com.example.secure_it_all.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.secure_it_all.R
import com.example.secure_it_all.model.AlertStatus
import com.example.secure_it_all.model.AppAlert

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alerts = listOf(
            AppAlert("PhotoBlur Pro", "", AlertStatus.DANGER, "PB"),
            AppAlert("Fast VPN Free", "", AlertStatus.WARNING, "FV"),
            AppAlert("Coupon Saver", "", AlertStatus.WARNING, "CS")
        )

        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rvAlerts)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = AppAlertAdapter(alerts)

        view.findViewById<android.widget.TextView>(R.id.tvGreeting).text = "Good morning, user!"
    }
}