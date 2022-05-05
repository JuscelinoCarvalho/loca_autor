package com.jussa.locaautos


import android.content.Intent
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.jussa.locaautos.R.*
import com.jussa.locaautos.data.DataAuto

class RecyclerAdapter(private val parentlistAutos: ArrayList<DataAuto>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    /*
        private var titles = arrayOf("Carro Esportivo", "Mini Carro", "Carro Popular", "Carro Eletrico",
            "Carro Muscle Car", "Carro SUV", "Carro Truck ou Caminhonete", "Carro Onibus",
            "Carro Esportivo", "Mini Carro", "Carro Popular", "Carro Eletrico",
            "Carro Muscle Car", "Carro SUV", "Carro Truck ou Caminhonete", "Carro Onibus")


        private var descriptions = arrayOf("Carro esportivo muito potentente", "MiniCarro para uso em metrop.",
            "Carro popular GOL VolksWagen",
            "Nova tendencia de mercado",
            "Os Muscle Cars ",
            "SUVs ainda estao como pref.",
            "Trucks ou Caminhonetes",
            "Transportam muitas pess.",
            "Carro esportivo muito potentente", "MiniCarro para uso em metrop.",
            "Carro popular GOL VolksWagen",
            "Nova tendencia de mercado",
            "Os Muscle Cars ",
            "SUVs ainda estao como pref.",
            "Trucks ou Caminhonetes",
            "Transportam muitas pess."
            )
        private var images = intArrayOf(
            drawable.car_maseratti, drawable.car_mini, drawable.car_gol,
            drawable.car_gol, drawable.car_mini, drawable.car_maseratti,
            drawable.car_gol, drawable.car_mini,
            drawable.car_maseratti, drawable.car_mini, drawable.car_gol,
            drawable.car_gol, drawable.car_mini, drawable.car_maseratti,
            drawable.car_gol, drawable.car_mini
            )
    */


    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var itemChassi: TextView? = itemView.findViewById(id.item_chassi)
        var itemImage: ImageView = itemView.findViewById(id.item_image)
        var itemDetails: TextView = itemView.findViewById(id.item_details)
        var itemTitle: TextView = itemView.findViewById(id.item_title)

        private var imgChecked: ImageView = itemView.findViewById(id.auto_checked)
        private var btnDeleteItens: Button? = itemView.findViewById(id.btnDeleteItens)

        private var selecteds = ArrayList<Int>()


        init {
            //Toast.makeText(itemView.context, "Chassi: ${itemChassi.text}", Toast.LENGTH_SHORT).show()
            val listParentAutos = parentlistAutos

            btnDeleteItens?.setOnClickListener {
                //layoutPosition
            }

            itemView.setOnClickListener {
                val position: Int = absoluteAdapterPosition
                val context = itemView.context
                val vUser = FirebaseAuth.getInstance().currentUser?.email
                val intent = Intent(context, AutoActivity::class.java).apply {
                val vauto = listParentAutos[position]
/*
                    putExtra("Usuario", vUser)
                    putExtra("Chassi", parentlistAutos[position].key_chassi.toString())
                    putExtra("Imagem",  parentlistAutos[position].imagem.toString())
                    putExtra("Descricao", parentlistAutos[position].descricao.toString())
                    putExtra("MarcaModelo", parentlistAutos[position].marca_modelo.toString())
*/
                    putExtra("Usuario", vUser)
                    putExtra("Chassi", vauto.key_chassi) //extras?.getString("chassi"))
                    putExtra("Imagem",  vauto.imagem)
                    putExtra("Descricao", vauto.descricao.toString())
                    putExtra("MarcaModelo", vauto.marca_modelo.toString())


                }
                startActivity(context, intent, intent.extras)
            }

            //Vinculo o listener de Long Click na View do Recycler atual
             itemView.setOnLongClickListener {

                 val position: Int = absoluteAdapterPosition
                 val context = itemView.context

                 val intent = Intent(it.context, AutoActivity::class.java).apply {
                     itemChassi?.text = extras?.getString("chassi")
                 }

                 if (it != null){
                    onItemLongClicked(layoutPosition)
                     if (selecteds.count() > 0){
                         btnDeleteItens?.visibility = VISIBLE
                     }else{
                         btnDeleteItens?.visibility = INVISIBLE
                     }
                    if (itemChassi == null){

                    }
                     Toast.makeText(it.context, "TESTE JK!!...${intent.extras}", Toast.LENGTH_SHORT).show()
                 return@setOnLongClickListener true
                 }else{
                     return@setOnLongClickListener false
                 }
             }

        }


        /*private fun onItemClicked(position: Int){
            if (position >= 0) {
                imgChecked.visibility = VISIBLE
            }
        }*/

        private fun onItemLongClicked(position: Int){
            if (position >= 0) {
                if(imgChecked.visibility == VISIBLE){
                    imgChecked.visibility = INVISIBLE
                    selecteds.remove(position)
                }else{
                    imgChecked.visibility = VISIBLE
                    selecteds.add(position)
                }
            }
        }


    } ////*Inner Class*/


    
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(layout.card_layout, viewGroup, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
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
        }catch (e: Exception){
            Log.d("ERROR DRAWABLE", "Erro Drawable Jussa..: ${e.printStackTrace()}")
        }finally {
            holder.itemChassi?.text = vAuto.key_chassi
            holder.itemDetails.text = vAuto.descricao
            holder.itemTitle.text = vAuto.marca_modelo
        }

    }

    override fun getItemCount(): Int {
        //return titles.size
        return parentlistAutos.size
    }

}/*PARENT CLASS*/