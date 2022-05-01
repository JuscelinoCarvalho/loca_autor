package com.jussa.locaautos.ui


import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.jussa.locaautos.R

class CadastroFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    private lateinit var txtNovoEmail: EditText
    private lateinit var txtNovoUsuario: EditText
    private lateinit var txtNovoPassword: EditText
    private lateinit var connCreateUser: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cadastro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        view.findViewById<Button>(R.id.btnCancelar).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnCadastroUsuario).setOnClickListener(this)
        txtNovoUsuario = view.findViewById<EditText>(R.id.txtNovoUsuario)
        txtNovoEmail = view.findViewById<EditText>(R.id.txtNovoEmail)
        txtNovoPassword = view.findViewById(R.id.txtPassword)

    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnCancelar -> requireActivity().onBackPressed()
            R.id.btnCadastroUsuario -> {
                if(!TextUtils.isEmpty(txtNovoEmail.text.toString()) && !TextUtils.isEmpty(txtNovoPassword.text.toString()) ){
                    try {
                        connCreateUser = FirebaseAuth.getInstance()
                        connCreateUser.createUserWithEmailAndPassword(txtNovoEmail.text.toString(), txtNovoPassword.toString())
                            .addOnSuccessListener {
                                Toast.makeText(context, it.user.toString(), Toast.LENGTH_SHORT).show()
                                val bundle = bundleOf("argUserLogin" to txtNovoEmail.text.toString())
                                navController!!.navigate(R.id.action_cadastroFragment_to_successFragment, bundle)
                            }
                            .addOnFailureListener {
                            Toast.makeText(context, "Ocorreu um erro ao criar o usuário: \n ${it.cause.toString()}", Toast.LENGTH_LONG).show()
                        }
                    }catch (e: Exception){
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        //Eu poderia ter utilizado outra sintax
        //como R.id.btnCancelar -> activity!!.onBackPressed() após a seta ->
        //mas mantive com o navigate pela action mesmo apenas para exercitar.
        //Ao final voltei a usar o onBackPressed pois as animações não
        // funcionariam como esperado
    }
}