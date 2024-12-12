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
    private val onClick: (Buku) -> Unit) : ListAdapter<Buku, RecyclerView.ViewHolder>(BukuDiffCallback()) {
    private var selectedBuku: Buku? = null
    enum class ITEM_VIEW_TYPE { PRODUCT, ADS }

    override fun getItemViewType(position: Int): Int {
        val buku = getItem(position)
        return if (buku.stok == 0)
            ITEM_VIEW_TYPE.ADS.ordinal
        else
            ITEM_VIEW_TYPE.PRODUCT.ordinal
    }

    fun setData(newList: List<Buku>) {
        submitList(newList)
    }
    fun SelectedBuku(): Buku? {
        return selectedBuku
    }


    // ViewHolder untuk buku dengan stok tersedia
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
                // Mengirim Intent ke DetailActivity dengan ID Buku
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("BUKU_ID", buku.id)  // Mengirimkan ID buku
                    putExtra("BUKU_JUDUL", buku.judul)  // Mengirimkan judul buku
                    putExtra("BUKU_PENULIS", buku.penulis)  // Mengirimkan penulis buku
                    putExtra("BUKU_GAMBAR_URL", buku.gambarUrl)  // Mengirimkan URL gambar buku
                    putExtra("BUKU_DESKRIPSI", buku.deskripsi)  // Mengirimkan deskripsi buku
                    putExtra("BUKU_TAHUN_TERBIT", buku.tahunTerbit)  // Mengirimkan tahun terbit buku
                    putExtra("BUKU_STOK", buku.stok)  // Mengirimkan stok buku

                    

                }
                itemView.context.startActivity(intent)  // Mulai activity DetailActivity
            }
        }
    }

    // ViewHolder untuk buku dengan stok habis
    class BukuADSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvJudulBuku: TextView = itemView.findViewById(R.id.tvJudulBukuAds)
        private val tvPenulis: TextView = itemView.findViewById(R.id.tvPenulisAds)
        private val ivSoldOut: ImageView = itemView.findViewById(R.id.tvStokSold) // Pastikan ini adalah ImageView
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewBukuADS)

        fun bind(buku: Buku) {
            tvJudulBuku.text = buku.judul
            tvPenulis.text = buku.penulis

            ivSoldOut.visibility = View.VISIBLE // Tampilkan "sold out" jika stok habis

            buku.gambarUrl?.let {
                Glide.with(itemView.context)
                    .load(buku.gambarUrl)
                    .into(imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE.PRODUCT.ordinal) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.style_daftar_buku, parent, false)
            BukuViewHolder(view, onClick)
        } else {
            val viewAds = LayoutInflater.from(parent.context)
                .inflate(R.layout.style_daftar_bukuads, parent, false)
            BukuADSViewHolder(viewAds)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val buku = getItem(position)
        if (holder is BukuViewHolder) {
            holder.bind(buku)
        } else if (holder is BukuADSViewHolder) {
            holder.bind(buku)
        }
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
