package com.hincho.modaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hincho.modaapp.adapter.PedidoAdapter
import com.hincho.modaapp.data.DBHelper
import com.hincho.modaapp.databinding.ActivityReportesBinding

class ReportesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportesBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        cargarResumen()
        cargarListaPedidos()
    }

    private fun cargarResumen() {
        val totalVentas = dbHelper.obtenerTotalVentas()
        binding.tvTotalAcumulado.text = "S/ ${String.format("%.2f", totalVentas)}"
    }

    private fun cargarListaPedidos() {
        val pedidos = dbHelper.obtenerPedidos()
        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = PedidoAdapter(pedidos)
    }
}