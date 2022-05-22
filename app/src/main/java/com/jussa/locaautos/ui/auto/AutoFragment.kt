package com.jussa.locaautos.ui.auto

import android.Manifest.permission
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.jussa.locaautos.R
import kotlin.math.min


class AutoFragment : Fragment(), View.OnClickListener {

    private lateinit var register: ActivityResultLauncher<Intent>
    private lateinit var vUsuario: String

    private var vChassi: String? = null
    private var vImage: String? = "http://img"
    private var vDesc: String? = null
    private var vMarcaModelo: String? = null

    private lateinit var btnGravar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnExcluir: Button

    private lateinit var varTxtCHASSI: EditText
    private lateinit var imgVwCarro: ImageView
    private lateinit var varTxtMarcaModelo: EditText
    private lateinit var varTxtDescricaoVeiculo: EditText
    private lateinit var navController: NavController
    private lateinit var bmp: Bitmap
    private var countValidationPassword: Int = 17

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    private val permReqLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        isGranted ->
        if (isGranted){
            Log.i("DEBUG", "Permission Granted!!!")
        }
        else{
            Log.i("DEBUG", "Permission DENIED!!!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vUsuario = requireActivity().intent.extras?.get("Usuario") as String
        vChassi = requireActivity().intent.extras?.get("Chassi") as String
        vDesc = requireActivity().intent.extras?.get("Descricao") as String
        vMarcaModelo = requireActivity().intent.extras?.get("MarcaModelo") as String

        permReqLauncher.launch(permission.READ_EXTERNAL_STORAGE)

        register = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK && it != null){
                try {
                    val imageUri: Uri = it.data!!.data as Uri
                    bmp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.requireContext().contentResolver, imageUri))
                    } else {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(this.requireContext().contentResolver, imageUri)
                    }
                    if(!::imgVwCarro.isInitialized){
                        imgVwCarro = view?.findViewById(R.id.imgCarro)!!
                    }
                    imgVwCarro.setImageBitmap(bmp)

                }
                catch (e: Exception){
                    Log.d("AutoFragment", "${e.printStackTrace()}")
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.auto_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnGravar = view.findViewById(R.id.btnGravarCarro)
        btnCancelar = view.findViewById(R.id.btnCancelarCarro)
        btnExcluir = view.findViewById(R.id.btnDeleteAuto)

        varTxtMarcaModelo = view.findViewById(R.id.txtMarcaModelo)
        imgVwCarro = view.findViewById(R.id.imgCarro)
        varTxtDescricaoVeiculo = view.findViewById(R.id.txtDescricaoVeiculo)
        varTxtCHASSI = view.findViewById(R.id.txtChassi)
        navController = view.findNavController()

        if(vChassi != null && vChassi != "") {
            btnExcluir.visibility = VISIBLE
            btnCancelar.text = getString(R.string.voltar)
            view.findViewById<Button>(R.id.btnDeleteAuto).setOnClickListener(this)
        }
        else {
            btnExcluir.visibility = INVISIBLE
            btnCancelar.text = getString(R.string.cancelar)
        }

        // url firebase -> "gs://loca-auto-fiap.appspot.com"
        val fbaseInstance = FirebaseStorage.getInstance()
        val fbaseStore = fbaseInstance.getReference("loca_autos_img/")
        try {

            varTxtMarcaModelo.setText(vMarcaModelo)
            varTxtDescricaoVeiculo.setText(vDesc)
            varTxtCHASSI.setText(vChassi)

            fbaseStore.child("$vChassi.jpg").downloadUrl
                .addOnSuccessListener {
                    Glide.with(context)
                        .load(it.toString())
                        .into(imgVwCarro)
                }
                .addOnFailureListener{
                    Log.d("ERROR onFailureListener", "Erro Jussa..: ${it.printStackTrace()}")
                }
        }
        catch (e: Exception){
            Log.d("ERROR DRAWABLE", "Erro Drawable Jussa..: ${e.printStackTrace()}")
        }

        view.findViewById<ImageView>(R.id.imgCarro).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnGravarCarro).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnCancelarCarro).setOnClickListener(this)

    }

    @Suppress("unused")
    fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = ACTION_GET_CONTENT
        register.launch(intent)//Modo novo de chamada
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imgVwCarro.setImageURI(data?.data)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.imgCarro -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(this.requireContext(), permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission denied
                        val permission = permission.READ_EXTERNAL_STORAGE
                        //requestPermissions(permissions, PERMISSION_CODE)
                        permReqLauncher.launch(permission)
                        pickImage()
                    }
                    else {
                        //permission already granted
                        pickImage()
                    }
                }
                else {
                    //System OS is < Marshmallow
                    pickImage()
                }
            }
            R.id.btnGravarCarro -> {

                if (!this.validateData())
                    return

                try {
                    bmp = imgVwCarro.drawable.toBitmap()
                    vImage = context?.let {
                                        Autos()
                                    }?.uploadAutoImage(bmp, varTxtCHASSI.text.toString())
                    context?.let {
                                Autos()
                            }?.writeNewAuto(varTxtCHASSI.text.toString(),
                                            varTxtDescricaoVeiculo.text.toString(),
                                            vImage.toString(),
                                            varTxtMarcaModelo.text.toString())
                }
                catch (e: Exception) {
                    Log.d("AutoFragment", "Erro na Aplicacao..: \n" + "${e.printStackTrace()}")
                }
                navController.navigate(R.id.action_autoFragment_to_listAutoFragment)
            }

            R.id.btnCancelarCarro -> {
                navController.navigate(R.id.action_autoFragment_to_listAutoFragment)
            }
            R.id.btnDeleteAuto -> {
                val vkey = varTxtCHASSI.text.toString()

                if (vkey != "" && vkey != "null") {
                    context?.let { Autos() }?.deleteAuto(
                        varTxtCHASSI.text.toString()
                    )
                }
                navController.navigate(R.id.action_autoFragment_to_listAutoFragment)
            }

        }
    }

    // Method to draw a circle on a canvas and generate bitmap
    private fun drawCircle(bgColor:Int = Color.TRANSPARENT,
                           circleColor:Int = Color.WHITE,
                           width:Int = 200,
                           height:Int = 200): Bitmap {
        // Initialize a new Bitmap object
        val bitmap: Bitmap = Bitmap.createBitmap(
            width,
            height,
            Bitmap.Config.ARGB_8888 // Config
        )

        // Initialize a new Canvas instance
        val canvas = Canvas(bitmap)

        // Draw a solid color to the canvas background
        canvas.drawColor(bgColor)

        // Initialize a new Paint instance to draw the Circle
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = circleColor
        paint.isAntiAlias = true

        // Calculate the available radius of canvas
        val radius:Int = min(canvas.width,canvas.height/2)

        // Set a pixels value to padding around the circle
        val padding = 5

        // Finally, draw the circle on the canvas
        canvas.drawCircle(
            (canvas.width / 2).toFloat(), // cx
            (canvas.height / 2).toFloat(), // cy
            (radius - padding).toFloat(), // Radius
            paint
        )

        // Return the newly created bitmap
        return bitmap
    }

    private fun validateData(): Boolean {

        if (varTxtMarcaModelo.text.toString().isNullOrEmpty() ||
                varTxtMarcaModelo.text.toString().isNullOrBlank()) {
            Log.d("AutoFragment", getResources().getString(R.string.message_auto_input_model_vehicle))
            Toast.makeText(context, getResources().getString(R.string.message_auto_input_model_vehicle), Toast.LENGTH_LONG).show()
            return false
        }

        if (vImage.toString().isNullOrEmpty() ||
                vImage.toString().isNullOrBlank()) {
            Log.d("AutoFragment", getResources().getString(R.string.message_auto_input_image_vehicle))
            Toast.makeText(context, getResources().getString(R.string.message_auto_input_image_vehicle), Toast.LENGTH_LONG).show()
            return false
        }

        if (varTxtCHASSI.text.isNullOrEmpty() ||
                varTxtCHASSI.text.isNullOrBlank()) {
            Log.d("AutoFragment", getResources().getString(R.string.message_auto_input_chassi))
            Toast.makeText(context, getResources().getString(R.string.message_auto_input_chassi), Toast.LENGTH_LONG).show()
            return false
        }

        if (varTxtCHASSI.text.length < countValidationPassword) {
            Log.d("AutoFragment", getResources().getString(R.string.message_auto_input_chassi_valid))
            Toast.makeText(context, getResources().getString(R.string.message_auto_input_chassi_valid), Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}