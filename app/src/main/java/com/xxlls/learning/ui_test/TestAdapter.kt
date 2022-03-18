package com.xxlls.learning.ui_test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xxlls.learning.R

/**
 * @Date 3/7/22
 * @Aurth xuzengzeng
 * @Desceiption
 */
class TestAdapter(private val items: ArrayList<String>) :
    RecyclerView.Adapter<TestAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_single_start_center_text, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.centerText.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }


    companion object
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var centerText: TextView = itemView.findViewById(R.id.item_center_text)
    }

}