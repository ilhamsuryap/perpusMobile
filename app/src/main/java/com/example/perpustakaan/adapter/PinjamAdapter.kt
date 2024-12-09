package com.example.perpustakaan.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.EditDataPinjamActivity
import com.example.perpustakaan.R
import com.example.perpustakaan.entity.Pinjam

class PinjamAdapter(private val onItemClick: (Pinjam) -> Unit) : RecyclerView.Adapter<PinjamAdapter.PinjamViewHolder>() {

    private val pinjamList = mutableListOf<Pinjam>()

    class PinjamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaAnggota: TextView = itemView.findViewById(R.id.tvNamaAnggota)
        val tvJudulBukuPinjam: TextView = itemView.findViewById(R.id.tvJudulBukuPinjam)
        val tvTanggalPinjam: TextView = itemView.findViewById(R.id.tvTanggalPinjam)
        val tvTanggalKembali: TextView = itemView.findViewById(R.id.tvTanggalKembali)

        fun bind(pinjam: Pinjam) {
            tvNamaAnggota.text = pinjam.namaanggota
            tvJudulBukuPinjam.text = pinjam.judulbuku_pinjam
            tvTanggalPinjam.text = pinjam.tanggalpinjam
            tvTanggalKembali.text = pinjam.tanggalkembali

            // Tambahkan klik listener untuk membuka aktivitas EditDataPinjamActivity
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, EditDataPinjamActivity::class.java)
                intent.putExtra("id_pinjam", pinjam.id_pinjam)
                intent.putExtra("namaanggota", pinjam.namaanggota)
                intent.putExtra("judulbuku_pinjam", pinjam.judulbuku_pinjam)
                intent.putExtra("tanggalpinjam", pinjam.tanggalpinjam)
                intent.putExtra("tanggalkembali", pinjam.tanggalkembali)
                context.startActivity(intent)
            }
        }
    }

    fun setData(data: List<Pinjam>) {
        pinjamList.clear()
        pinjamList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinjamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.style_pinjam_buku, parent, false)
        return PinjamViewHolder(view)
    }

    override fun onBindViewHolder(holder: PinjamViewHolder, position: Int) {
        val pinjam = pinjamList[position]
        holder.bind(pinjam)
        holder.itemView.setOnClickListener {
            onItemClick(pinjam) // Callback saat item diklik
        }
    }

    override fun getItemCount(): Int = pinjamList.size
}
