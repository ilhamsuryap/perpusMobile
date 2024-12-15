package com.example.perpustakaan.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R
import com.example.perpustakaan.detailbuku.DetailActivity

// BukuAdapter
class BukuAdapter(
    private val onClick: (Buku) -> Unit) : ListAdapter<Buku, BukuAdapter.BukuViewHolder>(BukuDiffCallback()) {

    private var selectedBuku: Buku? = null


    fun setData(newList: List<Buku>) {
        submitList(newList)
    }


    fun SelectedBuku(): Buku? {
        return selectedBuku
    }

    // ViewHolder untuk buku
    class BukuViewHolder(itemView: View, private val onClick: (Buku) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvJudulBuku: TextView = itemView.findViewById(R.id.tvJudulBuku)
        private val tvPenulis: TextView = itemView.findViewById(R.id.tvPenulis)
        private val tvStok: TextView = itemView.findViewById(R.id.tvStok)
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewBuku)

        fun bind(buku: Buku) {
            tvJudulBuku.text = buku.judul
            tvPenulis.text = buku.penulis
            tvStok.text = "Stok: ${buku.stok}"

            // Memuat gambar menggunakan Glide
            buku.gambarUrl?.let {
                Glide.with(itemView.context)
                    .load(buku.gambarUrl)
                    .into(imageView)
            }

            // Arahkan ke DetailActivity saat diklik
            itemView.setOnClickListener {
                // Mengirim Intent ke DetailActivity dengan data buku
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("id", buku.id)  // Mengirimkan ID buku
                    putExtra("judul", buku.judul)  // Mengirimkan judul buku
                    putExtra("penulis", buku.penulis)  // Mengirimkan penulis buku
                    putExtra("gambarUrl", buku.gambarUrl)  // Mengirimkan URL gambar buku
                    putExtra("deskripsi", buku.deskripsi)  // Mengirimkan deskripsi buku
                    putExtra("tahunTerbit", buku.tahunTerbit)  // Mengirimkan tahun terbit buku
                    putExtra("stok", buku.stok)  // Mengirimkan stok buku
                }
                itemView.context.startActivity(intent)  // Mulai activity DetailActivity
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.style_daftar_buku, parent, false)
        return BukuViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val buku = getItem(position)
        holder.bind(buku)
    }
}

class BukuDiffCallback : DiffUtil.ItemCallback<Buku>() {
    override fun areItemsTheSame(oldItem: Buku, newItem: Buku): Boolean {
        return oldItem.id == newItem.id // Pastikan `id` di Buku tidak null atau duplikat
    }

    override fun areContentsTheSame(oldItem: Buku, newItem: Buku): Boolean {
        return oldItem == newItem // Pastikan buku memiliki implementasi `equals` yang benar
    }
}
