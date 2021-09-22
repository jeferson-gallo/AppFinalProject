package com.eljeff.appfinalproject.ui.purchase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eljeff.appfinalproject.data.server.ProductServer
import com.eljeff.appfinalproject.databinding.FragmentPurchaseBinding
import com.eljeff.appfinalproject.model.Users
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PurchaseFragment : Fragment() {

    //create binding
    private var _binding: FragmentPurchaseBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPurchaseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // inicializamos el adaptador
        productAdapter = ProductAdapter(onItemClicked = { onProductItemClicked(it) } )
        // configuramos el recycler view
        binding.purchaseReciclerVw.apply {
            layoutManager = LinearLayoutManager(this@PurchaseFragment.context)
            adapter = productAdapter
            setHasFixedSize(false)
        }


        // cargamos la lista de productos desde la base de datos
        loadFromServer()

        return root
    }

    private fun onProductItemClicked(product: ProductServer) {

        // Instanciamos la base de datos
        val db = Firebase.firestore
        // Add a new document with a generated ID
        product.id?.let {id -> db.collection("cart_list").document(id).set(product) }

        Toast.makeText(requireContext(), "agregado - " + product.name, Toast.LENGTH_SHORT).show()
    }

    private fun loadFromServer() {
        val db = Firebase.firestore
        db.collection("products").get().addOnSuccessListener { result ->

            var listProducts: MutableList<ProductServer> = arrayListOf()

            for (document in result) {
                listProducts.add(document.toObject<ProductServer>())

            }

            productAdapter.appendItems(listProducts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

