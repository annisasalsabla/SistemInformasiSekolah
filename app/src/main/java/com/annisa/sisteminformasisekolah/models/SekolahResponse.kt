package com.annisa.sekolah.models

data class SekolahResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<ListItems>
) {
    data class ListItems(
        val id: String,
        val nama_sekolah: String,
        val no_telepon: String,
        val akreditasi: String,
        val gambar: String,
        val informasi: String
    )
}
