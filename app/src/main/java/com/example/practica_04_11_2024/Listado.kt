package com.example.practica_04_11_2024

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Listado : AppCompatActivity() {

    private lateinit var etListado: EditText
    private lateinit var btnRegresar: Button
    private lateinit var admin: ControladorBD

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado)

        etListado = findViewById(R.id.editDetalle)
        btnRegresar = findViewById(R.id.btnRegresar)
        admin = ControladorBD(this, "GestorEmpleados.db", null, 1)
        val bd = admin.readableDatabase
        val registro = bd.rawQuery("select * from empleado order by numep", null)
        val n = registro.count
        var nr = 0
        if (n > 0) {
            registro.moveToFirst()
            etListado.text.insert(0, "")
            do {
                etListado.text.insert(nr, "Numero:  ${registro.getString(0)}\nNombre: ${registro.getString(1)}\nApellidos: ${registro.getString(2)}\nSueldo: ${registro.getString(3)}\n")
                nr++
            } while (registro.moveToNext())
        } else {
            Toast.makeText(this, "Sin registro de empleados", Toast.LENGTH_SHORT).show()
        }
        bd.close()

        btnRegresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}