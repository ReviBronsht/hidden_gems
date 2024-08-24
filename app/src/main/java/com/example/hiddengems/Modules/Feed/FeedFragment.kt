package com.example.hiddengems.Modules.Feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.hiddengems.R

class FeedFragment : Fragment() {

    var tvFeedTitle: TextView ?= null
    var title:String?= null

    companion object {

        const val TITLE = "TITLE"
        fun newInstance(title:String) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE,title)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            title = it.getString(TITLE)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        tvFeedTitle = view.findViewById(R.id.tvFeedTitle)
        tvFeedTitle?.text = title ?: "default value"

    return view
    }
}