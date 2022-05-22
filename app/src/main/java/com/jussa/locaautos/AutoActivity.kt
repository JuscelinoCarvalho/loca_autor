package com.jussa.locaautos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jussa.locaautos.ui.auto.AutoFragmentArgs

class AutoActivity() : AppCompatActivity(){

   companion object AutoActivity {

       private const val IS_PRIVACY_POLICY_ACCEPTED = "isPrivacyPolicyAccepted"
       private const val IMAGE_PICK_CODE = 1000
       private const val PERMISSION_CODE = 1001
       lateinit var recyclerView: RecyclerView

        fun open(context: Context, isAcceptedNewPrivacyPolicy: Boolean) {
            context.startActivity(Intent(context, AutoActivity::class.java).
                    apply {
                        putExtra(IS_PRIVACY_POLICY_ACCEPTED, "isPrivacyPolicyAccepted")
                    }
            )
        }
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_auto_fragment)
        val navControler = navHostFragment?.findNavController()
        val graphInflater = navControler?.navInflater
        val navGraph = graphInflater?.inflate(R.navigation.main_graph)
    
        val destination = if (intent.getBooleanExtra(IS_PRIVACY_POLICY_ACCEPTED,false)) {
            R.id.activity_auto
        }
        else {
            R.id.autoFragment
        }

        //O nome da classe do fragmento de start Args é uma classe gerada automaticamente
        //para podermos passar os argumentos da intent da Activity para o Fragment.
        if(intent.extras != null) {
            //Tive que testar nulo e forçar uma variável receber o valor Bundle senão teria errro
            // de TypeMismach from Bundle? to Bundle
            val vBundle: Bundle = intent.extras as Bundle
            AutoFragmentArgs.fromBundle(vBundle)
        }

        //Como utilizei uma nova Activity (não a Main) e direcionei ela para o mesmo
        //NAV_HOST_FRAGMENT que é o container que comporta a navegação entre os Fragments
        //na Activity a partir do MAIN_GRAPH onde esta \t\o\d\o o fluxo de navegação dos Frags,
        //Tive que novamente SETAR o Fragmento de inicial do NAV_HOST_FRAGMENT, senão ele volta
        //no Fragmento inicial configurado anteriormente que é a tela de LOGIN
        navGraph?.setStartDestination(destination)

        if (navControler != null) {
            if (navGraph != null) {
                navControler.graph = navGraph
            }
        }
    }
}