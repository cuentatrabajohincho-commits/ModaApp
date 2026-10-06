package com.hincho.modaapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hincho.modaapp.databinding.ItemPedidoBinding
import com.hincho.modaapp.model.Pedido

class PedidoAdapter(private val lista: List<Pedido>) : RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPedidoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.tvCliente.text = "Pedido #${item.id} - ${item.clienteNombre}"
        holder.binding.tvTelefono.text = "Tel: ${item.clienteTelefono}"
        holder.binding.tvFecha.text = "Fecha: ${item.fecha}"
        holder.binding.tvTotalPedido.text = "S/ ${String.format("%.2f", item.total)}"
    }

    override fun getItemCount(): Int = lista.size
}