package com.eljeff.appfinalproject.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.eljeff.appfinalproject.R
import com.eljeff.appfinalproject.data.server.MessageServer
import com.eljeff.appfinalproject.databinding.CardViewMessageItemBinding

class ChatAdapter(): RecyclerView.Adapter<ChatAdapter.ViewHolder>(){
    private var side : Boolean = false
    private var listMessageServers: MutableList<MessageServer> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val view : View = if (side){
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_message_item,
                parent, false)

        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.card_view_message_to_item,
                parent, false)
        }
        side = !side
        return ChatAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
        holder.bind(listMessageServers[position])
    }

    fun appendItems(newItems: MutableList<MessageServer>){
        listMessageServers.clear()
        listMessageServers.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listMessageServers.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        private val binding = CardViewMessageItemBinding.bind(view)
        fun bind(message: MessageServer){

            with(binding){
                messageTextView.text = message.text
                //imageView2.setImageDrawable(R.drawable.ic_baseline_person_24.toDrawable())
            }

        }

    }
}