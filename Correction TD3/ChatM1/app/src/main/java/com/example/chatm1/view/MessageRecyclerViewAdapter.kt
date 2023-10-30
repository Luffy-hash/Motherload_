package com.example.chatm1.view

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.chatm1.model.Message
import com.example.chatm1.R

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

    // Cette méthode est utilisée par la RecyclerView quand elle a besoin de créer les éléments
    // graphiques composant un item de liste.
    // NB : on n'en créé par un pour chaque élément de la liste : le recyclierView est prévue pour
    // réutiliser ceux qui ne sont plus actuellement afffichés.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item_message, parent, false)
        return ViewHolder(view)
    }

    // Cette méthode est utilisée par la reciclerView pour afficher un message dans un item de liste.
    // Il faut donc dans cette méthode mettre à jor les éléments de 'holder' de manière à ce qu'ils
    // affichent le message en position 'position'.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mAuthorView.text = mValues[position].author
        var textContent = mValues[position].msg
        if (textContent.length > 50) textContent = textContent.substring(0, 47) + "..."
        holder.mMsgView.text = textContent
        holder.mView.setOnClickListener { mListener?.onListFragmentInteraction(holder.mItem) }
    }

    // Doit retourner le nombre d'élément à afficher dnas la liste
    override fun getItemCount(): Int {
        return mValues.size
    }

    // Notre viewHolder. Il s'agit de l'élément graphique correspondant à un item de liste.
    // Le notre contient 3 champs texte destinés à afficher la date, l'auteur et le début du contenu
    // du message
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