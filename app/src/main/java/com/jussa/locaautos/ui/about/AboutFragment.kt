package com.jussa.locaautos.ui.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.jussa.locaautos.R

class AboutFragment : Fragment(), View.OnClickListener   {
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        view.findViewById<Button>(R.id.btnCancelarAbout).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btnCancelarAbout -> {
                navController.navigate(R.id.action_listAutoFragment_to_homeFragment)
            }
        }
    }
}