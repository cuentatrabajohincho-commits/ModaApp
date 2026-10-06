package com.hincho.modaapp.model

data class DetallePedido(
    val id: Int = 0,
    val idPedido: Int = 0,
    val ropa: Ropa,
    var cantidad: Int,
    val precioUnitario: Double
) {
    val subtotal: Double
        get() = cantidad * precioUnitario
}