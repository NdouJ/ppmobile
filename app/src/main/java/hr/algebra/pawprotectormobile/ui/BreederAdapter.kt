package hr.algebra.pawprotectormobile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.pawprotectormobile.databinding.ItemBreederBinding
import hr.algebra.pawprotectormobile.model.Breeder

class BreederAdapter(
    private val breederList: List<Breeder>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<BreederAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(breeder: Breeder)
    }

    inner class ViewHolder(private val binding: ItemBreederBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedBreeder = breederList[position]
                    itemClickListener.onItemClick(clickedBreeder)
                }
            }
        }

        fun bind(breeder: Breeder) {
            binding.breeder = breeder
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBreederBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = breederList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(breederList[position])
    }
}
