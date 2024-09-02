package hr.algebra.pawprotectormobile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hr.algebra.pawprotectormobile.databinding.ItemDogBinding
import hr.algebra.pawprotectormobile.model.Dog

class DogAdapter(
    private val dogList: List<Dog>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DogAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(dog: Dog)
    }

    inner class ViewHolder(private val binding: ItemDogBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedDog = dogList[position]
                    itemClickListener.onItemClick(clickedDog)
                }
            }
        }

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
