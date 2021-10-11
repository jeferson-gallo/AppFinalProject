package com.eljeff.appfinalproject.ui.cart

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.eljeff.appfinalproject.R
import com.eljeff.appfinalproject.data.server.ListProductServer
import com.eljeff.appfinalproject.data.server.ProductServer
import com.eljeff.appfinalproject.data.server.ProductSumz
import com.eljeff.appfinalproject.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*


class CartFragment : Fragment() {

    //create binding
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter

    //inicialir firebase
    private lateinit var auth: FirebaseAuth

    private var total: Double = 0.0
    private var total_show: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // inicializamos el adaptador
        cartAdapter = CartAdapter(onItemClicked = { onProductItemClicked(it) })
        // configuramos el recycler view
        binding.cartRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CartFragment.context)
            adapter = cartAdapter
            setHasFixedSize(false)
        }

        //Firebase
        auth = Firebase.auth

        // cargamos carrito de la base de datos
        loadFromServer()

        // actializamos precio total
        updateAmount()

        binding.buyButton.setOnClickListener {

            // actializamos precio total
            updateAmount()

            resumePurchase()

            alertDialog()

        }

        return root
    }

    private fun alertDialog() {

        // crear cuadro de alerta
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("¿Desea realizar la compra por un monto de: " + String.format("%.3f", total) + "$ ?")
                setPositiveButton(R.string.accept) { dialog, id ->
                }
                    
                    //cleanCart()
                
                    findNavController().navigate(CartFragmentDirections.actionNavCartToNavChat(true))

                setNegativeButton(R.string.cancel) { dialog, id ->
                }
            }
            builder.create()
        }
        alertDialog?.show()
    }

    private fun resumePurchase() {
        val db = Firebase.firestore
        val id = auth.currentUser?.uid
        val email = auth.currentUser?.email.toString()

        //datos a guardar
        //var listProducts: MutableList<ProductSumz> = arrayListOf()
        val nameCollection: String = ("cart_list" + "_" + id.toString())
        db.collection(nameCollection).get().addOnSuccessListener { result ->
            var listProducts: MutableList<ProductSumz> = arrayListOf()

            for (document in result) {
                val product: ProductServer = document.toObject<ProductServer>()

                val detailProduct = ProductSumz(
                    name = product.name.toString(),
                    cost = product.cost.toString(),
                    amount = product.amount.toString()
                )

                listProducts.add(detailProduct)
            }

            var listProductServer = ListProductServer(
                name = email,
                total = total.toString(),
                date = "11/10/2021",
                delivered = false,
                products = listProducts
            )

            // Add a new document with a generated ID
            db.collection("buy_list").document(email.toString()).set(listProductServer)

        }
    }

    fun updateAmount(){
        val db = Firebase.firestore
        val id = auth.currentUser?.uid
        val nameCollection: String = ("cart_list" + "_" + id.toString())

        db.collection(nameCollection).get().addOnSuccessListener { result ->

            total = 0.0

            for (document in result) {
                val product: ProductServer = document.toObject<ProductServer>()
                val costProduct = product.cost?.toDouble()
                val amount = product.amount?.toDouble()
                //Log.d("amount", String.format("%.3f",amount))
                total += (costProduct!! * amount!!)

            }
            val totalSatring = "Comprar: " + String.format("%.3f", total) + "$"
            //val totalSatring = "Total: " + total.toString() + "$"

            binding.buyButton.text = totalSatring
        }
    }

    private fun loadFromServer() {
        val db = Firebase.firestore
        val id = auth.currentUser?.uid
        val nameCollection: String = ("cart_list" + "_" + id.toString())
        db.collection(nameCollection).get().addOnSuccessListener { result ->

            var listProducts: MutableList<ProductServer> = arrayListOf()

            for (document in result) {
                val product: ProductServer = document.toObject<ProductServer>()
                listProducts.add(product)

            }
            cartAdapter.appendItems(listProducts)
        }
    }

    private fun onProductItemClicked(product: ProductServer) {

        val nameProduct: String? = product.name
        deleteProductFromCart(nameProduct)

        //Actualizar lista
        loadFromServer()
        //Actualizar total
        updateAmount()

        Toast.makeText(requireContext(), "Eliminado - " + product.name, Toast.LENGTH_SHORT).show()


    }

    private fun deleteProductFromCart(name: String?) {

        val db = Firebase.firestore
        val id = auth.currentUser?.uid
        val nameCollection: String = ("cart_list" + "_" + id.toString())
        // ********************* delete viejo ******************
        name?.let { name -> db.collection(nameCollection).document(name).delete() }

    }
}