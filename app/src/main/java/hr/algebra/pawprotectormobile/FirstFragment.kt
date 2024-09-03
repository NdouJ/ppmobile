package hr.algebra.pawprotectormobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.pawprotectormobile.databinding.FragmentFirstBinding
import hr.algebra.pawprotectormobile.model.Dog
import hr.algebra.pawprotectormobile.network.ApiService
import hr.algebra.pawprotectormobile.ui.DogAdapter
import hr.algebra.pawprotectormobile.ui.SharedViewModel
import hr.algebra.pawprotectormobile.utils.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FirstFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DogAdapter
    private var dogList: MutableList<Dog> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        binding.recyclerViewDogs.layoutManager = LinearLayoutManager(context)

        // Initialize adapter with a mutable list and click listener
        adapter = DogAdapter(dogList, object : DogAdapter.OnItemClickListener {
            override fun onItemClick(dog: Dog) {
                openBreederFragment(dog.breedName)
            }
        })
        binding.recyclerViewDogs.adapter = adapter

        fetchTokenAndDogs() // Fetch data after initializing the adapter

        // Setup button click listener
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun fetchTokenAndDogs() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val tokenResponse = RetrofitInstance.api.getToken("c9bb9f03-0134-493d-8918-82e450d2ec66")

                if (tokenResponse.isSuccessful) {
                    val token = tokenResponse.body()?.string()?.trim()
                    viewModel.sharedString.postValue(token)

                    if (token != null && token.isNotEmpty()) {
                        Log.d("FetchTokenAndDogs", "Token: $token")
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
            val dogsResponse = RetrofitInstance.api.getAllDogs("Bearer $token")
            if (dogsResponse.isSuccessful) {
                val dogs = dogsResponse.body() ?: emptyList()
                Log.d("FetchDogs", "Fetched dogs: $dogs")
                dogList.clear()
                dogList.addAll(dogs)
                updateRecyclerView()
            } else {
                Log.e("FetchDogs", "Failed to fetch dogs: ${dogsResponse.code()}")
                showToast("Failed to fetch dogs: ${dogsResponse.code()}")
            }
        } catch (e: IOException) {
            Log.e("FetchDogs", "Network error: ${e.localizedMessage}")
            showToast("Network error: ${e.localizedMessage}")
        } catch (e: HttpException) {
            Log.e("FetchDogs", "HTTP error: ${e.localizedMessage}")
            showToast("HTTP error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("FetchDogs", "Unexpected error: ${e.localizedMessage}")
            showToast("Unexpected error: ${e.localizedMessage}")
        }
    }

    private fun updateRecyclerView() {
        lifecycleScope.launch(Dispatchers.Main) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun showToast(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openBreederFragment(breedName: String) {
        val bundle = Bundle().apply {
            putString("breed_name", breedName)
        }
        findNavController().navigate(R.id.action_FirstFragment_to_BreederFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
