package com.eljeff.appfinalproject.ui.cart

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eljeff.appfinalproject.R
import com.eljeff.appfinalproject.data.server.ProductServer
import com.eljeff.appfinalproject.databinding.CardViewCartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class CartAdapter(
    private val onItemClicked: (ProductServer) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var ListProduct: MutableList<ProductServer> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.card_view_cart_item,
            parent, false
        )

        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ListProduct[position])
    }

    override fun getItemCount(): Int {
        return ListProduct.size
    }

    fun appendItems(newItems: MutableList<ProductServer>) {
        ListProduct.clear()
        ListProduct.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, var onItemClicked: (ProductServer) -> Unit) : RecyclerView.ViewHolder(view) {

        private val binding = CardViewCartItemBinding.bind(view)


        fun bind(product: ProductServer) {
            with(binding) {

                cartCostTxVw.text = ("Precio: " + product.cost + "$")
                cartDescriptionTxVw.text = ("Descripción: " + product.description)
                cartProductNameTxVw.text = product.name
                cartAmountEdTx.setText(product.amount)

                if (product.urlPicture != null) {
                    Picasso.get().load(product.urlPicture).into(cartProductImgVw)
                }
                // Boton agregar
                cartAddCartBtt.setOnClickListener {
                    onItemClicked(product)
                }

                cartAmountEdTx.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        //Log.d("edtx", "aft - s: " + s.toString())
                        val amount = s.toString()
                        if (amount.isNotEmpty()) {

                            // Instanciamos la base de datos
                            val db = Firebase.firestore
                            val auth: FirebaseAuth = Firebase.auth
                            val id = auth.currentUser?.uid
                            val nameCollection: String = ("cart_list" + "_" + id.toString())
                            val nameProduct = product.name

                            // cantidad
                            var documentUpdate = HashMap<String, Any>()
                            documentUpdate["amount"] = amount
                            nameProduct?.let { nameProduct ->
                                db.collection(nameCollection).document(nameProduct)
                                    .update(documentUpdate)
                            }
                        }
                    }

                })

            }

        }

    }

}