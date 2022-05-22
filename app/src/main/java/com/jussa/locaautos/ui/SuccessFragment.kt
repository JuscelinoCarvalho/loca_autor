package com.jussa.locaautos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jussa.locaautos.R

class SuccessFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController
    lateinit var argEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argEmail = requireArguments().getString("argNome").toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnOkSuccess).setOnClickListener(this)
    }

    override fun onClick(v: View){
        when(v.id){
            R.id.btnOkSuccess -> navController.navigate(R.id.action_successFragment_to_loginFragment)
        }
    }
}