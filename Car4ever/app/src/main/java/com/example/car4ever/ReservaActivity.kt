package com.example.car4ever

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ReservaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserva)

        val txvModelo:TextView = findViewById<TextView>(R.id.txtReservaResultado)
        val modeloSharedPreferences:SharedPreferences = this.getSharedPreferences("Pref", MODE_PRIVATE)
        val nome: String? = modeloSharedPreferences.getString("nome","")
       val modelo: String? = modeloSharedPreferences.getString("modelo","")
        txvModelo.text = nome + "\n" + modelo

    }

}