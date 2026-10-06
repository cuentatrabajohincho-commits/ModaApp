package com.hincho.modaapp.model

data class Categoria(
    val id: Int,
    val nombre: String
) {
    override fun toString(): String = nombre
}