package com.hincho.modaapp.data

import com.hincho.modaapp.model.DetallePedido
import com.hincho.modaapp.model.Ropa

object CarritoManager {
    val items = mutableListOf<DetallePedido>()

    fun agregarRopa(ropa: Ropa) {
        val existente = items.find { it.ropa.id == ropa.id }
        if (existente != null) {
            if (existente.cantidad < ropa.cantidad) {
                existente.cantidad++
            }
        } else {
            items.add(DetallePedido(ropa = ropa, cantidad = 1, precioUnitario = ropa.precio))
        }
    }

    fun obtenerTotal(): Double {
        return items.sumOf { it.subtotal }
    }

    fun limpiar() {
        items.clear()
    }
}