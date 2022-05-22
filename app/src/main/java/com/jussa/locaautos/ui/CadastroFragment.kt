package com.jussa.locaautos.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.jussa.locaautos.R

class CadastroFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    private lateinit var txtNovoEmail: EditText
    private lateinit var txtNovoPassword: EditText
    private var countValidationPassword: Int = 6
    private lateinit var connCreateUser: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cadastro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        view.findViewById<Button>(R.id.btnCancelar).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnCadastroUsuario).setOnClickListener(this)
        txtNovoEmail = view.findViewById(R.id.txtNovoEmail)
        txtNovoPassword = view.findViewById(R.id.txtNovoPassword)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnCancelar -> requireActivity().onBackPressed()
            R.id.btnCadastroUsuario -> {

                if (!this.validateDataUser())
                    return

                if(!TextUtils.isEmpty(txtNovoEmail.text.toString()) && !TextUtils.isEmpty(txtNovoPassword.text.toString()) ){
                    try {
                        connCreateUser = FirebaseAuth.getInstance()
                        connCreateUser.createUserWithEmailAndPassword(txtNovoEmail.text.toString(), txtNovoPassword.text.toString())
                            .addOnCompleteListener {
                                if(it.isSuccessful){
                                    Toast.makeText(context, getResources().getString(R.string.message_record_user_sucess), Toast.LENGTH_SHORT).show()
                                    val bundle = bundleOf("argUserLogin" to txtNovoEmail.text.toString())
                                    navController!!.navigate(R.id.action_cadastroFragment_to_successFragment, bundle)
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Ocorreu um erro ao criar o usuário: \n ${it.cause.toString()}", Toast.LENGTH_LONG).show()
                            }
                    }
                    catch (e: Exception){
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun validateDataUser(): Boolean {

        if (txtNovoEmail.text.isNullOrEmpty() ||
                txtNovoEmail.text.isNullOrBlank()) {
            Log.d("CadastroFragment", getResources().getString(R.string.message_user_input_email))
            Toast.makeText(context, getResources().getString(R.string.message_user_input_email), Toast.LENGTH_LONG).show()
            return false
        }

        if (txtNovoPassword.text.isNullOrEmpty() ||
                txtNovoPassword.text.isNullOrBlank()) {
            Log.d("CadastroFragment", getResources().getString(R.string.message_user_input_password))
            Toast.makeText(context, getResources().getString(R.string.message_user_input_password), Toast.LENGTH_LONG).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(txtNovoEmail.text).matches()) {
            Log.d("CadastroFragment", getResources().getString(R.string.message_user_input_valid_email))
            Toast.makeText(context, getResources().getString(R.string.message_user_input_valid_email), Toast.LENGTH_LONG).show()
            return false
        }

        if (txtNovoPassword.text.length < countValidationPassword) {
            Log.d("CadastroFragment", getResources().getString(R.string.message_user_input_password_valid))
            Toast.makeText(context, getResources().getString(R.string.message_user_input_password_valid), Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}