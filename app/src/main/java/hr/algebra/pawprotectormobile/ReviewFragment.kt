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
import hr.algebra.pawprotectormobile.databinding.FragmentReviewBinding
import hr.algebra.pawprotectormobile.databinding.FragmnentBreederBinding
import hr.algebra.pawprotectormobile.model.Breeder
import hr.algebra.pawprotectormobile.model.Review
import hr.algebra.pawprotectormobile.ui.BreederAdapter
import hr.algebra.pawprotectormobile.ui.ReviewAdapter
import hr.algebra.pawprotectormobile.ui.SharedViewModel
import hr.algebra.pawprotectormobile.utils.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ReviewFragment: Fragment() {
    private lateinit var viewModel: SharedViewModel
    private var token: String? = null  // Change to nullable
    private var breederId: String = ""
    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewAdapter: ReviewAdapter
    private var reviewList: MutableList<Review> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        breederId = arguments?.getString("idBreeder") ?: "267"
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.sharedString.observe(viewLifecycleOwner, Observer { sharedString ->
            token = sharedString
            if (token != null) {
                fetchReview()
            }
        })

        binding.recyclerViewReview.layoutManager = LinearLayoutManager(context)
        reviewAdapter = ReviewAdapter(reviewList, object : ReviewAdapter.OnItemClickListener {
            override fun onItemClick(review: Review) {
               // openReviewFragment(breeder.idBreeder)
            }
        })
        binding.recyclerViewReview.adapter = reviewAdapter
    }

    private fun fetchReview() {
        token?.let {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = RetrofitInstance.api.getUserReview(breederId, "Bearer $it")
                    if (response.isSuccessful) {
                        val reviews = response.body() ?: emptyList()
                        Log.d("FetchReview", "Fetched review: $reviews")
                        reviewList.clear()
                        reviewList.addAll(reviews)
                        updateRecyclerView()
                    } else {
                        Log.e("FetchReviews", "Failed to fetch reviews: ${response.code()}")
                        showToast("Failed to fetch reviews: ${response.code()}")
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
            reviewAdapter.notifyDataSetChanged()
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