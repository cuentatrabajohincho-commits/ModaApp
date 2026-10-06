package com.hincho.modaapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hincho.modaapp.model.Categoria
import com.hincho.modaapp.model.DetallePedido
import com.hincho.modaapp.model.Pedido
import com.hincho.modaapp.model.Ropa
import com.hincho.modaapp.model.Usuario

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "modaapp.db"
        private const val DATABASE_VERSION = 2
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla usuario (HU-04)
        db.execSQL(
            """
            CREATE TABLE usuario (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT UNIQUE NOT NULL,
                clave TEXT NOT NULL,
                rol TEXT NOT NULL,
                telefono TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Tabla categoria (HU-05)
        db.execSQL(
            """
            CREATE TABLE categoria (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT UNIQUE NOT NULL
            )
            """.trimIndent()
        )

        // Tabla ropa (HU-05)
        db.execSQL(
            """
            CREATE TABLE ropa (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                modelo TEXT NOT NULL,
                id_categoria INTEGER NOT NULL,
                talla TEXT NOT NULL,
                marca TEXT,
                color TEXT,
                precio REAL CHECK(precio > 0),
                cantidad INTEGER CHECK(cantidad >= 0),
                foto TEXT,
                FOREIGN KEY (id_categoria) REFERENCES categoria(id)
            )
            """.trimIndent()
        )

        // Insertar usuario admin inicial
        db.execSQL("INSERT INTO usuario (usuario, clave, rol, telefono) VALUES ('admin', '1234', 'ADMIN', '999888777')")

        // Insertar categorías precargadas
        val categorias = listOf("Polos", "Pantalones", "Vestidos", "Casacas", "Zapatillas")
        for (cat in categorias) {
            db.execSQL("INSERT INTO categoria (nombre) VALUES ('$cat')")
        }

        // Insertar prendas de vestir demo
        db.execSQL("INSERT INTO ropa (modelo, id_categoria, talla, marca, color, precio, cantidad, foto) VALUES ('Polo Oversize Urban', 1, 'M', 'Nike', 'Negro', 69.90, 15, '')")
        db.execSQL("INSERT INTO ropa (modelo, id_categoria, talla, marca, color, precio, cantidad, foto) VALUES ('Polo Basic Cotton', 1, 'S', 'Adidas', 'Blanco', 45.00, 20, '')")
        db.execSQL("INSERT INTO ropa (modelo, id_categoria, talla, marca, color, precio, cantidad, foto) VALUES ('Jeans Slim Fit', 2, '30', 'Levis', 'Azul Oscuro', 129.90, 8, '')")
        db.execSQL("INSERT INTO ropa (modelo, id_categoria, talla, marca, color, precio, cantidad, foto) VALUES ('Pantalon Cargo Street', 2, 'L', 'Puma', 'Beige', 99.00, 12, '')")
        db.execSQL("INSERT INTO ropa (modelo, id_categoria, talla, marca, color, precio, cantidad, foto) VALUES ('Vestido Floral Verano', 3, 'S', 'Zara', 'Rojo', 89.90, 6, '')")
        db.execSQL("INSERT INTO ropa (modelo, id_categoria, talla, marca, color, precio, cantidad, foto) VALUES ('Casaca De Jean Classic', 4, 'M', 'Tommy', 'Azul', 159.00, 5, '')")
        db.execSQL("INSERT INTO ropa (modelo, id_categoria, talla, marca, color, precio, cantidad, foto) VALUES ('Zapatillas Retro Runner', 5, '41', 'Puma', 'Blanco/Gris', 219.90, 10, '')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL(
                """
            CREATE TABLE pedido (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                cliente_nombre TEXT NOT NULL,
                cliente_telefono TEXT NOT NULL,
                fecha TEXT NOT NULL,
                total REAL NOT NULL,
                estado TEXT NOT NULL
            )
            """.trimIndent()
            )

            db.execSQL(
                """
            CREATE TABLE detalle_pedido (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                id_pedido INTEGER NOT NULL,
                id_ropa INTEGER NOT NULL,
                cantidad INTEGER NOT NULL,
                precio_unitario REAL NOT NULL,
                FOREIGN KEY (id_pedido) REFERENCES pedido(id),
                FOREIGN KEY (id_ropa) REFERENCES ropa(id)
            )
            """.trimIndent()
            )
        }
    }

    // --- Métodos HU-04: Usuario ---
    fun validarUsuario(usuario: String, clave: String): Usuario? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, usuario, clave, rol, telefono FROM usuario WHERE usuario = ? AND clave = ?",
            arrayOf(usuario, clave)
        )
        var user: Usuario? = null
        if (cursor.moveToFirst()) {
            user = Usuario(
                id = cursor.getInt(0),
                usuario = cursor.getString(1),
                clave = cursor.getString(2),
                rol = cursor.getString(3),
                telefono = cursor.getString(4)
            )
        }
        cursor.close()
        return user
    }

    // --- Métodos HU-05: Categorías y Ropa ---
    fun obtenerCategorias(): List<Categoria> {
        val lista = mutableListOf<Categoria>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, nombre FROM categoria ORDER BY id ASC", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(Categoria(cursor.getInt(0), cursor.getString(1)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun insertarRopa(ropa: Ropa): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("modelo", ropa.modelo)
            put("id_categoria", ropa.idCategoria)
            put("talla", ropa.talla)
            put("marca", ropa.marca)
            put("color", ropa.color)
            put("precio", ropa.precio)
            put("cantidad", ropa.cantidad)
            put("foto", ropa.foto)
        }
        val result = db.insert("ropa", null, values)
        return result != -1L
    }

    fun listarRopa(): List<Ropa> {
        val lista = mutableListOf<Ropa>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, modelo, id_categoria, talla, marca, color, precio, cantidad, foto FROM ropa ORDER BY id DESC", null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Ropa(
                        id = cursor.getInt(0),
                        modelo = cursor.getString(1),
                        idCategoria = cursor.getInt(2),
                        talla = cursor.getString(3),
                        marca = cursor.getString(4),
                        color = cursor.getString(5),
                        precio = cursor.getDouble(6),
                        cantidad = cursor.getInt(7),
                        foto = cursor.getString(8) ?: ""
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    // --- Métodos HU-06: Catálogo ---
    fun listarRopaDisponibles(idCategoria: Int = 0): List<Ropa> {
        val lista = mutableListOf<Ropa>()
        val db = readableDatabase
        val query = if (idCategoria == 0) {
            "SELECT id, modelo, id_categoria, talla, marca, color, precio, cantidad, foto FROM ropa WHERE cantidad > 0 ORDER BY id DESC"
        } else {
            "SELECT id, modelo, id_categoria, talla, marca, color, precio, cantidad, foto FROM ropa WHERE cantidad > 0 AND id_categoria = $idCategoria ORDER BY id DESC"
        }
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Ropa(
                        id = cursor.getInt(0),
                        modelo = cursor.getString(1),
                        idCategoria = cursor.getInt(2),
                        talla = cursor.getString(3),
                        marca = cursor.getString(4),
                        color = cursor.getString(5),
                        precio = cursor.getDouble(6),
                        cantidad = cursor.getInt(7),
                        foto = cursor.getString(8) ?: ""
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    // Metodo para guardar pedido completo y descontar stock
    fun guardarPedido(pedido: Pedido, detalles: List<DetallePedido>): Boolean {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val valuesPedido = ContentValues().apply {
                put("cliente_nombre", pedido.clienteNombre)
                put("cliente_telefono", pedido.clienteTelefono)
                put("fecha", pedido.fecha)
                put("total", pedido.total)
                put("estado", pedido.estado)
            }
            val idPedido = db.insert("pedido", null, valuesPedido)
            if (idPedido == -1L) return false

            for (det in detalles) {
                val valuesDet = ContentValues().apply {
                    put("id_pedido", idPedido)
                    put("id_ropa", det.ropa.id)
                    put("cantidad", det.cantidad)
                    put("precio_unitario", det.precioUnitario)
                }
                db.insert("detalle_pedido", null, valuesDet)

                // Descontar Stock
                db.execSQL(
                    "UPDATE ropa SET cantidad = cantidad - ? WHERE id = ?",
                    arrayOf(det.cantidad, det.ropa.id)
                )
            }

            db.setTransactionSuccessful()
            return true
        } catch (e: Exception) {
            return false
        } finally {
            db.endTransaction()
        }
    }
}