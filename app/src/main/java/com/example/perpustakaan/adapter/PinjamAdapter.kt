package com.example.perpustakaan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.R
import com.example.perpustakaan.entity.Pinjam

class PinjamAdapter : RecyclerView.Adapter<PinjamAdapter.PinjamViewHolder>() {
    private var pinjamList = emptyList<Pinjam>()

    inner class PinjamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaAnggota: TextView = itemView.findViewById(R.id.tvNamaAnggota)
        val tvJudulBukuPinjam: TextView = itemView.findViewById(R.id.tvJudulBukuPinjam)
        val tvTanggalPinjam: TextView = itemView.findViewById(R.id.tvTanggalPinjam)
        val tvTanggalKembali: TextView = itemView.findViewById(R.id.tvTanggalKembali)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinjamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.style_pinjam_buku, parent, false)
        return PinjamViewHolder(view)
    }

    override fun onBindViewHolder(holder: PinjamViewHolder, position: Int) {
        val currentItem = pinjamList[position]
        holder.tvNamaAnggota.text = currentItem.namaanggota
        holder.tvJudulBukuPinjam.text = currentItem.judulbuku_pinjam
        holder.tvTanggalPinjam.text = currentItem.tanggalpinjam
        holder.tvTanggalKembali.text = currentItem.tanggalkembali
    }

    override fun getItemCount(): Int = pinjamList.size

    fun setData(pinjam: List<Pinjam>) {
        this.pinjamList = pinjam
        notifyDataSetChanged()
    }
}
