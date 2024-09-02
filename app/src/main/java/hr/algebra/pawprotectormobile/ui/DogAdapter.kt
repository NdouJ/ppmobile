package hr.algebra.pawprotectormobile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hr.algebra.pawprotectormobile.databinding.ItemDogBinding
import hr.algebra.pawprotectormobile.model.Dog

class DogAdapter(private val dogList: List<Dog>) : RecyclerView.Adapter<DogAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDogBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dog: Dog) {
            binding.dog = dog
            binding.executePendingBindings() // Ensures that the binding happens immediately

            // Load the image with Glide
            Glide.with(binding.root)
                .load(dog.image) // Assuming 'dog.image' is a URL or a resource ID
                .apply(RequestOptions().centerCrop())
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dogList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dogList[position])
    }
}
