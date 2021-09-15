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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private var isSearching = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var idDebtor: String? = ""

        binding.profileButton.setOnClickListener {

            val name = binding.userNameProEdTx.text.toString()

            if (isSearching) { //actualizando
                //searchInLocal(debtorDao, name, idDebtor)
                val db = Firebase.firestore

                db.collection("users").get().addOnSuccessListener { result ->

                    var debtorEsxist = false

                    for (document in result) {

                        val user: Users = document.toObject<Users>()

                        if (name == user.name) {

                            // guardamos el ID
                            idDebtor = user.uid

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

                            }
                        }
                    }
                    if (!debtorEsxist) {
                        Toast.makeText(requireContext(), "El Usuario no existe", Toast.LENGTH_SHORT).show()
                    }
                }
            } else { // actualizando
                //updateLocal(idDebtor, debtorDao)
                var documentUpdate = HashMap<String, Any>()

                documentUpdate["name"] = binding.userNameProEdTx.text.toString()
                documentUpdate["address"] = binding.addressProEdTx2.text.toString()
                documentUpdate["telephone"] = binding.telephoneProEdTx2.text.toString()
                documentUpdate["phone"] = binding.phoneProEdTx2.text.toString()

                val db = Firebase.firestore
                idDebtor?.let { id ->
                    db.collection("users").document(id)
                        .update(documentUpdate).addOnSuccessListener {
                            Toast.makeText(requireContext(), "Usuario actualizado con exito", Toast.LENGTH_SHORT).show()
                        }
                }

                binding.profileButton.text = getString(R.string.search)
                isSearching = true
                //cleanWidgets()
            }
        }
        return root
    }
}