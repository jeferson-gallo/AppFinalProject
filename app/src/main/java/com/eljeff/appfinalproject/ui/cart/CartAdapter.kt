package com.eljeff.appfinalproject.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eljeff.appfinalproject.R
import com.eljeff.appfinalproject.data.server.ProductServer
import com.eljeff.appfinalproject.databinding.CardViewCartItemBinding
import com.squareup.picasso.Picasso


class CartAdapter(
    private val onItemClicked: (ProductServer) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var ListProduct: MutableList<ProductServer> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_cart_item,
            parent, false)

        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ListProduct[position])
    }

    override fun getItemCount(): Int {
        return ListProduct.size
    }

    fun appendItems(newItems: MutableList<ProductServer>){
        ListProduct.clear()
        ListProduct.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, var onItemClicked: (ProductServer) -> Unit): RecyclerView.ViewHolder(view){

        private val binding = CardViewCartItemBinding.bind(view)


        fun bind(product: ProductServer){
            with(binding){

                cartCostTxVw.text = ("Precio: " + product.cost.toString() + "$")
                cartDescriptionTxVw.text = ("Descripción: " + product.description)
                cartProductNameTxVw.text = product.name

                if(product.urlPicture != null){
                    Picasso.get().load(product.urlPicture).into(cartProductImgVw)
                }
                // Boton agregar
                cartAddCartBtt.setOnClickListener {
                    onItemClicked(product)
                }

            }

        }

    }

}