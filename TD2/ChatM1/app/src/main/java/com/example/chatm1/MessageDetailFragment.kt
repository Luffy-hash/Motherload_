package com.example.chatm1

import android.content.Context
import android.widget.TextView
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatm1.Message
import com.example.chatm1.R

/**
 * A simple [Fragment] subclass.
 * Use the [MessageDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
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
        messageViewModel = ViewModelProvider(requireActivity())[MessageViewModel::class.java]
        messageViewModel.selectedMessage.observe(this.viewLifecycleOwner) {update(it)}
        /**
         * ou on aurais put faire avec la variable messageViewModel
         *
         * messageViewModel.selectedMessage.observe(viewLifecycleOwner, {message -> update(message})
         */
    }


    fun update(message: Message?) {

        if (message != null) {
            mMessageText?.text = message.msg
            mAuthorText?.text = message.author
        }
        mDateText?.text = ""
        mAuthorText?.text = ""
    }

    companion object {
        fun newInstance(): MessageDetailFragment {
            return MessageDetailFragment()
        }
    }
}

