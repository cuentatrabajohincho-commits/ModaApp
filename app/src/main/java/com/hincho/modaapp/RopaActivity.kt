package com.hincho.modaapp

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hincho.modaapp.data.DBHelper
import com.hincho.modaapp.databinding.ActivityRopaBinding
import com.hincho.modaapp.model.Categoria
import com.hincho.modaapp.model.Ropa
import java.io.File
import java.io.FileOutputStream

class RopaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRopaBinding
    private lateinit var dbHelper: DBHelper
    private var rutaFotoGuardada: String = ""
    private var listaCategorias: List<Categoria> = emptyList()

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val file = File(filesDir, "ropa_${System.currentTimeMillis()}.jpg")
            contentResolver.openInputStream(it)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            rutaFotoGuardada = file.absolutePath
            binding.ivFoto.setImageBitmap(BitmapFactory.decodeFile(rutaFotoGuardada))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRopaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        // Cargar Categorías
        listaCategorias = dbHelper.obtenerCategorias()
        val adapterCat = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaCategorias)
        binding.spCategoria.adapter = adapterCat

        // Cargar Tallas
        val tallas = arrayOf("XS", "S", "M", "L", "XL")
        val adapterTallas = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tallas)
        binding.spTalla.adapter = adapterTallas

        binding.btnElegirFoto.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        binding.btnGuardar.setOnClickListener {
            guardarRopa()
        }
    }

    private fun guardarRopa() {
        val modelo = binding.etModelo.text.toString().trim()
        val marca = binding.etMarca.text.toString().trim()
        val color = binding.etColor.text.toString().trim()
        val cantidadStr = binding.etCantidad.text.toString().trim()
        val precioStr = binding.etPrecio.text.toString().trim()

        if (modelo.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty() || rutaFotoGuardada.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos obligatorios y seleccione una foto", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidad = cantidadStr.toIntOrNull() ?: 0
        val precio = precioStr.toDoubleOrNull() ?: 0.0

        if (precio <= 0 || cantidad < 0) {
            Toast.makeText(this, "Precio y cantidad inválidos", Toast.LENGTH_SHORT).show()
            return
        }

        val categoriaSeleccionada = binding.spCategoria.selectedItem as Categoria
        val tallaSeleccionada = binding.spTalla.selectedItem.toString()

        val nuevaRopa = Ropa(
            modelo = modelo,
            idCategoria = categoriaSeleccionada.id,
            talla = tallaSeleccionada,
            marca = marca,
            color = color,
            precio = precio,
            cantidad = cantidad,
            foto = rutaFotoGuardada
        )

        if (dbHelper.insertarRopa(nuevaRopa)) {
            Toast.makeText(this, "Prenda registrada correctamente", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al registrar la prenda", Toast.LENGTH_SHORT).show()
        }
    }
}