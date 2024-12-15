package com.example.perpustakaan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.kasircafeapp.data.network.NetworkHelper
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.ViewModel.PeminjamanViewModel
import com.example.perpustakaan.databinding.FragmentDikembalikanBinding
import com.google.firebase.database.*

class FragmentDikembalikan : Fragment() {

    private var _binding: FragmentDikembalikanBinding? = null
    private val binding get() = _binding!!

    private val bukuViewModel: BukuViewModel by viewModels()
    private val pinjamViewModel: PeminjamanViewModel by viewModels()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val networkHelper by lazy { NetworkHelper(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDikembalikanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val judulBuku = requireArguments().getString("judul_buku")
        val tanggalPinjam = requireArguments().getString("tanggal_pinjam")
        val tanggalKembali = requireArguments().getString("tanggal_kembali")
        val idPinjam = requireArguments().getInt("id_pinjam") // ID pinjaman
        val idBuku = requireArguments().getInt("id_buku") // ID buku

        // Menampilkan data di TextView melalui binding
        binding.tvJudulBuku.text = judulBuku
        binding.tvTanggalpinjam.text = tanggalPinjam
        binding.tvTanggalkembali.text = tanggalKembali

        // Tombol Kembalikan Buku ditekan
        binding.btnKembalikan.setOnClickListener {
            Log.d("FragmentDikembalikan", "Button Kembalikan ditekan")

            // Validasi data
            if (idBuku != -1 && idPinjam != -1) {
                // Proses update stok dan hapus data pinjam
                updateStokDanHapusPinjam(idBuku, idPinjam)
            } else {
                context?.let {
                    Toast.makeText(it, "ID buku atau ID pinjam tidak valid", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateStokDanHapusPinjam(idBuku: Int, idPinjam: Int) {
        // Periksa koneksi internet
        if (networkHelper.isNetworkConnected()) {
            // Ambil judul buku dari intent atau arguments
            val judulBuku = binding.tvJudulBuku.text.toString()

            // Cari referensi buku di Firebase berdasarkan judul buku
            val firebaseRef = firebaseDatabase.getReference("buku")
            firebaseRef.orderByChild("judul").equalTo(judulBuku).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Iterasi melalui hasil pencarian (seharusnya hanya satu buku)
                        for (childSnapshot in snapshot.children) {
                            val bukuRef = childSnapshot.ref

                            // Ambil stok saat ini
                            val stokLama = childSnapshot.child("stok").getValue(Int::class.java) ?: 0
                            val stokBaru = stokLama + 1

                            // Update stok buku
                            bukuRef.child("stok").setValue(stokBaru).addOnSuccessListener {
                                Log.d("FragmentDikembalikan", "Stok buku berhasil diperbarui di Firebase")

                                // Setelah berhasil update stok di Firebase, sinkronkan Room
                                syncRoomWithFirebase()

                                // Hapus data pinjam di Firebase dan Room
                                deletePinjamData(idPinjam)
                            }.addOnFailureListener {
                                Log.e("FragmentDikembalikan", "Gagal memperbarui stok di Firebase")
                                context?.let {
                                    Toast.makeText(it, "Gagal memperbarui stok di Firebase", Toast.LENGTH_SHORT).show()
                                }
                            }

                            // Keluar dari loop setelah menemukan buku
                            break
                        }
                    } else {
                        Log.e("FragmentDikembalikan", "Buku tidak ditemukan")
                        context?.let {
                            Toast.makeText(it, "Buku tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FragmentDikembalikan", "Gagal mencari buku di Firebase: ${error.message}")
                    context?.let {
                        Toast.makeText(it, "Gagal mencari buku", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            context?.let {
                Toast.makeText(it, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Sinkronkan data Room dengan Firebase (tanpa mengubah stok di Room)
    private fun syncRoomWithFirebase() {
        bukuViewModel.getAllBook().observe(viewLifecycleOwner, Observer { bukuList ->
            // Ambil data terbaru dari Firebase dan sinkronkan dengan Room
            bukuList.forEach { buku ->
                val firebaseRef = firebaseDatabase.getReference("buku").child(buku.id.toString())
                firebaseRef.get().addOnSuccessListener { snapshot ->
                    val stokFirebase = snapshot.child("stok").getValue(Int::class.java) ?: buku.stok
                    // Sinkronkan stok Room dengan Firebase
                    buku.stok = stokFirebase
                    bukuViewModel.updateRoom(buku) // Update Room dengan stok terbaru dari Firebase
                }
            }
        })
    }

    // Fungsi untuk menghapus data pinjam di Firebase dan Room
    private fun deletePinjamData(idPinjam: Int) {
        val pinjamRef = firebaseDatabase.getReference("pinjam").child(idPinjam.toString())
        pinjamRef.removeValue().addOnSuccessListener {
            // Menghapus data pinjam di Room
            pinjamViewModel.deletePinjam(idPinjam)
            Log.d("FragmentDikembalikan", "Data pinjam berhasil dihapus")
            context?.let {
                Toast.makeText(it, "Stok buku berhasil diperbarui dan data pinjam dihapus!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Log.e("FragmentDikembalikan", "Gagal menghapus data pinjam di Firebase")
            context?.let {
                Toast.makeText(it, "Gagal menghapus data pinjam di Firebase", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
