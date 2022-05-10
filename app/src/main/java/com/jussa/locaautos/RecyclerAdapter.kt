package com.jussa.locaautos

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.jussa.locaautos.R.id
import com.jussa.locaautos.R.layout
import com.jussa.locaautos.data.DataAuto
import com.jussa.locaautos.ui.auto.Autos

class LocaAutosAdapter(private val parentlistAutos: ArrayList<DataAuto>, myAutoActivity: AutoActivity): RecyclerView.Adapter<LocaAutosAdapter.LocaAutosViewHolder>(), View.OnClickListener {

    private lateinit var myAutoViewHolder: LocaAutosViewHolder
    private var act: AutoActivity = myAutoActivity

    inner class LocaAutosViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemChassi: TextView? = itemView.findViewById(id.item_chassi)
        var itemImage: ImageView = itemView.findViewById(id.item_image)
        var itemDetails: TextView = itemView.findViewById(id.item_details)
        var itemTitle: TextView = itemView.findViewById(id.item_title)
        private var imgBtnExcluir: ImageButton = itemView.findViewById(id.btn_card_delete)

        init {
            itemView.setOnClickListener{
                this@LocaAutosAdapter.onClick(itemView, layoutPosition)
            }

            imgBtnExcluir.setOnClickListener {
                val vAutos = Autos()
                vAutos.deleteAuto(itemChassi?.text.toString())
                parentlistAutos.clear()
                //this@LocaAutosAdapter.notifyDataSetChanged()
            }
        }
    }

    fun onClick(p0: View?, pos: Int) {
            val context = p0!!.context
            val vUser = FirebaseAuth.getInstance().currentUser?.email

            val intent = Intent(context, AutoActivity::class.java ).apply{
                val listParentAutos = parentlistAutos
                val vauto = listParentAutos[pos]
                putExtra("Usuario", vUser)
                putExtra("Chassi", vauto.key_chassi)
                putExtra("Imagem",  vauto.imagem)
                putExtra("Descricao", vauto.descricao.toString())
                putExtra("MarcaModelo", vauto.marca_modelo.toString())
            }
       return startActivity(context, intent, intent.extras)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LocaAutosAdapter.LocaAutosViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(layout.card_layout, viewGroup, false)
        myAutoViewHolder = LocaAutosViewHolder(v)
        return myAutoViewHolder
    }

    override fun onBindViewHolder(holder: LocaAutosAdapter.LocaAutosViewHolder, position: Int) {
        val vAuto = parentlistAutos[position]
        val fbaseInstance = FirebaseStorage.getInstance() //.getReferenceFromUrl("gs://loca-auto-fiap.appspot.com")
        val fbaseStore = fbaseInstance.getReference("loca_autos_img/")

        try {
            fbaseStore.child(vAuto.key_chassi + ".jpg").downloadUrl
                .addOnSuccessListener {
                    Glide.with(holder.itemView.context)
                        .load(it.toString())
                        .into(holder.itemImage)
                }
                .addOnFailureListener{
                    Log.d("ERROR onFailureListener", "Erro Jussa..: ${it.printStackTrace()}")
                }
        }
        catch (e: Exception){
            Log.d("ERROR DRAWABLE", "Erro Drawable Jussa..: ${e.printStackTrace()}")
        }
        finally {
            holder.itemChassi?.text = vAuto.key_chassi
            holder.itemDetails.text = vAuto.descricao
            holder.itemTitle.text = vAuto.marca_modelo
        }
    }

    override fun getItemCount(): Int {
        return parentlistAutos.size
    }

    override fun onClick(p0: View?) {
    }
}