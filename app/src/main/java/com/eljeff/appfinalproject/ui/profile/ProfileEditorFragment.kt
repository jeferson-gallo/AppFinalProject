package com.eljeff.appfinalproject.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eljeff.appfinalproject.R
import com.eljeff.appfinalproject.databinding.FragmentProfileBinding
import com.eljeff.appfinalproject.model.Users
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProfileEditorFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var isSearching : Boolean = false
    private var idProduct : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        binding.profileButton.setOnClickListener {
            updateProfile()

        }
        return binding.root
    }

    private fun updateProfile() {
        val name = binding.userNameProEdTx.text.toString()
        if (isSearching) { //Buscando
            //searchInLocal(debtorDao, name, idProduct)
            searchUser(name)
        } else { // actualizando
            //updateLocal(idProduct, debtorDao)
            updateFields()
            binding.profileButton.text = getString(R.string.search)
            isSearching = true
            cleanWidgets()
        }
    }

    private fun searchUser(name : String) {
        val db = Firebase.firestore

        db.collection("users").get().addOnSuccessListener { result ->

            var debtorEsxist = false

            for (document in result) {

                val user: Users = document.toObject<Users>()

                if (name == user.name) {

                    // guardamos el ID
                    idProduct = user.id

                    debtorEsxist = true

                    with(binding) {

                        // campos
                        emailProEdTx.setText(user.email)
                        addressProEdTx2.setText(user.address)
                        telephoneProEdTx2.setText(user.telephone)
                        phoneProEdTx2.setText(user.phone)
                        profileScore.text = getString(R.string.score_2, user.score.toString())

                        // boton
                        profileButton.text = getString(R.string.update)
                        isSearching = false

                        //Toast.makeText(requireContext(), "User: "+idProduct, Toast.LENGTH_SHORT).show()

                    }
                }
            }
            if (!debtorEsxist) {
                Toast.makeText(requireContext(), "El Usuario no existe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFields() {
        var documentUpdate = HashMap<String, Any>()

        documentUpdate["name"] = binding.userNameProEdTx.text.toString()
        documentUpdate["address"] = binding.addressProEdTx2.text.toString()
        documentUpdate["telephone"] = binding.telephoneProEdTx2.text.toString()
        documentUpdate["phone"] = binding.phoneProEdTx2.text.toString()

        val db = Firebase.firestore
        //Toast.makeText(requireContext(), "User: "+idProduct, Toast.LENGTH_SHORT).show()
        idProduct?.let { id ->
            db.collection("users").document(id)
                .update(documentUpdate).addOnSuccessListener {
                    Toast.makeText(requireContext(), "Usuario actualizado con exito", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun cleanWidgets() {
        with(binding){
            profileScore.text = getString(R.string.score_1)
            userNameProEdTx.setText("")
            emailProEdTx.setText("")
            addressProEdTx2.setText("")
            telephoneProEdTx2.setText("")
            phoneProEdTx2.setText("")
        }
    }
}