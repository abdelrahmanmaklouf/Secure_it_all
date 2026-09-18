package com.example.secure_it_all.ui.network


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.secure_it_all.R
import com.example.secure_it_all.data.database.NetworkConnectionEntity

class NetworkConnectionAdapter :
    RecyclerView.Adapter<NetworkConnectionAdapter.ConnectionViewHolder>() {

    private var connections = emptyList<NetworkConnectionEntity>()

    fun submitList(newList: List<NetworkConnectionEntity>) {
        connections = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConnectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_network_connection, parent, false)

        return ConnectionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ConnectionViewHolder,
        position: Int
    ) {
        holder.bind(connections[position])
    }

    override fun getItemCount(): Int = connections.size

    class ConnectionViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvDomain =
            itemView.findViewById<TextView>(R.id.tvDomain)

        private val tvConnection =
            itemView.findViewById<TextView>(R.id.tvConnection)

        private val tvProtocol =
            itemView.findViewById<TextView>(R.id.tvProtocol)

        private val tvBytes =
            itemView.findViewById<TextView>(R.id.tvBytes)

        fun bind(connection: NetworkConnectionEntity) {

            tvDomain.text =
                connection.domain ?: connection.destinationIp

            tvConnection.text =
                "${connection.destinationIp}:${connection.destinationPort}"

            tvProtocol.text =
                connection.protocol

            tvBytes.text =
                "↑ ${connection.bytesSent} B   ↓ ${connection.bytesReceived} B"
        }
    }
}