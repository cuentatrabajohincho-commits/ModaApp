package com.hincho.modaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.hincho.modaapp.adapter.CatalogoAdapter
import com.hincho.modaapp.data.DBHelper
import com.hincho.modaapp.databinding.ActivityCatalogoBinding

class CatalogoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogoBinding
    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: CatalogoAdapter
    private var idCategoriaSeleccionada: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        adapter = CatalogoAdapter(emptyList())
        binding.rvCatalogo.layoutManager = GridLayoutManager(this, 2)
        binding.rvCatalogo.adapter = adapter

        // Evento para abrir el Carrito
        binding.fabCarrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            startActivity(intent)
        }

        cargarChipsCategorias()
        cargarCatalogo(0)
    }

    override fun onResume() {
        super.onResume()
        // Recargar prendas al volver del carrito por si cambió el stock
        cargarCatalogo(idCategoriaSeleccionada)
    }

    private fun cargarChipsCategorias() {
        binding.chipGroupCategorias.removeAllViews()

        val chipTodas = Chip(this).apply {
            text = "Todas"
            isCheckable = true
            isChecked = true
            setOnClickListener {
                idCategoriaSeleccionada = 0
                cargarCatalogo(0)
            }
        }
        binding.chipGroupCategorias.addView(chipTodas)

        val categorias = dbHelper.obtenerCategorias()
        for (cat in categorias) {
            val chip = Chip(this).apply {
                text = cat.nombre
                isCheckable = true
                setOnClickListener {
                    idCategoriaSeleccionada = cat.id
                    cargarCatalogo(cat.id)
                }
            }
            binding.chipGroupCategorias.addView(chip)
        }
    }

    private fun cargarCatalogo(idCategoria: Int) {
        val lista = dbHelper.listarRopaDisponibles(idCategoria)
        adapter.actualizarLista(lista)
    }
}