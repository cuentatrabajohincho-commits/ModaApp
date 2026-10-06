package com.hincho.modaapp.model

data class Pedido(
    val id: Int = 0,
    val clienteNombre: String,
    val clienteTelefono: String,
    val fecha: String,
    val total: Double,
    val estado: String = "PENDIENTE"
)