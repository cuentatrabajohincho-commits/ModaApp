package com.hincho.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hincho.modaapp.data.DBHelper
import com.hincho.modaapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

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
                // Validación con SQLite (HU-04)
                val userObj = dbHelper.validarUsuario(usuario, clave)
                if (userObj != null) {
                    val intent = Intent(this, MenuActivity::class.java).apply {
                        putExtra("USUARIO_NOMBRE", userObj.usuario)
                        putExtra("USUARIO_ROL", userObj.rol)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, R.string.err_credenciales, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnVerCatalogo.setOnClickListener {
            startActivity(Intent(this, CatalogoActivity::class.java))
        }
    }
}