package hr.algebra.pawprotectormobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.pawprotectormobile.databinding.FragmentFirstBinding
import hr.algebra.pawprotectormobile.model.Dog
import hr.algebra.pawprotectormobile.model.TokenResponse
import hr.algebra.pawprotectormobile.network.ApiService
import hr.algebra.pawprotectormobile.ui.DogAdapter
import hr.algebra.pawprotectormobile.utils.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DogAdapter
    private val binding get() = _binding!!
    private var dogList: List<Dog> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewDogs
        recyclerView.layoutManager = LinearLayoutManager(context)        // Initialize the adapter
        adapter = DogAdapter(dogList)
        recyclerView.adapter = adapter

        fetchTokenAndDogs()

        return binding.root
    }

    private fun fetchTokenAndDogs() {
        // Launch a coroutine in the IO context
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Fetch the token asynchronously
                val tokenResponse = RetrofitInstance.api.getToken("c9bb9f03-0134-493d-8918-82e450d2ec66")

                if (tokenResponse.isSuccessful) {
                    // Directly extract the token from the response body
                    val token = tokenResponse.body()?.string()?.trim()

                    if (token != null && token.isNotEmpty()) {
                        // Log the token (for debugging purposes, consider removing this in production)
                        Log.d("FetchTokenAndDogs", "Token: $token")

                        // Fetch the dogs asynchronously with the token
                      fetchDogs(token)
                    } else {
                        showToast("Token is null or empty")
                    }
                } else {
                    showToast("Failed to fetch token: ${tokenResponse.code()}")
                }
            } catch (e: IOException) {
                showToast("Network error: ${e.localizedMessage}")
            } catch (e: HttpException) {
                showToast("HTTP error: ${e.localizedMessage}")
            } catch (e: Exception) {
                showToast("Unexpected error: ${e.localizedMessage}")
            }
        }
    }



    private suspend fun fetchDogs(token: String) {
        try {
            // Fetch the dogs asynchronously
            val dogsResponse = RetrofitInstance.api.getAllDogs("Bearer $token")
            if (dogsResponse.isSuccessful) {
                val dogs = dogsResponse.body() ?: emptyList()
                dogList = dogs
                updateRecyclerView()
            } else {
                showToast("Failed to fetch dogs: ${dogsResponse.code()}")
            }
        } catch (e: IOException) {
            showToast("Network error: ${e.localizedMessage}")
        } catch (e: HttpException) {
            showToast("HTTP error: ${e.localizedMessage}")
        }
    }

    private fun updateRecyclerView() {
        // Switch to the main thread to update the UI
        lifecycleScope.launch(Dispatchers.Main) {
            adapter = DogAdapter(dogList)
            recyclerView.adapter = adapter
        }
    }

    private fun showToast(message: String) {
        // Show a toast message on the main thread
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}
