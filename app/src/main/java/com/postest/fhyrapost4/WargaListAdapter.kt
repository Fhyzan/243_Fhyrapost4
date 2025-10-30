package com.postest.fhyrapost4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.postest.fhyrapost4.databinding.ItemWargaBinding

class WargaListAdapter : ListAdapter<Warga, WargaListAdapter.WargaViewHolder>(WargaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WargaViewHolder {
        val binding = ItemWargaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WargaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WargaViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, position + 1) // position + 1 untuk nomor urut
    }

    class WargaViewHolder(private val binding: ItemWargaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(warga: Warga, position: Int) {
            binding.tvNama.text = "$position. ${warga.namaLengkap} (${warga.jenisKelamin}) - ${warga.statusPernikahan}"
            binding.tvNik.text = "NIK: ${warga.nik}"
            binding.tvAlamat.text = "Alamat: RT ${warga.rt}/RW ${warga.rw}, ${warga.desa}, ${warga.kecamatan}, ${warga.kabupaten}"
        }
    }

    class WargaDiffCallback : DiffUtil.ItemCallback<Warga>() {
        override fun areItemsTheSame(oldItem: Warga, newItem: Warga): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Warga, newItem: Warga): Boolean {
            return oldItem == newItem
        }
    }
}