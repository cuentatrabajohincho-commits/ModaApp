package com.hincho.modaapp.model

data class Ropa(
    val id: Int = 0,
    val modelo: String,
    val idCategoria: Int,
    val talla: String,
    val marca: String,
    val color: String,
    val precio: Double,
    val cantidad: Int,
    val foto: String
)