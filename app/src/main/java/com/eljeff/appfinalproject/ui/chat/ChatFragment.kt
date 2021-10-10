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
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.card_view_message_item.view.*
import kotlinx.android.synthetic.main.card_view_message_item.view.messageCardView
import kotlinx.android.synthetic.main.card_view_message_item.view.message_Text_View
import kotlinx.android.synthetic.main.card_view_message_to_item.view.*
import java.util.*


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

        loadFromServer()


        return  root
    }


    private fun sendMessage(message: String) {
        val from = auth.currentUser?.uid
        val to = "Admin"
        val boolVal = false
        if (notEmpty(message)){
            val db = Firebase.firestore
            val document = db.collection("chats").document()
            //val id = document.id
            val id = getCurrentDateTime()
            Log.d("Tamaño Lista Online", getCurrentDateTime())
            val message = MessageServer(id,from,to,message,boolVal,true,boolVal)
            db.collection("chats").document(id).set(message)
            updateView(message)
        }
    }

    private fun updateView(message: MessageServer) {
        with(binding){
            messageInputEditText.setText("")
            adapter.add(ChatFromItem(message.text.toString()))
            chatRecyclerView.smoothScrollToPosition(adapter.itemCount-1)
        }
    }

    private fun loadFromServer() {


        val db = Firebase.firestore
        db.collection("chats").orderBy("id").get().addOnSuccessListener { result ->

            //var listMessages: MutableList<MessageServer> = arrayListOf()

            for (document in result){
                //
                val message= document.toObject<MessageServer>()
                if (message.from.toString() == auth.currentUser?.uid.toString()){
                    //listMessages.add(message)
                    adapter.add(ChatFromItem(message.text.toString()))
                }
                else if (message.to.toString() == auth.currentUser?.uid.toString()){
                    //listMessages.add(message)
                    adapter.add(ChatToItem(message.text.toString()))
                }

            }
            binding.chatRecyclerView.adapter = adapter
            binding.chatRecyclerView.smoothScrollToPosition(adapter.itemCount-1)
            //chatAdapter.appendItems(listMessages)
        }
    }


}
class ChatFromItem (private val message: String): Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.card_view_message_item
    }

    override fun bind(p0: GroupieViewHolder, p1: Int) {
        p0.itemView.message_Text_View.text = message
    }
}
class ChatToItem(private val message: String) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.card_view_message_to_item
    }

    override fun bind(p0: GroupieViewHolder, p1: Int) {
        p0.itemView.message_Text_View.text = message
    }
}