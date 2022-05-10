package com.jussa.locaautos.ui.login

import android.os.Bundle
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

class LoginFragment : Fragment(), View.OnClickListener   {
    private lateinit var navController: NavController
    private lateinit var bundle: Bundle
    private lateinit var connect: FirebaseAuth
    private lateinit var vTxtEmailLogin: EditText
    private lateinit var vTxtPassLogin: EditText
    private var countValidationPassword: Int = 6

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connect = FirebaseAuth.getInstance()

        navController = Navigation.findNavController(view)

        vTxtEmailLogin = view.findViewById(R.id.txtUserLogin)
        vTxtPassLogin = view.findViewById(R.id.txtPasswordLogin)

        view.findViewById<Button>(R.id.btnLogin).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnRegister).setOnClickListener(this)
    }

    override fun onClick(v: View?){
        when(v!!.id){
            R.id.btnRegister -> {
                navController.navigate(R.id.action_loginFragment_to_cadastroFragment)
            }
            R.id.btnLogin -> {
                try {
                    connect = FirebaseAuth.getInstance()

                    if (!this.validateDataUser())
                        return
                        connect.signInWithEmailAndPassword(
                            vTxtEmailLogin.text.toString(),
                            vTxtPassLogin.text.toString()
                        )
                      .addOnCompleteListener {signIn ->
                            if(signIn.isComplete){
                                Log.d("LOGIN_COMPLETE", "Estrou no isComplete do Login")
                                /*
                                bundle = bundleOf(
                                    "argEmail" to vTxtEmailLogin.text.toString(),
                                    "argPassword" to vTxtPassLogin.text.toString()
                                )
                                //navController.navigate(R.id.action_loginFragment_to_listAutoFragment, bundle)
                                navController.navigate(R.id.action_loginFragment_to_homeFragment)
                            */
                            }
                          if(signIn.isCanceled){
                              Log.d("LOGIN_CANCELED", "Estrou no isCanceled do Login")
                            }
                          if(signIn.isSuccessful) {
                                bundle = bundleOf(
                                    "argEmail" to vTxtEmailLogin.text.toString(),
                                    "argPassword" to vTxtPassLogin.text.toString()
                                )
                                //navController.navigate(R.id.action_loginFragment_to_listAutoFragment, bundle)
                                navController.navigate(R.id.action_loginFragment_to_homeFragment, bundle)
                            }else{
                                Log.d("LoginFragment", "Erro ao efetuar login: \n${signIn.exception.toString()}")
                                //Toast.makeText(context,"Erro ao efetuar o login: \n ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                            }
                        }
                    .addOnFailureListener {
                        Log.d("LoginFragment", "Erro ao efetuar o login: \n ${it.printStackTrace()}")
                        Toast.makeText(context, "Erro ao efetuar o login: \n ${it.printStackTrace()}", Toast.LENGTH_LONG).show()
                    }
                }
                catch(e: Exception) {
                    Log.d("LoginFragment", "Error in the application!")
                    Toast.makeText(requireContext(), "Error in the application!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateDataUser(): Boolean {

        if (vTxtEmailLogin.text.isNullOrEmpty() ||
                vTxtEmailLogin.text.isNullOrBlank()) {
            Log.d("LoginFragment", "Please type a e-mail \n")
            Toast.makeText(context, "Please type a e-mail \n", Toast.LENGTH_LONG).show()
            return false
        }

        if (vTxtPassLogin.text.isNullOrEmpty() ||
                vTxtPassLogin.text.isNullOrBlank()) {
            Log.d("LoginFragment", "Please type a password \n")
            Toast.makeText(context, "Please type a password \n", Toast.LENGTH_LONG).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(vTxtEmailLogin.text).matches()) {
            Log.d("LoginFragment", "Please type a valid e-mail \n")
            Toast.makeText(context, "Please type a valid e-mail \n", Toast.LENGTH_LONG).show()
            return false
        }

        if (vTxtPassLogin.text.length < countValidationPassword) {
            Log.d("LoginFragment", "Please type a password equals or greather than $countValidationPassword digits \n"
            )
            Toast.makeText(context, "Please type a password  equals or greather than $countValidationPassword digits\n", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}
