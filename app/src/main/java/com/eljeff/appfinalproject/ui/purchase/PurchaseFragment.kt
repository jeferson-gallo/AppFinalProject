package com.eljeff.appfinalproject.ui.purchase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eljeff.appfinalproject.data.server.ProductServer
import com.eljeff.appfinalproject.databinding.FragmentPurchaseBinding
import com.eljeff.appfinalproject.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PurchaseFragment : Fragment() {

    //create binding
    private var _binding: FragmentPurchaseBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter

    //inicialir firebase
    private lateinit var auth: FirebaseAuth

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

        //Firebase
        auth = Firebase.auth

        // cargamos la lista de productos desde la base de datos
        loadFromServer()

        // actializamos precio total
        updateAmount()
        return root
    }

    private fun onProductItemClicked(product: ProductServer) {

        val id = auth.currentUser?.uid

        // Instanciamos la base de datos
        val db = Firebase.firestore

        val nmId: String = (product.name.toString()+"_"+id.toString())
        val nameCollection:String = ("cart_list"+"_"+id.toString())
        //**************** nuevo *****************
        db.collection(nameCollection).document(product.name.toString()).set(product)

        //nmId?.let {id -> db.collection(nameCollection).document(product.name.toString()).set(product) }
        //**************** Antiguo *****************

        // Add a new document with a generated ID
        //product.id?.let {id -> db.collection("cart_list").document(id).set(product) }


        // cambiarle el ID al producto
        /*var documentUpdate = HashMap<String, Any>()
        documentUpdate["id"] = id.toString()
        db.collection("cart_list").document(nmId).update(documentUpdate)
        */
        updateAmount()

        Toast.makeText(requireContext(), "agregado - " + product.name, Toast.LENGTH_SHORT).show()
    }

    private fun updateAmount() {
        val db = Firebase.firestore
        val id = auth.currentUser?.uid
        val nameCollection:String = ("cart_list"+"_"+id.toString())


        db.collection(nameCollection).get().addOnSuccessListener { result ->

            var total: Double = 0.0

            for (document in result){
                val product: ProductServer = document.toObject<ProductServer>()
                val costProduct = product.cost?.toDouble()
                val amount = product.amount?.toDouble()
                //Log.d("amount", String.format("%.3f",amount))
                total += (costProduct!! * amount!!)

            }

            val totalSatring = "Total: " + String.format("%.3f",total) + "$"
            //val totalSatring = "Total: " + total.toString() + "$"

            binding.totalTxVw.text = totalSatring

        }


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

