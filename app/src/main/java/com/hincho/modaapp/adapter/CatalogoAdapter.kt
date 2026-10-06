package com.hincho.modaapp.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hincho.modaapp.databinding.ItemCatalogoBinding
import com.hincho.modaapp.model.Ropa
import java.io.File

class CatalogoAdapter(private var lista: List<Ropa>) : RecyclerView.Adapter<CatalogoAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCatalogoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCatalogoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.tvItemModelo.text = item.modelo
        holder.binding.tvItemDetalle.text = "Talla ${item.talla} · ${item.color}"
        holder.binding.tvItemPrecio.text = "S/ ${String.format("%.2f", item.precio)}"

        if (item.foto.isNotEmpty() && File(item.foto).exists()) {
            holder.binding.ivItemFoto.setImageBitmap(BitmapFactory.decodeFile(item.foto))
        } else {
            holder.binding.ivItemFoto.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<Ropa>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}