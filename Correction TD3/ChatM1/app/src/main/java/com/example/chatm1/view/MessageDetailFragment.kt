package com.example.chatm1.view

import android.widget.TextView
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatm1.model.Message
import com.example.chatm1.MessageViewModel
import com.example.chatm1.R

/**
 * Ce fragment est destiné à afficher le messgae selectionné dans son intégralité
 */
class MessageDetailFragment : Fragment() {
    var mAuthorText: TextView? = null
    var mMessageText: TextView? = null
    var mDateText: TextView? = null
    private lateinit var messageViewModel: MessageViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val ret = inflater.inflate(R.layout.fragment_message_detail, container, false)
        mAuthorText = ret.findViewById<View>(R.id.msgDetailAuthorText) as TextView
        mMessageText = ret.findViewById<View>(R.id.msgDetailContentText) as TextView
        mDateText = ret.findViewById<View>(R.id.msgDetailDateText) as TextView
        return ret
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageViewModel = ViewModelProvider(requireActivity()).get(MessageViewModel::class.java)
        messageViewModel.selectedMessage.observe(viewLifecycleOwner, Observer {
                message -> update(message)
        })
    }

    /**
     * Méthode destinée à mettre à jour le fragment de manière à ce qu'il affiche le message passé
     * en paramètre.
     * On a juste besoin de mettreà jour les textviews avec ce que le message contient.
     */
    fun update(message: Message?) {
        Log.d("NDFrag", "Update called")
        if (message == null) {
            mAuthorText?.text = ""
            mMessageText?.text = ""
            mDateText?.text=""
            return
        }
        mAuthorText?.text = message.author
        mMessageText?.text = message.msg
        mDateText?.text= message.date.toString()
    }

    companion object {
        fun newInstance(): MessageDetailFragment {
            return MessageDetailFragment()
        }
    }
}