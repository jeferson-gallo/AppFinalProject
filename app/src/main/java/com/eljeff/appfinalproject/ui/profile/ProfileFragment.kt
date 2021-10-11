package com.eljeff.appfinalproject.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.eljeff.appfinalproject.R
import com.eljeff.appfinalproject.databinding.FragmentProfileOpcBinding
import com.eljeff.appfinalproject.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.card_view_profile_header.view.*
import kotlinx.android.synthetic.main.card_view_profile_item.view.*

class ProfileFragment : Fragment() {

    //private var _binding: FragmentProfileBinding? = null
    private var _binding: FragmentProfileOpcBinding? = null

    private val binding get() = _binding!!
    private var isSearching = true

    private var idProduct: String? = ""

    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: GroupieAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //_binding = FragmentProfileBinding.inflate(inflater, container, false)
        _binding = FragmentProfileOpcBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        adapter = GroupieAdapter()

        loadUser()

        binding.updateButton.setOnClickListener {
            //findNavController().navigate(CartFragmentDirections.actionNavCartToNavChat(true))
            findNavController().navigate(ProfileFragmentDirections.actionNavProfileToProfileEditorFragment())

        }
        return binding.root
    }

    private fun setProfileFields(userInfo : Users) {
        adapter.add(HeaderField(userInfo))
        Log.d("YO","YO")
        adapter.add(ProfileField("Nombre de Usuario", userInfo.name.toString()))
        adapter.add(ProfileField("Correo electrónico", userInfo.email.toString()))
        adapter.add(ProfileField("Celular", userInfo.phone.toString()))
        adapter.add(ProfileField("Teléfono", userInfo.telephone.toString()))
        Log.d("YO","YO")
        adapter.add(ProfileField("Dirección", userInfo.address.toString()))
        binding.profileRecyclerView.adapter = adapter
    }

    private fun loadUser() {
        val db = Firebase.firestore
        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result){
                val user = document.toObject<Users>()
                if (user.id.toString() == auth.currentUser?.uid.toString()){
                    setProfileFields(user)
                }
            }
        }

    }
    /*
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
    }*/
}
class HeaderField (private val user : Users): Item<GroupieViewHolder>() {
    companion object{
        const val TAG = "HEADER FIELD CLASS"
    }

    override fun bind(p0: GroupieViewHolder, p1: Int) {
        val uri = user.urlImage
        val targetImageView = p0.itemView.profile_image_view
        Picasso.get().load(uri).into(targetImageView)
        p0.itemView.name_text_view.text = user.name
        p0.itemView.score_text_view.text = user.score.toString()

        p0.itemView.profile_image_view.setOnClickListener{
            Log.d(TAG, "Cambiando foto de perfil.")
        }

    }

    override fun getLayout(): Int {
        return R.layout.card_view_profile_header
    }

}

class ProfileField (private val field: String,private val value: String): Item<GroupieViewHolder>() {
    override fun bind(p0: GroupieViewHolder, p1: Int) {
        p0.itemView.name_item_text_view.text = field
        p0.itemView.value_item_text_view.text = value

    }

    override fun getLayout(): Int {
        return R.layout.card_view_profile_item
    }

}