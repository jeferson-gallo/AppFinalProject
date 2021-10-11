package com.eljeff.appfinalproject.ui.chat

import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.eljeff.appfinalproject.R
import com.eljeff.appfinalproject.data.server.MessageServer
import com.eljeff.appfinalproject.data.server.ProductServer
import com.eljeff.appfinalproject.databinding.FragmentChatBinding
import com.eljeff.appfinalproject.utils.getCurrentDateTime
import com.eljeff.appfinalproject.utils.notEmpty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.card_view_message_item.view.*
import kotlinx.android.synthetic.main.card_view_message_item.view.message_Text_View
import kotlinx.android.synthetic.main.card_view_message_item.view.user_image_view
import kotlinx.android.synthetic.main.card_view_message_to_item.view.*

class ChatFragment : Fragment() {

    //create binding
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private lateinit var chatAdapter : ChatAdapter
    private lateinit var adapter: GroupieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = Firebase.auth
        adapter = GroupieAdapter()

        loadFromServer()
        /*chatAdapter = ChatAdapter()
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatFragment.context)
            adapter = chatAdapter
            //setHasFixedSize(true)
        }*/

        with(binding){
            floatingActionButton.setOnClickListener{
                sendMessage(messageInputEditText.text.toString())
            }
        }


        return  root
    }




    private fun sendMessage(message: String) {
        if (notEmpty(message)){
            uploadMessage(message)
            updateFromView(message)
            updateAnswer(message)
        }
    }

    private fun updateAnswer(message: String) {
        val answer : String
        val complement = "Digita OK para confirmar."
        var able = true
        if (message == "1"){
            answer = "Una vez se confirme el pago se envia el pedido. Numero de cuenta: 102349403." +
                    "Recuerda verificar la direccion y el telefono para el envio."

        }
        else if (message == "2"){
            answer = "El pedido ya se esta preparando para salir. "
        }
        else if (message == "3"){
            answer = "Puedes recoger el pedido una vez hayas transferido." +
                    "Numero de cuenta: 102349403"
        }
        else if (message == "4"){
            answer = "Ya puedes recoger tu pedido."
        }
        else if (message == "Ok" || message == "ok" || message == "OK"){
            answer = "Gracias por tu compra. Feliz resto de dia."
            able = false

            deleteLista()


        }
        else{
            answer = "Opcion no valida."
            able = false
        }
        if (able ) updateToView(answer + complement)
        else updateToView(answer)
    }

    private fun deleteLista() {
        val db = Firebase.firestore
        val id = auth.currentUser?.uid
        val nameCollection: String = ("cart_list" + "_" + id.toString())

        db.collection(nameCollection).get().addOnSuccessListener { result ->


            for (document in result) {
                val product: ProductServer = document.toObject<ProductServer>()
                deletDocument(product.name.toString())
            }
        }
    }

        private fun deletDocument(name: String?) {
            val db = Firebase.firestore
            val id = auth.currentUser?.uid
            val nameCollection: String = ("cart_list" + "_" + id.toString())
            // ********************* delete viejo ******************
            name?.let { name -> db.collection(nameCollection).document(name).delete() }
        }

        private fun uploadMessage(message: String) {
            val from = auth.currentUser?.uid
            val to = "Admin"
            val boolVal = false
            val db = Firebase.firestore
            val document = db.collection("chats").document()
            //val id = document.id
            val id = getCurrentDateTime()
            val message = MessageServer(id, from, to, message, boolVal, true, boolVal)
            db.collection("chats").document(id).set(message)
        }

        private fun updateFromView(message: String) {
            with(binding) {
                messageInputEditText.setText("")
                adapter.add(ChatFromItem(message))
                chatRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }

        private fun updateToView(message: String) {
            with(binding) {
                messageInputEditText.setText("")
                adapter.add(ChatToItem(message))
                chatRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }

        private fun loadFromServer() {


            val db = Firebase.firestore
            db.collection("chats").get().addOnSuccessListener { result ->

                for (document in result) {
                    //
                    val message = document.toObject<MessageServer>()
                    if (message.from.toString() == auth.currentUser?.uid.toString()) {
                        adapter.add(ChatFromItem(message.text.toString()))
                    } else if (message.to.toString() == auth.currentUser?.uid.toString()) {
                        adapter.add(ChatToItem(message.text.toString()))
                    } else if ((message.from.toString() == "Admin")
                        && message.to.toString() == "Everybody"
                    ) {
                        adapter.add(ChatToItem(message.text.toString()))
                    }

                }
                binding.chatRecyclerView.adapter = adapter
                if (adapter.itemCount > 0) binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)

            }

        }

}

class ChatFromItem (private val message: String): Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.card_view_message_item
    }

    override fun bind(p0: GroupieViewHolder, p1: Int) {
        val uri = "https://firebasestorage.googleapis.com/v0/b/appfinalproject-b2c62.appspot.com/o/profile_images%2Fdefault_image.png?alt=media&token=222d2d72-5c50-4fdb-8ccc-983d7e707dbc"
        val targetImageView = p0.itemView.user_image_view
        Picasso.get().load(uri).into(targetImageView)
        p0.itemView.message_Text_View.text = message
    }
}
class ChatToItem(private val message: String) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.card_view_message_to_item
    }

    override fun bind(p0: GroupieViewHolder, p1: Int) {
        val uri = "https://firebasestorage.googleapis.com/v0/b/appfinalproject-b2c62.appspot.com/o/profile_images%2Fic_launcher-playstore.png?alt=media&token=64561be0-829a-4bdb-b5dd-3c1fa16b9c7f"
        val targetImageView = p0.itemView.user_image_view
        Picasso.get().load(uri).into(targetImageView)
        p0.itemView.message_Text_View.text = message
    }
}