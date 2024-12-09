package com.example.perpustakaan.daftarbukuActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.databinding.FragmentTambahdatabukuBinding

class FragmentTambahDataBuku : Fragment() {

    private lateinit var binding: FragmentTambahdatabukuBinding
    private lateinit var bukuViewModel: BukuViewModel
    private var imageUri: Uri? = null // Menyimpan URI gambar yang dipilih

    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent> // Launcher untuk hasil pemilihan gambar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTambahdatabukuBinding.inflate(inflater, container, false)

        bukuViewModel = ViewModelProvider(this).get(BukuViewModel::class.java) // Inisialisasi ViewModel Buku

        // Launcher untuk memilih gambar
        selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) { // Jika pemilihan gambar berhasil
                val data = result.data
                imageUri = data?.data // Menyimpan URI gambar yang dipilih

                if (imageUri != null && isValidImage(imageUri!!)) { // Validasi gambar yang dipilih
                    binding.ivBuku.setImageURI(imageUri) // Menampilkan gambar yang dipilih pada ImageView
                } else {
                    Toast.makeText(requireContext(), "Pilih gambar yang valid", Toast.LENGTH_SHORT).show() // Pesan jika gambar tidak valid
                }
            }
        }

        // Tombol pilih gambar
        binding.btnSelectImage.setOnClickListener {
            openGallery() // Memanggil fungsi untuk membuka galeri gambar
        }

        // Tombol simpan buku
        binding.btnSimpanBuku.setOnClickListener {
            simpanBuku() // Memanggil fungsi untuk menyimpan data buku
        }

        // Observer untuk status upload buku
        bukuViewModel.uploadStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) { // Jika buku berhasil disimpan
                Toast.makeText(requireContext(), "Buku berhasil disimpan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Gagal menyimpan buku", Toast.LENGTH_SHORT).show() // Jika terjadi kesalahan
            }
        }

        return binding.root // Mengembalikan root view
    }

    // Fungsi untuk menyimpan buku ke dalam database
    private fun simpanBuku() {
        val judul = binding.etJudulBuku.text.toString()
        val penulis = binding.etPenulis.text.toString()
        val tahunTerbit = binding.etTahunTerbit.text.toString()
        val stok = binding.etStok.text.toString()
        val deskripsi = binding.etDeskripsi.text.toString()
        val imageUrl = imageUri?.toString() // Menyimpan URL gambar yang dipilih

        // Validasi input untuk memastikan tidak ada yang kosong
        if (judul.isEmpty() || penulis.isEmpty() || tahunTerbit.isEmpty() || stok.isEmpty() || deskripsi.isEmpty() || imageUri == null) {
            Toast.makeText(requireContext(), "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
            return // Menghentikan eksekusi jika ada data yang kosong
        }

        try {
            val tahunTerbitInt = tahunTerbit.toInt() // Mengubah tahun terbit menjadi integer
            val stokInt = stok.toInt() // Mengubah stok menjadi integer

            val buku = Buku(
                judul = judul,
                penulis = penulis,
                tahunTerbit = tahunTerbitInt,
                stok = stokInt,
                deskripsi = deskripsi,
                gambarUrl = imageUrl
            )

            // Menyimpan buku menggunakan ViewModel
            bukuViewModel.insert(buku, imageUri)
        } catch (e: NumberFormatException) {
            // Jika terjadi kesalahan dalam konversi angka
            Toast.makeText(requireContext(), "Tahun terbit dan stok harus berupa angka", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk membuka galeri gambar
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageLauncher.launch(intent) // Meluncurkan intent untuk memilih gambar
    }

    // Fungsi untuk memvalidasi apakah gambar yang dipilih valid
    private fun isValidImage(uri: Uri): Boolean {
        val mimeType = requireContext().contentResolver.getType(uri)
        return mimeType != null && (mimeType.contains("image/jpeg") ||
                mimeType.contains("image/png") ||
                mimeType.contains("image/jpg")) // Memastikan file adalah gambar
    }
}
