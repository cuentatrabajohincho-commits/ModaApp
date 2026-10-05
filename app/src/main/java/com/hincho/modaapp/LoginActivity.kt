package com.hincho.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hincho.modaapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIngresar.setOnClickListener {
            val usuario = binding.etUsuario.text.toString().trim()
            val clave = binding.etClave.text.toString().trim()

            var valido = true

            if (usuario.isEmpty()) {
                binding.tilUsuario.error = getString(R.string.err_campo_requerido)
                valido = false
            } else {
                binding.tilUsuario.error = null
            }

            if (clave.isEmpty()) {
                binding.tilClave.error = getString(R.string.err_campo_requerido)
                valido = false
            } else {
                binding.tilClave.error = null
            }

            if (valido) {
                // Validación estática para el Sprint 1
                if (usuario == "admin" && clave == "1234") {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra el login para no regresar con 'Atrás'
                } else {
                    Toast.makeText(this, R.string.err_credenciales, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnVerCatalogo.setOnClickListener {
            val intent = Intent(this, CatalogoActivity::class.java)
            startActivity(intent)
        }
    }
}