package com.example.secure_it_all.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.secure_it_all.R
import com.example.secure_it_all.model.AppAlert
import com.example.secure_it_all.model.bgColorRes
import com.example.secure_it_all.model.labelText
import com.example.secure_it_all.model.textColorRes

// ViewHolder just "holds" references to the views in one row, so we don't
// call findViewById() every time the row scrolls into view.
class AppAlertViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val initials: TextView = view.findViewById(R.id.tvInitials)
    val name: TextView = view.findViewById(R.id.tvName)
    val description: TextView = view.findViewById(R.id.tvDescription)
    val status: TextView = view.findViewById(R.id.tvStatus)
}

// T given a list of AppAlert, create rows w 2emlahom
class AppAlertAdapter(private val items: List<AppAlert>) :
    RecyclerView.Adapter<AppAlertViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppAlertViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_alert, parent, false)
        return AppAlertViewHolder(view)
    }


    override fun onBindViewHolder(holder: AppAlertViewHolder, position: Int) {
        val item = items[position]
        holder.initials.text = item.iconInitials
        holder.name.text = item.name
        holder.description.text = item.description
        holder.status.text = item.status.labelText()
        holder.status.setBackgroundColor(
            holder.itemView.context.getColor(item.status.bgColorRes())
        )
        holder.status.setTextColor(
            holder.itemView.context.getColor(item.status.textColorRes())
        )
    }


    override fun getItemCount() = items.size
}