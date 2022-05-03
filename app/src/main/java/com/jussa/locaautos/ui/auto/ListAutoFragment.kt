package com.jussa.locaautos.ui.auto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jussa.locaautos.AutoActivity
import com.jussa.locaautos.R
import com.jussa.locaautos.RecyclerAdapter
import com.jussa.locaautos.data.DataAuto
import kotlin.collections.ArrayList


class ListAutoFragment : Fragment(), View.OnClickListener {

    private lateinit var recyclerViewAuto: RecyclerView
    private lateinit var imgBtnNewCar: ImageButton
    private lateinit var imgBtnSignOut: ImageButton
    private lateinit var navController: NavController
    private lateinit var vListAutos: ArrayList<DataAuto>
    private var firebaseInstance: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var dataRef: DatabaseReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_auto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewAuto = view.findViewById(R.id.recyclerViewAuto)
        recyclerViewAuto.hasFixedSize()
        recyclerViewAuto.layoutManager = LinearLayoutManager(activity)
        recyclerViewAuto.itemAnimator = DefaultItemAnimator()

        imgBtnSignOut = view.findViewById(R.id.imgBtnSignOut)
        imgBtnNewCar = view.findViewById(R.id.imgBtnNewCar)

        imgBtnNewCar.setOnClickListener(this)
        imgBtnSignOut.setOnClickListener(this)

        navController = view.findNavController()

        vListAutos = arrayListOf()
        getAutoData()

    }

    private fun getAutoData(){
        dataRef = FirebaseDatabase.getInstance().getReference("autos")
        dataRef.addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for(eachAuto in snapshot.children){
                            val vAuto = eachAuto.getValue(DataAuto::class.java)
                            vAuto?.key_chassi = eachAuto.key
                            vListAutos.add(vAuto!!)
                        }
                    }
                    recyclerViewAuto.adapter = RecyclerAdapter(vListAutos)
                }

                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(context, "A tarefa foi cancelada!", Toast.LENGTH_SHORT).show()
                    Log.d("CANCELED", "Nao foi possivel obter os dados do Automovel.")
                }

            }
        )
    }

    override fun onClick(v: View?) {

        when(v!!.id){
            imgBtnNewCar.id -> {

                val intent = Intent(context, AutoActivity::class.java).apply {
                    putExtra("Usuario", "")
                    putExtra("Chassi", "")
                    putExtra("Imagem", "")
                    putExtra("Descricao", "")
                    putExtra("MarcaModelo", "")

                }
                context?.let { ContextCompat.startActivity(it, intent, intent.extras) }
            //navController.navigate(R.id.action_listAutoFragment_to_autoFragment)
            }

            imgBtnSignOut.id -> {
                try {
                    firebaseInstance.signOut()
                    navController.navigate(R.id.action_listAutoFragment_to_loginFragment)
                }catch (e: Exception){
                    //Toast.makeText(context, "Erro ao tentar sair e voltar à tela de login.\n${e.printStackTrace()}", Toast.LENGTH_SHORT).show()
                    Log.d("ListAutoFragment", "Erro ao tentar sair e voltar a tela de login\n${e.printStackTrace()}")
                }

            }
        }
    }

}

