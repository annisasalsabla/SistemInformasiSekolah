package com.annisa.sekolah

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.annisa.sisteminformasisekolah.DetailSekolahActivity
import com.annisa.sisteminformasisekolah.R
import com.annisa.sekolah.models.SekolahResponse
import com.squareup.picasso.Picasso

class SekolahAdapter(
    private val dataSekolah: ArrayList<SekolahResponse.ListItems>
) : RecyclerView.Adapter<SekolahAdapter.ViewHolder>() { // Change from SekolahResponse.ViewHolder to SekolahAdapter.ViewHolder

    // Define ViewHolder class inside the adapter
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Initialize views
        val imgSekolah: ImageView = view.findViewById(R.id.imgSekolah)
        val tvNamaSekolah: TextView = view.findViewById(R.id.tvNamaSekolah)
        val tvNoTelepon: TextView = view.findViewById(R.id.tvNoTelepon)
        val tvAkreditasi: TextView = view.findViewById(R.id.tvAkreditasi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_sekolah, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSekolah.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sekolah = dataSekolah[position]

        // Set data to views
        Picasso.get().load(sekolah.gambar).into(holder.imgSekolah)
        holder.tvNamaSekolah.text = sekolah.nama_sekolah
        holder.tvNoTelepon.text = "No. Telepon: ${sekolah.no_telepon}"
        holder.tvAkreditasi.text = "Akreditasi: ${sekolah.akreditasi}"

        // Handle item click to open detail view
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailSekolahActivity::class.java).apply {
                putExtra("gambar", sekolah.gambar)
                putExtra("nama_sekolah", sekolah.nama_sekolah)
                putExtra("no_telepon", sekolah.no_telepon)
                putExtra("akreditasi", sekolah.akreditasi)
                putExtra("informasi", sekolah.informasi)
            }
            holder.imgSekolah.context.startActivity(intent)
        }
    }

    // Set data in adapter
    fun setData(data: List<SekolahResponse.ListItems>) {
        dataSekolah.clear()
        dataSekolah.addAll(data)
        notifyDataSetChanged()
    }
}
