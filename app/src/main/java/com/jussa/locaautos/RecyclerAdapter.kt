package com.jussa.locaautos

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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

        var itemChassi: TextView = itemView.findViewById(id.item_chassi)
        var itemImage: ImageView = itemView.findViewById(id.item_image)
        var itemDetails: TextView = itemView.findViewById(id.item_details)
        var itemTitle: TextView = itemView.findViewById(id.item_title)

        init {

            Toast.makeText(itemView.context, "Chassi: ${itemChassi.text}", Toast.LENGTH_SHORT).show()

            itemView.setOnClickListener {
                val position: Int = absoluteAdapterPosition
                val context = itemView.context
                val vUser = FirebaseAuth.getInstance().currentUser?.email

                val intent = Intent(context, AutoActivity::class.java).apply {
/*
                    putExtra("Usuario", vUser)
                    putExtra("Chassi", parentlistAutos[position].key_chassi.toString())
                    putExtra("Imagem",  parentlistAutos[position].imagem.toString())
                    putExtra("Descricao", parentlistAutos[position].descricao.toString())
                    putExtra("MarcaModelo", parentlistAutos[position].marca_modelo.toString())
*/
                    putExtra("Usuario", vUser)
                    putExtra("Chassi", itemChassi.text)
                    putExtra("Imagem",  "itemImage")
                    putExtra("Descricao", parentlistAutos[position].descricao.toString())
                    putExtra("MarcaModelo", parentlistAutos[position].marca_modelo.toString())


                }
                startActivity(context, intent, intent.extras)
            }
        }
    } /*Inner Class*/
    
    
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val vAuto = parentlistAutos[position]

        holder.itemChassi.text = vAuto.key_chassi
        holder.itemImage.setImageResource(drawable.car_mini)
        holder.itemDetails.text = vAuto.descricao
        holder.itemTitle.text = vAuto.marca_modelo

    }

    override fun getItemCount(): Int {
        //return titles.size
        return parentlistAutos.size
    }

}/*PARENT CLASS*/