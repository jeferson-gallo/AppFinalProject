package com.eljeff.appfinalproject.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.eljeff.appfinalproject.data.server.ProductServer
import com.eljeff.appfinalproject.databinding.FragmentCartBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CartFragment : Fragment() {

    //create binding
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // inicializamos el adaptador
        cartAdapter = CartAdapter( onItemClicked = { onProductItemClicked(it) } )
        // configuramos el recycler view
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CartFragment.context)
            adapter = cartAdapter
            setHasFixedSize(false)
        }

        // cargamos carrito de la base de datos
        loadFromServer()

        return  root
    }

    private fun loadFromServer() {
        val db = Firebase.firestore
        db.collection("cart_list").get().addOnSuccessListener { result ->

            var listProducts: MutableList<ProductServer> = arrayListOf()

            for (document in result){
                listProducts.add(document.toObject<ProductServer>())

            }
            cartAdapter.appendItems(listProducts)
        }
    }

    private fun onProductItemClicked(product: ProductServer) {

        val nameProduct: String? = product.name
        deleteProductFromCart(nameProduct)

        Toast.makeText(requireContext(), "Eliminado - " + product.name, Toast.LENGTH_SHORT).show()

    }

    private fun deleteProductFromCart(name: String?) {
        val db = Firebase.firestore
        db.collection("cart_list").get().addOnSuccessListener { result ->
            for (document in result) {
                val product: ProductServer = document.toObject<ProductServer>()
                if (name == product.name) {
                    product.id?.let { id ->
                        db.collection("cart_list").document(id).delete()
                    }

                }
                loadFromServer()
            }
        }
    }
}