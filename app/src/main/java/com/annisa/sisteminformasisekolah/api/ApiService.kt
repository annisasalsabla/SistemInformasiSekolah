package com.annisa.sisteminformasisekolah.api

import com.annisa.sekolah.models.SekolahResponse
import com.annisa.sisteminformasisekolah.models.RegisterResponse
import com.annisa.sisteminformasisekolah.models.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // Endpoint untuk Register
    @FormUrlEncoded
    @POST("API_SEKOLAH/register.php")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("fullname") fullname: String,
        @Field("email") email: String,
    ): Call<RegisterResponse>

    // Endpoint untuk Login
    @FormUrlEncoded
    @POST("API_SEKOLAH/login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    // Endpoint untuk Mendapatkan Data Sekolah (Semua data atau berdasarkan nama sekolah)
    @GET("API_SEKOLAH/get_sekolah.php")
    fun getListSekolah(@Query("nama_sekolah") namaSekolah: String?): Call<SekolahResponse>

    // Optional: Endpoint untuk Mendapatkan Detail Sekolah Berdasarkan ID
    @GET("API_SEKOLAH/get_sekolah.php")
    fun getDetailSekolah(@Query("id") id: Int): Call<SekolahResponse>
}
