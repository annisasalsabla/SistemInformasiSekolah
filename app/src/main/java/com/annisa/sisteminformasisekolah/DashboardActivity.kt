package com.annisa.sisteminformasisekolah

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.annisa.sekolah.SekolahAdapter
import com.annisa.sekolah.models.SekolahResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.annisa.sisteminformasisekolah.api.ApiClient
import com.annisa.sisteminformasisekolah.models.DeleteSekolahResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var addSchoolButton: FloatingActionButton
    private lateinit var sekolahAdapter: SekolahAdapter
    private lateinit var notFoundImage: ImageView

    // Register activity result callback for adding a school
    private val addSekolahResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            fetchSchools("") // Refresh the list to show the newly added school
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize views
        searchView = findViewById(R.id.svNamaSekolah)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.rvSekolah)
        addSchoolButton = findViewById(R.id.floatBtnTambahSekolah)
        notFoundImage = findViewById(R.id.imgNotFound)

        // Initialize adapter
        sekolahAdapter = SekolahAdapter(ArrayList(), ::onDeleteSekolah)
        recyclerView.adapter = sekolahAdapter

        // Fetch schools on activity creation
        fetchSchools("")

        // Set listener for search query changes
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                fetchSchools(query.orEmpty()) // Fetch schools based on the search query
                return true
            }
        })

        // Set click listener for floating action button to add new school
        addSchoolButton.setOnClickListener {
            val intent = Intent(this, TambahSekolahActivity::class.java)
            addSekolahResult.launch(intent)
        }
    }

    // Function to fetch schools with a given name or search query
    private fun fetchSchools(searchQuery: String) {
        progressBar.visibility = View.VISIBLE
        ApiClient.apiService.getListSekolah(searchQuery).enqueue(object : Callback<SekolahResponse> {
            override fun onResponse(call: Call<SekolahResponse>, response: Response<SekolahResponse>) {
                if (response.isSuccessful) {
                    val sekolahData = response.body()?.data.orEmpty()
                    if (sekolahData.isNotEmpty()) {
                        sekolahAdapter.setData(sekolahData) // Update the adapter with new data
                        notFoundImage.visibility = View.GONE
                    } else {
                        sekolahAdapter.setData(emptyList()) // Set empty data if no schools found
                        notFoundImage.visibility = View.VISIBLE
                    }
                } else {
                    // Handle error response, for example, if the response code is not 200
                    Toast.makeText(this@DashboardActivity, "Error fetching data", Toast.LENGTH_SHORT).show()
                }
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<SekolahResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        })
    }

    // Callback for delete action
    private fun onDeleteSekolah(sekolahId: Int) {
        deleteSekolah(sekolahId)
    }

    // Function to handle deletion of a school
    private fun deleteSekolah(sekolahId: Int) {
        ApiClient.apiService.deleteSekolah(sekolahId.toString()).enqueue(object : Callback<DeleteSekolahResponse> {
            override fun onResponse(call: Call<DeleteSekolahResponse>, response: Response<DeleteSekolahResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    // Notify user and refresh the list after successful deletion
                    Toast.makeText(this@DashboardActivity, "Sekolah berhasil di hapus", Toast.LENGTH_SHORT).show()
                    fetchSchools("") // Refresh the list of schools
                } else {
                    Toast.makeText(this@DashboardActivity, "Gagal mengahapus sekolah", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DeleteSekolahResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}