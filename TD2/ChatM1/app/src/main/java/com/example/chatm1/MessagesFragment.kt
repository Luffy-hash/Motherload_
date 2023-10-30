package com.example.chatm1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Fragment représentant une liste d'éléments.
 */
class MessagesFragment
/**
 * Constructeur par défaut (sans argument) obligatoire
 */
    : Fragment() {
    private val mListener: OnListFragmentInteractionListener = object : OnListFragmentInteractionListener {
        override fun onListFragmentInteraction(item: Message?) {messageViewModel.selectMessage(item)}
    }

    private var mMessages: List<Message>? = null
    private var mAdapter: MessageRecyclerViewAdapter? = null
    private lateinit var messageViewModel: MessageViewModel

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Message?)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageViewModel = ViewModelProvider(requireActivity())[MessageViewModel::class.java]
        messageViewModel.messages.observe(viewLifecycleOwner) { messages ->
            mAdapter?.updateMessages(messages)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_message_list, container, false)

        // définir l'adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            recyclerView.layoutManager = LinearLayoutManager(context)
            if (mMessages == null) mMessages = ArrayList()
            if (mAdapter == null) mAdapter = MessageRecyclerViewAdapter(mListener)
            recyclerView.adapter = mAdapter
        }
        return view
    }
}