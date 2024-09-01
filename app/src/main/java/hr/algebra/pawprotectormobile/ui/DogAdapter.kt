package hr.algebra.pawprotectormobile.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hr.algebra.pawprotectormobile.R
import hr.algebra.pawprotectormobile.databinding.ItemDogBinding
import hr.algebra.pawprotectormobile.model.Dog

class DogAdapter(private val dogList: List<Dog>) : RecyclerView.Adapter<DogAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemDogBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(dog: Dog) {
            binding.breedName.text = dog.breedName
            binding.description.text = dog.description
            binding.avgWeightFemale.text = "Female: ${dog.avgWeightFemale} kg"
            binding.avgWeightMale.text = "Male: ${dog.avgWeightMale} kg"
            Glide.with(binding.root)
                .load(dog.image)
                .apply(RequestOptions().centerCrop())
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = dogList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dogList[position])
    }
}

