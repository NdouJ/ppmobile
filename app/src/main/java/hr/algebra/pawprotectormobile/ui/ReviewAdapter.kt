package hr.algebra.pawprotectormobile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.pawprotectormobile.databinding.ItemBreederBinding
import hr.algebra.pawprotectormobile.databinding.ItemReviewBinding
import hr.algebra.pawprotectormobile.model.Review

class ReviewAdapter(private val reviewList: List<Review>,
                    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(review: Review)
    }

    inner class ViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedReview = reviewList[position]
                    itemClickListener.onItemClick(clickedReview)
                }
            }
        }

        fun bind(review: Review) {
            binding.review = review
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = reviewList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }

}