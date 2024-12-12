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
import androidx.fragment.app.viewModels
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.adapter.BukuAdapter
import com.example.perpustakaan.databinding.FragmentTambahdatabukuBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentTambahDataBuku : Fragment() {

    private lateinit var binding: FragmentTambahdatabukuBinding
    private val bukuViewModel: BukuViewModel by viewModels()
    private var imageUri: Uri? = null // Menyimpan URI gambar yang dipilih
//    private lateinit var bukuAdapter: BukuAdapter // Adapter untuk RecyclerView

    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent> // Launcher untuk hasil pemilihan gambar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTambahdatabukuBinding.inflate(inflater, container, false)

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

        syncToFirebase()
        bukuViewModel.syncBuku()

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

    private fun syncToFirebase() {
        val firebaseRef = FirebaseDatabase.getInstance().getReference("buku")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bukuList = mutableListOf<Buku>()

                for (dataSnapshot in snapshot.children) {
                    val buku = dataSnapshot.getValue(Buku::class.java)
                    if (buku != null) {
                        bukuList.add(buku)
                    }
                }

                // Perbarui adapter dengan daftar buku

                bukuViewModel.syncLocalDatabase(bukuList)
                bukuViewModel.syncUnsyncedData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal mengambil data dari Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk menyimpan buku ke dalam database
    private fun simpanBuku() {
        val judul = binding.etJudulBuku.text.toString().trim()
        val penulis = binding.etPenulis.text.toString().trim()
        val tahunTerbit = binding.etTahunTerbit.text.toString().trim()
        val stok = binding.etStok.text.toString().trim()
        val deskripsi = binding.etDeskripsi.text.toString().trim()
        val imageUrl = imageUri?.toString() // Mengambil URI gambar yang dipilih

        if (judul.isEmpty()) {
            binding.etJudulBuku.error = "Judul tidak boleh kosong"
            return
        }

        if (penulis.isEmpty()) {
            binding.etPenulis.error = "Penulis tidak boleh kosong"
            return
        }

        if (tahunTerbit.isEmpty() || !tahunTerbit.matches(Regex("\\d{4}"))) {
            binding.etTahunTerbit.error = "Tahun terbit harus berupa angka valid"
            return
        }

        if (stok.isEmpty() || !stok.matches(Regex("\\d+"))) {
            binding.etStok.error = "Stok harus berupa angka"
            return
        }

        if (deskripsi.isEmpty()) {
            binding.etDeskripsi.error = "Deskripsi tidak boleh kosong"
            return
        }
        if (imageUri == null) {
            Toast.makeText(requireContext(), "Pilih gambar buku", Toast.LENGTH_SHORT).show()
            return
        }

        val buku = Buku(
            id = 0,
            judul = judul,
            penulis = penulis,
            tahunTerbit = tahunTerbit.toInt(),
            stok = stok.toInt(),
            deskripsi = deskripsi,
            gambarUrl = null // Gambar bersifat opsional
        )

        bukuViewModel.insert(buku, imageUri) // Jika imageUri null, proses tetap berlanjut
    }

    // Fungsi untuk membuka galeri gambar
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageLauncher.launch(intent) // Meluncurkan intent untuk memilih gambar
    }

    // Fungsi untuk memvalidasi apakah gambar yang dipilih valid
    private fun isValidImage(uri: Uri): Boolean {
        val contentResolver = requireContext().contentResolver
        val mimeType = contentResolver.getType(uri)
        return mimeType == "image/jpeg" || mimeType == "image/png" || mimeType == "image/jpg"
    }
}
