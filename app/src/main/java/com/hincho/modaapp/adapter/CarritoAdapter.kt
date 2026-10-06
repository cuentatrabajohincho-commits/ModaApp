package com.hincho.modaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hincho.modaapp.databinding.ItemCarritoBinding
import com.hincho.modaapp.model.DetallePedido

class CarritoAdapter(private val lista: List<DetallePedido>) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCarritoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.tvNombreProd.text = item.ropa.modelo
        holder.binding.tvPrecioUnit.text = "Precio: S/ ${String.format("%.2f", item.precioUnitario)}"
        holder.binding.tvCantidad.text = "x${item.cantidad}"
        holder.binding.tvSubtotal.text = "S/ ${String.format("%.2f", item.subtotal)}"
    }

    override fun getItemCount(): Int = lista.size
}