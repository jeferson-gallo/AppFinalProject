package com.eljeff.appfinalproject.ui.purchase

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.recyclerview.widget.RecyclerView
import com.eljeff.appfinalproject.R
import com.eljeff.appfinalproject.data.server.ProductServer
import com.eljeff.appfinalproject.databinding.CardViewProductsItemBinding
import com.squareup.picasso.Picasso

class ProductAdapter(private val onItemClicked: (ProductServer) -> Unit):
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var ListProduct: MutableList<ProductServer> = mutableListOf()

    // me dice cual car view o layout quiero pintar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_products_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ListProduct[position])

        // con click al card view le manda el deudor a onItemClicked
        //holder.itemView.setOnClickListener { onItemClicked(ListProduct[position])  }
        holder.itemView.setOnClickListener { onItemClicked(ListProduct[position])  }

    }

    override fun getItemCount(): Int {
        return ListProduct.size
    }

    fun appendItems(newItems: MutableList<ProductServer>){
        ListProduct.clear()
        ListProduct.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(view:View): RecyclerView.ViewHolder(view){

        private val binding = CardViewProductsItemBinding.bind(view)

        fun bind(product: ProductServer){
            with(binding){

                purchaseCostTxVw.text = ("Precio: " + product.cost.toString() + "$")
                purchaseDescriptionTxVw.text = ("Descripción: " + product.description)
                purchaseProductNameTxVw.text = product.name

                if(product.urlPicture != null){
                    Picasso.get().load(product.urlPicture).into(productImgVw)
                }
            }

        }
    }
}