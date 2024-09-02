package hr.algebra.pawprotectormobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.pawprotectormobile.databinding.FragmentSecondBinding
import hr.algebra.pawprotectormobile.databinding.FragmnentBreederBinding
import hr.algebra.pawprotectormobile.model.Dog
import hr.algebra.pawprotectormobile.ui.BreederAdapter
import hr.algebra.pawprotectormobile.ui.DogAdapter
import hr.algebra.pawprotectormobile.ui.SharedViewModel
import hr.algebra.pawprotectormobile.model.Breeder
import hr.algebra.pawprotectormobile.utils.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class BreederFragment : Fragment() {
    private lateinit var viewModel: SharedViewModel
    private var token: String? = null  // Change to nullable
    private var breedName: String = ""
    private var _binding: FragmnentBreederBinding? = null
    private val binding get() = _binding!!
    private lateinit var breederAdapter: BreederAdapter
    private var breederList: MutableList<Breeder> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        breedName = arguments?.getString("breed_name") ?: "AKITA"
        _binding = FragmnentBreederBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.sharedString.observe(viewLifecycleOwner, Observer { sharedString ->
            token = sharedString
            // Fetch breeders after token is initialized
            if (token != null) {
                fetchBreeders()
            }
        })

        binding.recyclerViewBreeder.layoutManager = LinearLayoutManager(context)
        breederAdapter = BreederAdapter(breederList, object : BreederAdapter.OnItemClickListener {
            override fun onItemClick(breeder: Breeder) {
                //openBreederFragment(dog.breedName)
            }
        })
        binding.recyclerViewBreeder.adapter = breederAdapter
    }

    private fun fetchBreeders() {
        token?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = RetrofitInstance.api.getBreederOfDog(breedName, "Bearer $it")
                    if (response.isSuccessful) {
                        val breeders = response.body() ?: emptyList()
                        Log.d("FetchDogs", "Fetched dogs: $breeders")
                        breederList.clear()
                        breederList.addAll(breeders)
                        updateRecyclerView()
                    } else {
                        Log.e("FetchBreeders", "Failed to fetch breeders: ${response.code()}")
                        showToast("Failed to fetch breeders: ${response.code()}")
                    }
                } catch (e: IOException) {
                    showToast("Network error: ${e.localizedMessage}")
                } catch (e: HttpException) {
                    showToast("HTTP error: ${e.localizedMessage}")
                } catch (e: Exception) {
                    showToast("Unexpected error: ${e.localizedMessage}")
                }
            }
        } ?: showToast("Token is not initialized")
    }

    private fun updateRecyclerView() {
        lifecycleScope.launch(Dispatchers.Main) {
            breederAdapter.notifyDataSetChanged()
        }
    }

    private fun showToast(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

