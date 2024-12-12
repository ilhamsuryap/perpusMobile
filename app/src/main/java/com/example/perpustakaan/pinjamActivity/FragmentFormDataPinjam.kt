package com.example.perpustakaan.pinjamActivity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.ViewModel.PeminjamanViewModel
import com.example.perpustakaan.databinding.FragmentFormDataPinjamBinding
import com.example.perpustakaan.entity.Pinjam
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentFormDataPinjam : Fragment() {
    private var _binding: FragmentFormDataPinjamBinding? = null
    private val binding get() = _binding!!

    private lateinit var pinjamViewModel: PeminjamanViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormDataPinjamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pinjamViewModel = ViewModelProvider(this).get(PeminjamanViewModel::class.java)

        setupDatePickers()
        setupSaveButton()
        syncToFirebase()
    }

    private fun setupDatePickers() {
        binding.etTglPinjam.setOnClickListener {
            showDatePicker { date ->
                binding.etTglPinjam.setText(date)
            }
        }

        binding.etTglKembali.setOnClickListener {
            showDatePicker { date ->
                binding.etTglKembali.setText(date)
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSimpan.setOnClickListener {
            val namaAnggota = binding.etNamaAnggota.text.toString().trim()
            val judulBuku = binding.etJudulBuku.text.toString().trim()
            val tanggalPinjam = binding.etTglPinjam.text.toString()
            val tanggalKembali = binding.etTglKembali.text.toString()

            if (validateInput(namaAnggota, judulBuku, tanggalPinjam, tanggalKembali)) {
                savePinjamData(namaAnggota, judulBuku, tanggalPinjam, tanggalKembali)
            }
        }
    }

    private fun validateInput(
        namaAnggota: String,
        judulBuku: String,
        tanggalPinjam: String,
        tanggalKembali: String
    ): Boolean {
        return when {
            namaAnggota.isEmpty() -> {
                Toast.makeText(requireContext(), "Nama Anggota harus diisi", Toast.LENGTH_SHORT).show()
                false
            }
            judulBuku.isEmpty() -> {
                Toast.makeText(requireContext(), "Judul Buku harus diisi", Toast.LENGTH_SHORT).show()
                false
            }
            tanggalPinjam.isEmpty() -> {
                Toast.makeText(requireContext(), "Tanggal Pinjam harus dipilih", Toast.LENGTH_SHORT).show()
                false
            }
            tanggalKembali.isEmpty() -> {
                Toast.makeText(requireContext(), "Tanggal Kembali harus dipilih", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun savePinjamData(
        namaAnggota: String,
        judulBuku: String,
        tanggalPinjam: String,
        tanggalKembali: String
    ) {
        try {
            val pinjam = Pinjam(
                namaanggota = namaAnggota,
                judulbuku_pinjam = judulBuku,
                tanggalpinjam = tanggalPinjam,
                tanggalkembali = tanggalKembali
            )

            pinjamViewModel.insert(pinjam)
            Toast.makeText(requireContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show()

            startActivity(Intent(requireContext(), PinjamBukuActivity::class.java))
            requireActivity().finish() // Optional: close current fragment/activity
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Terjadi kesalahan saat menyimpan data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun syncToFirebase() {
        val firebaseRef = FirebaseDatabase.getInstance().getReference("pinjam")
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pinjamList = snapshot.children
                    .mapNotNull { it.getValue(Pinjam::class.java) }
                    .toMutableList()

                pinjamViewModel.syncLocalDatabase(pinjamList)
                pinjamViewModel.syncUnsyncedData()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal mengambil data dari Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.time

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}