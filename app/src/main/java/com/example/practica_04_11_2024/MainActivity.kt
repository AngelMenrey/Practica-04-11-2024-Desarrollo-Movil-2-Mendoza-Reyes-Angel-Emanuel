package com.example.practica_04_11_2024

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import java.sql.SQLException

class MainActivity : AppCompatActivity() {

    private lateinit var etNumEmp: EditText
    private lateinit var etNombre: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etSueldo: EditText
    private lateinit var btnAgregar: ImageButton
    private lateinit var btnBuscar: ImageButton
    private lateinit var btnActualizar: ImageButton
    private lateinit var btnEliminar: ImageButton
    private lateinit var btnLista: Button

    private lateinit var admin: ControladorBD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etNumEmp = findViewById(R.id.editTextNumEmp)
        etNombre = findViewById(R.id.editTextNombre)
        etApellidos = findViewById(R.id.editTextApellidos)
        etSueldo = findViewById(R.id.editTextSueldo)
        btnAgregar = findViewById(R.id.imageButtonAgregar)
        btnBuscar = findViewById(R.id.imageButtonBuscar)
        btnActualizar = findViewById(R.id.imageButtonActualizar)
        btnEliminar = findViewById(R.id.imageButtonEliminar)
        btnLista = findViewById(R.id.buttonLista)

        admin = ControladorBD(this, "GestorEmpleados.db", null, 1)

        btnAgregar.setOnClickListener { registrarEmpleado() }
        btnBuscar.setOnClickListener { buscarEmpleado() }
        btnActualizar.setOnClickListener { actualizarEmpleado() }
        btnEliminar.setOnClickListener { eliminarEmpleado() }
        btnLista.setOnClickListener { listarRegistro() }
    }

    private fun listarRegistro() {
        val intent = Intent(this, Listado::class.java)
        startActivity(intent)
    }

    private fun eliminarEmpleado() {
        val bd = admin.writableDatabase
        val nump = etNumEmp.text.toString()
        if (nump.isNotEmpty()) {
            val cantidad = bd.delete("empleado", "numep = ?", arrayOf(nump))
            bd.close()
            etNumEmp.setText("")
            etNombre.setText("")
            etApellidos.setText("")
            etSueldo.setText("")
            etNumEmp.requestFocus()
            if (cantidad > 0) Toast.makeText(this, "Empleado Eliminado", Toast.LENGTH_SHORT).show() else Toast.makeText(this, "El numero de empleado no existe", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, " Ingresa numero de empleado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarEmpleado() {
        val bd = admin.writableDatabase
        val nump = etNumEmp.text.toString()
        val nomp = etNombre.text.toString()
        val apep = etApellidos.text.toString()
        val suep = etSueldo.text.toString()
        if (nump.isNotEmpty() && nomp.isNotEmpty() && apep.isNotEmpty() && suep.isNotEmpty()) {
            val registro = ContentValues()
            registro.put("numep", nump)
            registro.put("nombre", nomp)
            registro.put("apellidos", apep)
            registro.put("sueldo", suep)
            val cantidad = bd.update("empleado", registro, "numep=?", arrayOf(nump))
            bd.close()
            etNumEmp.setText("")
            etNombre.setText("")
            etApellidos.setText("")
            etSueldo.setText("")
            etNumEmp.requestFocus()
            if (cantidad > 0) Toast.makeText(this, "Datos del empleado actualizado", Toast.LENGTH_SHORT).show() else Toast.makeText(this, "El numero de empleado no existe", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Debes registrar primero los datos ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buscarEmpleado() {
        val bd = admin.readableDatabase
        val nump = etNumEmp.text.toString()
        if (nump.isNotEmpty()) {
            val fila = bd.rawQuery("select nombre, apellidos, sueldo from empleado where numep =?", arrayOf(nump))
            if (fila.moveToFirst()) {
                etNombre.setText(fila.getString(0))
                etApellidos.setText(fila.getString(1))
                etSueldo.setText(fila.getString(2))
                bd.close()
            } else {
                Toast.makeText(this, "Numero de empleado no existe", Toast.LENGTH_SHORT).show()
                etNumEmp.setText("")
                etNumEmp.requestFocus()
                bd.close()
            }
        } else {
            Toast.makeText(this, "Ingresa numero de empleado", Toast.LENGTH_SHORT).show()
            etNumEmp.requestFocus()
        }
    }

    private fun registrarEmpleado() {
        val bd = admin.writableDatabase
        val nump = etNumEmp.text.toString()
        val nomp = etNombre.text.toString()
        val apep = etApellidos.text.toString()
        val suep = etSueldo.text.toString()
        if (nump.isNotEmpty() && nomp.isNotEmpty() && apep.isNotEmpty() && suep.isNotEmpty()) {
            val registro = ContentValues()
            registro.put("numep", nump)
            registro.put("nombre", nomp)
            registro.put("apellidos", apep)
            registro.put("sueldo", suep)
            if (bd != null) {
                try {
                    val x: Long = bd.insert("empleado", null, registro)
                } catch (e: SQLException) {
                    Toast.makeText(this, "Error al registrar empleado", Toast.LENGTH_SHORT).show()
                }
                bd.close()
            }
            etNumEmp.setText("")
            etNombre.setText("")
            etApellidos.setText("")
            etSueldo.setText("")
            etNumEmp.requestFocus()
            Toast.makeText(this, "Empleado registrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Debes registrar primero los datos ", Toast.LENGTH_SHORT).show()
        }
    }
}