package com.hincho.modaapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hincho.modaapp.adapter.CarritoAdapter
import com.hincho.modaapp.data.CarritoManager
import com.hincho.modaapp.data.DBHelper
import com.hincho.modaapp.databinding.ActivityCarritoBinding
import com.hincho.modaapp.model.Pedido
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        binding.rvCarrito.layoutManager = LinearLayoutManager(this)
        binding.rvCarrito.adapter = CarritoAdapter(CarritoManager.items)

        actualizarTotal()

        binding.btnConfirmarPedido.setOnClickListener {
            confirmarPedido()
        }
    }

    private fun actualizarTotal() {
        val total = CarritoManager.obtenerTotal()
        binding.tvTotal.text = "S/ ${String.format("%.2f", total)}"
    }

    private fun confirmarPedido() {
        val nombre = binding.etNombre.text.toString().trim()
        val telefono = binding.etTelefono.text.toString().trim()

        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Ingrese el nombre y teléfono del cliente", Toast.LENGTH_SHORT).show()
            return
        }

        if (CarritoManager.items.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        val total = CarritoManager.obtenerTotal()

        val nuevoPedido = Pedido(
            clienteNombre = nombre,
            clienteTelefono = telefono,
            fecha = fecha,
            total = total
        )

        if (dbHelper.guardarPedido(nuevoPedido, CarritoManager.items)) {
            enviarWhatsApp(nombre, telefono, total)
            CarritoManager.limpiar()
            Toast.makeText(this, "Pedido registrado con éxito", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar el pedido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enviarWhatsApp(cliente: String, telefono: String, total: Double) {
        val mensaje = StringBuilder()
        mensaje.append("¡Hola $cliente! Confirmamos tu pedido en ModaApp:\n\n")
        for (item in CarritoManager.items) {
            mensaje.append("• ${item.ropa.modelo} (x${item.cantidad}) - S/ ${String.format("%.2f", item.subtotal)}\n")
        }
        mensaje.append("\n*Total: S/ ${String.format("%.2f", total)}*")

        try {
            val url = "https://api.whatsapp.com/send?phone=51$telefono&text=" + URLEncoder.encode(mensaje.toString(), "UTF-8")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}