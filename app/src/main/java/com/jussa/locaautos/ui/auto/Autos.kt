package com.jussa.locaautos.ui.auto

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jussa.locaautos.data.DataAuto
import java.net.URI

class Autos {
    private var cont: Context? = null

    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
    private val nomeDB = "autos" //autos é o nome dó nó parent principal da base de dados no FireBase
    private val context: Context? = cont
    private val fBase = FirebaseDatabase.getInstance()
    private var fStorageRef = FirebaseStorage.getInstance().reference
    private val myRef = fBase.getReference(nomeDB)

    fun uploadAutoImage(imgPath: String): StorageReference {

        val uRi: Uri = Uri.parse(imgPath)
        fStorageRef.putFile(uRi)
        return fStorageRef

    }

    fun getAutoImage(imgPath: String): StorageReference {
        return fStorageRef.child(imgPath)
    }

    fun writeNewAuto(
        Chassi: String? = null,
        Descricao: String? = null,
        Imagem: String? = null,
        MarcaModelo: String? = null
    ) {
        //val autos = Autos(NomeAuto, MarcaModelo, Descricao, Imagem, NUMBER )

        val vAuto = DataAuto(
            Chassi,
            Imagem,
            Descricao,
            MarcaModelo
        )

            if (Chassi != null && Chassi != "null" && Chassi != "") {
                myRef.child(Chassi).setValue(vAuto)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "DADOS GRAVADOS COM SUCESSO!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "ERRO NA GRAVACAO DOS DADOS!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
    }

}


