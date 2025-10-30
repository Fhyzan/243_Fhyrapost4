package com.postest.fhyrapost4

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warga_table")
data class Warga(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val namaLengkap: String,
    val nik: String,
    val kabupaten: String,
    val kecamatan: String,
    val desa: String,
    val rt: String,
    val rw: String,
    val jenisKelamin: String,
    val statusPernikahan: String
)