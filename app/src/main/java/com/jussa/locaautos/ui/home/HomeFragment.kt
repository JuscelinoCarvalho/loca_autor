package com.jussa.locaautos.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jussa.locaautos.R

//private const val ARG_STR_USUARIO = "strUsuario"

class HomeFragment : Fragment() {
    //private var strUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    /*      arguments?.let {
            strUsuario = it.getString(ARG_STR_USUARIO)
        } */

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
/*
    companion object {
        /**
         * Create a new instance of
         * this fragment using the provided parameters.
         *
         * @param strUsuario Parameter 1.
         * @return A new instance of fragment HomeFragment.

        @JvmStatic
        fun newInstance(strUsuario: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_STR_USUARIO, strUsuario)
                }
            }*/
    }*/
}