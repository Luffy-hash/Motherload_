package com.example.chatm1

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

/**
 * [RecyclerView.Adapter] that can display a [Message] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MessageRecyclerViewAdapter(private val mListener: MessagesFragment.OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder>() {

    private val mValues = mutableListOf<Message>()

    // Utilisez cette fonction pour mettre à jour la liste depuis le ViewModel
    fun updateMessages(newMessages: List<Message>) {
        mValues.clear()
        mValues.addAll(newMessages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mAuthorView.text = mValues[position].author
        var textContent = mValues[position].msg
        if (textContent.length > 50) textContent = textContent.substring(0, 47) + "..."
        holder.mMsgView.text = textContent
        holder.mView.setOnClickListener { mListener?.onListFragmentInteraction(holder.mItem) }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mAuthorView: TextView
        val mMsgView: TextView
        val mDateView: TextView
        var mItem: Message? = null
        override fun toString(): String {
            return super.toString() + " '" + mMsgView.text + "'"
        }

        init {
            mAuthorView = mView.findViewById<View>(R.id.id) as TextView
            mMsgView = mView.findViewById<View>(R.id.content) as TextView
            mDateView = mView.findViewById<View>(R.id.date) as TextView
        }
    }
}