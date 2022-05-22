package com.jussa.locaautos.ui.auto

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jussa.locaautos.data.DataAuto
import java.io.ByteArrayOutputStream

class Autos {
    private val nomeDB = "autos" //autos é o nome dó nó parent principal da base de dados no FireBase
    private val fBase = FirebaseDatabase.getInstance()
    private val fStorageInstance = FirebaseStorage.getInstance()
    private var fStorageRef = fStorageInstance.getReferenceFromUrl("gs://loca-auto-fiap.appspot.com")
    private val myRef = fBase.getReference(nomeDB)

    //Retorna o Path do Arquivo que foi feito Upload
    fun uploadAutoImage(bMap: Bitmap?, vChassi: String): String {
        //var sd: File = Environment.getExternalStorageDirectory();
        //val uri: Uri = getImageUri(context!!, bMap)
        //var strFinalPath = File(getRealPathFromUri(uri))
        ///var drwb: Drawable = bMap.toDrawable(context.resources)
        val arrStream = ByteArrayOutputStream()
        bMap?.compress(Bitmap.CompressFormat.JPEG, 100, arrStream)
        val data: ByteArray = arrStream.toByteArray()
        val fileRef: StorageReference = fStorageRef.child("loca_autos_img/${vChassi}.jpg")

        try {
            if(bMap != null){

            fileRef.putBytes(data)
                .addOnCompleteListener{
                    Log.d("COMPLETE_TASK_UPLOAD", it.result.storage.path) //+ it.result.storage.name
                }
                .addOnSuccessListener {
                    //strFinalPath = it.storage.path //+ it.storage.name
                    Log.d("SUCCESS_TASK_UPLOAD", it.storage.path)
                }
                .addOnFailureListener{
                    //Toast.makeText(context, "Erro ao efetuar o Upload da Imagem..:\n${it.printStackTrace()}", Toast.LENGTH_SHORT).show()
                    Log.d("ERROR_UPLOAD", "Erro ao efetuar upload da imagem...")
                }
            }
        }
        catch (ex: Exception){
            //Toast.makeText(context, "Erro ao efetuar o Upload da Imagem..:\n${ex.printStackTrace()}", Toast.LENGTH_SHORT).show()
            Log.d("ERROR_UPLOAD", "Erro ao efetuar upload da imagem...\n${ex.printStackTrace()}")
        }
        return fileRef.toString()
    }

/*    fun getImageUri(cont: Context, img: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val strPath: String = MediaStore.Images.Media.insertImage(cont.contentResolver, img, "Title", null)
        return Uri.parse(strPath)
    }
    fun getRealPathFromUri(uri: Uri): String{
        var strPath: String = ""
        val vResolver = context?.contentResolver
        if (vResolver != null){
            val vCursor: Cursor? = vResolver.query(uri, null, null, null, null)
            if (vCursor != null){
                vCursor.moveToFirst()
                val indx = vCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                strPath = vCursor.getString(indx)
                vCursor.close()
            }
        }
        return strPath
    }
    fun getAutoImage(imgPathChassi: String): StorageReference {
        val oneMegaByte: Long = 1024 * 1024

        fStorageRef.getBytes(oneMegaByte)
            .addOnSuccessListener {
                //it.
            }
         var img = fStorageRef.downloadUrl
             while (!img.isComplete){
            Log.d("DOWNLOADING....","Downloading Image....")
        }
        return fStorageRef
    }
*/
    fun deleteAuto(pChassi: String) {
        if (pChassi != "null" && pChassi != "") {
            myRef.child(pChassi).removeValue()
                .addOnCompleteListener {
                    deleteImgAuto(pChassi)
                }
                .addOnFailureListener {
                    Log.d("ERROR DELETE AUTO", "Erro ao tentar excluir o veículo!\n${it.printStackTrace()}")
                    //Toast.makeText(context, "Erro ao tentar excluir o veículo!\n${it.printStackTrace()}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun writeNewAuto(
        Chassi: String? = null,
        Descricao: String? = null,
        Imagem: String? = null,
        MarcaModelo: String? = null
    ) {
        val vAuto = DataAuto(
            Chassi,
            Imagem,
            Descricao,
            MarcaModelo
        )

        if (Chassi != null && Chassi != "null" && Chassi != "") {
            myRef.child(Chassi).setValue(vAuto)
                .addOnSuccessListener {
                    Log.d("writeNewAuto_Jussa", "DADOS GRAVADOS COM SUCESSO!")
                    //Toast.makeText(context, "DADOS GRAVADOS COM SUCESSO!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.d("writeNewAuto_Jussa", "ERRO NA GRAVACAO DOS DADOS!")
                    //Toast.makeText(context, "ERRO NA GRAVACAO DOS DADOS!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun deleteImgAuto(vChassi: String) {
        val fileRef: StorageReference = fStorageRef.child("loca_autos_img/${vChassi}.jpg")
        try {
                fileRef.delete()
                    .addOnCompleteListener{
                        Log.d("COMPLET_DELETE_IMG_AUTO", fileRef.path) //+ it.result.storage.name
                    }
                    .addOnSuccessListener {
                        //strFinalPath = it.storage.path //+ it.storage.name
                        Log.d("SUCCESS_DELETE_IMG_AUTO", fileRef.path)
                    }
                    .addOnFailureListener{
                        //Toast.makeText(context, "Erro ao efetuar o Upload da Imagem..:\n${it.printStackTrace()}", Toast.LENGTH_SHORT).show()
                        Log.d("ERROR_DELETE_IMG_AUTO", "Erro ao efetuar upload da imagem...")
                    }
        }
        catch (ex: Exception){
            //Toast.makeText(context, "Erro ao efetuar o Upload da Imagem..:\n${ex.printStackTrace()}", Toast.LENGTH_SHORT).show()
            Log.d("ERROR_UPLOAD", "Erro ao efetuar upload da imagem...\n${ex.printStackTrace()}")
        }
    }
}


