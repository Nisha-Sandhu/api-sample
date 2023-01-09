package com.example.wisdomleafassignment.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.wisdomleafassignment.R
import com.example.wisdomleafassignment.databinding.ListItemTechnologiesBinding
import com.example.wisdomleafassignment.listItems.model.ListResponse


class ListItemAdapter :
    RecyclerView.Adapter<ListItemAdapter.MiViewHolder>() {

    private var mItemClickListener: OnItemClickListener? = null
    val items = ArrayList<ListResponse.ListResponseItem>()

    private var context: Context? = null

    fun addAll(itemsAll: ArrayList<ListResponse.ListResponseItem>) {
        items.addAll(itemsAll)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListItemAdapter.MiViewHolder {
        context = parent.context

        val itemBinding = ListItemTechnologiesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MiViewHolder(itemBinding)
    }

    interface OnItemClickListener {
        fun onItemClick(item: Int)
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        holder.bind(items[position], position, context!!)

        holder.itemView.setOnClickListener {
            mItemClickListener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MiViewHolder(private val itemBinding: ListItemTechnologiesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(
            model: ListResponse.ListResponseItem,
            position: Int, context: Context
        ) {
            itemBinding.tvTitle.text = model.author
            itemBinding.tvDescription.text = model.url

            items[position].download_url.let { it1 ->
                if (it1.isNotEmpty()) {
                    val options: RequestOptions = RequestOptions()
                        .centerCrop()
                    Glide.with(context).load(it1).apply(options)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(itemBinding.ivImage)
                }
            }

        }


    }
}
