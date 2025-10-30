package com.postest.fhyrapost4

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.postest.fhyrapost4.R // Ganti com.example.yourapp
import com.postest.fhyrapost4.databinding.ActivityMainBinding // Ganti com.example.yourapp

class MainActivity : AppCompatActivity() {

    // Setup ViewBinding
    private lateinit var binding: ActivityMainBinding

    // Inisialisasi ViewModel
    private val wargaViewModel: WargaViewModel by viewModels()

    // Inisialisasi Adapter untuk RecyclerView
    private val adapter = WargaListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Dropdown Status Pernikahan
        setupDropdown()

        // Setup RecyclerView
        setupRecyclerView()

        // Observe (pantau) perubahan data di database
        observeData()

        // Setup listener untuk tombol
        binding.btnSimpan.setOnClickListener {
            simpanData()
        }

        binding.btnReset.setOnClickListener {
            resetData()
        }
    }

    private fun setupDropdown() {
        val statusArray = resources.getStringArray(R.array.status_pernikahan_array)
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusArray)
        binding.dropdownStatus.setAdapter(arrayAdapter)
    }

    private fun setupRecyclerView() {
        binding.rvWarga.adapter = adapter
        binding.rvWarga.layoutManager = LinearLayoutManager(this)
    }

    private fun observeData() {
        // ViewModel akan memberi tahu kita jika ada data baru
        wargaViewModel.allWarga.observe(this, Observer { wargaList ->
            // Kirim list baru ke adapter
            wargaList?.let {
                adapter.submitList(it)

                // Tampilkan pesan "Belum ada data" jika list kosong
                if (it.isEmpty()) {
                    binding.tvEmptyState.visibility = View.VISIBLE
                    binding.rvWarga.visibility = View.GONE
                } else {
                    binding.tvEmptyState.visibility = View.GONE
                    binding.rvWarga.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun simpanData() {
        // Ambil semua data dari form
        val nama = binding.etNama.text.toString().trim()
        val nik = binding.etNik.text.toString().trim()
        val kabupaten = binding.etKabupaten.text.toString().trim()
        val kecamatan = binding.etKecamatan.text.toString().trim()
        val desa = binding.etDesa.text.toString().trim()
        val rt = binding.etRt.text.toString().trim()
        val rw = binding.etRw.text.toString().trim()
        val status = binding.dropdownStatus.text.toString().trim()

        val selectedJenisKelaminId = binding.rgJenisKelamin.checkedRadioButtonId
        val jenisKelamin = if (selectedJenisKelaminId != -1) {
            findViewById<RadioButton>(selectedJenisKelaminId).text.toString()
        } else {
            "" // Kosong jika belum dipilih
        }

        // === VALIDASI ===
        if (nama.isEmpty() || nik.isEmpty() || kabupaten.isEmpty() || kecamatan.isEmpty() ||
            desa.isEmpty() || rt.isEmpty() || rw.isEmpty() || status.isEmpty() || jenisKelamin.isEmpty()
        ) {

            // Tampilkan Alert jika ada data yang kosong
            AlertDialog.Builder(this)
                .setTitle("Gagal Menyimpan")
                .setMessage("Semua data harus diisi.")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            return // Hentikan proses simpan
        }

        // Buat objek Warga
        val wargaBaru = Warga(
            namaLengkap = nama,
            nik = nik,
            kabupaten = kabupaten,
            kecamatan = kecamatan,
            desa = desa,
            rt = rt,
            rw = rw,
            jenisKelamin = jenisKelamin,
            statusPernikahan = status
        )

        // Masukkan ke database via ViewModel
        wargaViewModel.insert(wargaBaru)

        Toast.makeText(this, "Data $nama berhasil disimpan!", Toast.LENGTH_SHORT).show()

        // Kosongkan form setelah berhasil disimpan
        clearForm()
    }

    private fun resetData() {
        // Tampilkan dialog konfirmasi sebelum reset
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Reset")
            .setMessage("Apakah Anda yakin ingin menghapus SEMUA data warga? Data tidak dapat dikembalikan.")
            .setPositiveButton("Hapus") { _, _ ->
                // Hapus semua data via ViewModel
                wargaViewModel.deleteAll()
                Toast.makeText(this, "Semua data telah direset.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun clearForm() {
        binding.etNama.text = null
        binding.etNik.text = null
        binding.etKabupaten.text = null
        binding.etKecamatan.text = null
        binding.etDesa.text = null
        binding.etRt.text = null
        binding.etRw.text = null
        binding.dropdownStatus.text = null
        binding.rgJenisKelamin.clearCheck()

        // Set default dropdown (opsional)
        // binding.dropdownStatus.setText(resources.getStringArray(R.array.status_pernikahan_array)[0], false)

        binding.etNama.requestFocus() // Fokus kembali ke field pertama
    }
}
