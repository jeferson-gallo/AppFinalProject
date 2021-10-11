package com.eljeff.appfinalproject.ui.purchase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eljeff.appfinalproject.R
import com.eljeff.appfinalproject.data.server.ProductServer
import com.eljeff.appfinalproject.databinding.CardViewProductsItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProductAdapter(
    private val onItemClicked: (ProductServer) -> Unit
):RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var ListProduct: MutableList<ProductServer> = mutableListOf()

    // me dice cual car view o layout quiero pintar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_products_item,
            parent, false)

        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ListProduct[position])

        // con click al card view le manda el deudor a onItemClicked
        //holder.itemView.setOnClickListener { onItemClicked(ListProduct[position])  }
        //holder.itemView.setOnClickListener { onItemClicked(ListProduct[position])  }

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

        private val binding = CardViewProductsItemBinding.bind(view)


        fun bind(product: ProductServer){
            with(binding){

                // Instanciamos la base de datos
                /*val db = Firebase.firestore
                val auth: FirebaseAuth = Firebase.auth
                val id = auth.currentUser?.uid
                val nameCollection:String = ("cart_list"+"_"+id.toString())
                val nameProduct = product.name

                // cantidad
                var documentUpdate = HashMap<String, Any>()
                documentUpdate["amount"] = 1

                nameProduct?.let {
                        nameProduct -> db.collection(nameCollection).document(nameProduct)
                        .update(documentUpdate)
                }*/


                purchaseCostTxVw.text = ("Precio: " + product.cost + "$")
                purchaseDescriptionTxVw.text = ("Descripción: " + product.description)
                purchaseProductNameTxVw.text = product.name
                purchaseAmountEdTx.setText(product.amount)

                if(product.urlPicture != null){
                    Picasso.get().load(product.urlPicture).into(productImgVw)
                }
                // Boton agregar
                purchaseAddCartBtt.setOnClickListener {

                    // Instanciamos la base de datos
                    /*val db = Firebase.firestore
                    val auth: FirebaseAuth = Firebase.auth
                    val id = auth.currentUser?.uid
                    val nameCollection:String = ("cart_list"+"_"+id.toString())
                    val nameProduct = product.name

                    var amount = binding.purchaseAmountEdTx.text.toString()
                    if(amount.isEmpty()){
                        amount = "1"
                    }

                    // cantidad
                    var documentUpdate = HashMap<String, Any>()
                    documentUpdate["amount"] = amount.toInt()
                    nameProduct?.let {
                            nameProduct -> db.collection(nameCollection).document(nameProduct)
                        .update(documentUpdate)
                    }*/

                    var amount = binding.purchaseAmountEdTx.text.toString()
                    if(amount.isEmpty()){
                        amount = "1"
                    }
                    /*val selectedProduct: ProductServer = ProductServer(
                        id = product.id,
                        name = product.name,
                        amount = amount,
                        description = product.description,
                        urlPicture = product.urlPicture
                    )*/
                    onItemClicked(product)

                }

            }

        }

    }
}