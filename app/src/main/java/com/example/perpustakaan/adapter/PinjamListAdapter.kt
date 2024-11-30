package com.example.perpustakaan.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.databinding.StylePinjamBukuBinding
import com.example.perpustakaan.EditDataPinjamActivity
import com.example.perpustakaan.entity.Pinjam

class PinjamListAdapter(
    private val onItemClick: (Pinjam) -> Unit
) : ListAdapter<Pinjam, PinjamListAdapter.PinjamViewHolder>(PinjamDiffCallback()) {

    class PinjamViewHolder private constructor(val binding: StylePinjamBukuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pinjam: Pinjam, onItemClick: (Pinjam) -> Unit) {
            binding.tvNamaAnggota.text = pinjam.namaUser
            binding.tvJudulBukuPinjam.text = pinjam.judulBuku
            binding.tvTanggalPinjam.text = pinjam.tanggalPinjam
            binding.tvTanggalKembali.text = pinjam.tanggalKembali

            itemView.setOnClickListener {
                onItemClick(pinjam)

                // Ambil context dari itemView
                val context = itemView.context
                val intent = Intent(context, EditDataPinjamActivity::class.java)
                intent.putExtra("id_pinjam", pinjam.id)
                intent.putExtra("namaanggota", pinjam.namaUser)
                intent.putExtra("judulbuku_pinjam", pinjam.judulBuku)
                intent.putExtra("tanggalpinjam", pinjam.tanggalPinjam)
                intent.putExtra("tanggalkembali", pinjam.tanggalKembali)
                context.startActivity(intent) // Buka aktivitas editdatapinjam
            }
        }

        companion object {
            fun from(parent: ViewGroup): PinjamViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StylePinjamBukuBinding.inflate(layoutInflater, parent, false)
                return PinjamViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinjamViewHolder {
        return PinjamViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PinjamViewHolder, position: Int) {
        val pinjam = getItem(position)
        holder.bind(pinjam, onItemClick)
    }

    class PinjamDiffCallback : DiffUtil.ItemCallback<Pinjam>() {
        override fun areItemsTheSame(oldItem: Pinjam, newItem: Pinjam): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pinjam, newItem: Pinjam): Boolean {
            return oldItem == newItem
        }
    }
}
