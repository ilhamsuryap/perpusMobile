package com.example.perpustakaan.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.R
import com.example.perpustakaan.editdatapinjam
import com.example.perpustakaan.entity.Pinjam

class PinjamAdapter : RecyclerView.Adapter<PinjamAdapter.PinjamViewHolder>() {

    private var pinjamList = emptyList<Pinjam>()

    class PinjamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaAnggota: TextView = itemView.findViewById(R.id.tvNamaAnggota)
        val tvJudulBukuPinjam: TextView = itemView.findViewById(R.id.tvJudulBukuPinjam)
        val tvTanggalPinjam: TextView = itemView.findViewById(R.id.tvTanggalPinjam)
        val tvTanggalKembali: TextView = itemView.findViewById(R.id.tvTanggalKembali)

        // Bind fungsi untuk mengikat data dan menambahkan click listener
        fun bind(pinjam: Pinjam) {
            tvNamaAnggota.text = pinjam.namaanggota
            tvJudulBukuPinjam.text = pinjam.judulbuku_pinjam
            tvTanggalPinjam.text = pinjam.tanggalpinjam
            tvTanggalKembali.text = pinjam.tanggalkembali

            // Menambahkan klik listener pada itemView (CardView atau layout di dalamnya)
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, editdatapinjam::class.java)

                // Kirim data yang diperlukan ke EditDataPinjam (gunakan key yang benar)
                intent.putExtra("id_pinjam", pinjam.id_pinjam) // ID Pinjam yang diperlukan untuk update
                intent.putExtra("namaanggota", pinjam.namaanggota)
                intent.putExtra("judulbuku_pinjam", pinjam.judulbuku_pinjam)
                intent.putExtra("tanggalpinjam", pinjam.tanggalpinjam)
                intent.putExtra("tanggalkembali", pinjam.tanggalkembali)

                context.startActivity(intent) // Buka aktivitas editdatapinjam
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinjamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.style_pinjam_buku, parent, false)
        return PinjamViewHolder(view)
    }

    override fun onBindViewHolder(holder: PinjamViewHolder, position: Int) {
        val currentItem = pinjamList[position]
        holder.bind(currentItem) // Bind data dan tambahkan klik listener
    }

    override fun getItemCount(): Int = pinjamList.size

    fun setData(pinjam: List<Pinjam>) {
        this.pinjamList = pinjam
        notifyDataSetChanged() // Memberitahu adapter untuk memperbarui tampilan
    }
}
