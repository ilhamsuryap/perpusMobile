//package com.example.perpustakaan
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.perpustakaan.R
//import com.example.perpustakaan.adapter.PinjamAdapter
//import com.example.perpustakaan.entity.Pinjam
//
//class AdapterPengembalian(
//    private val pinjamList: List<Pinjam>,
//    private val onTerimaClick: (Pinjam) -> Unit
//) : RecyclerView.Adapter<PinjamAdapter.PinjamViewHolder>() {
//
//    inner class PinjamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvJudulBuku: TextView = itemView.findViewById(R.id.tvJudulBuku)
//        val btnTerima: Button = itemView.findViewById(R.id.btnTerima)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinjamViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.style_pinjam_buku_admin, parent, false)
//        return PinjamViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: PinjamViewHolder, position: Int) {
//        val pinjam = pinjamList[position]
//        holder.tvJudulBuku.text = pinjam.judulbuku_pinjam
//        holder.btnTerima.setOnClickListener {
//            onTerimaClick(pinjam)
//        }
//    }
//
//    override fun getItemCount(): Int = pinjamList.size
//}