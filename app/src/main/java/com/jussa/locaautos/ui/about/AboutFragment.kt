package com.jussa.locaautos.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jussa.locaautos.R

class AboutFragment : Fragment(), View.OnClickListener   {
    private lateinit var navController: NavController
    private var labelVersionDescription: String = "1.0.1"
    private var labelDevelopersDescription: String = "Juscelino Carvalho / Ricardo Lacerda / Marcos Porto / Gabriel Batalha"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        view.findViewById<Button>(R.id.btnCancelarAbout).setOnClickListener(this)
        var labelVersionTextView = view.findViewById<TextView>(R.id.labelTextViewVersion)
        labelVersionTextView.setText(getResources().getString(R.string.version) + labelVersionDescription)

        var labelDevelopersTextView = view.findViewById<TextView>(R.id.labelTextViewDevelopers)
        labelDevelopersTextView.setText(getResources().getString(R.string.developers) + labelDevelopersDescription)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btnCancelarAbout -> requireActivity().onBackPressed()
        }
    }
}