package com.hincho.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hincho.modaapp.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMenuRopa.setOnClickListener {
            startActivity(Intent(this, RopaActivity::class.java))
        }

        binding.btnMenuPedidos.setOnClickListener {
            startActivity(Intent(this, PedidosActivity::class.java))
        }

        binding.btnMenuClientes.setOnClickListener {
            startActivity(Intent(this, ClientesActivity::class.java))
        }

        binding.btnMenuReportes.setOnClickListener {
            startActivity(Intent(this, ReportesActivity::class.java))
        }

        binding.btnSalir.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}