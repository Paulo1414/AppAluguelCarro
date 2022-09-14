package com.example.car4ever

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lateinit var opcoes : Spinner

        lateinit var model : Intent

        /* val btnEnviar:Button = findViewById<Button>(R.id.btnEnviar)
         val edNome:EditText = findViewById<EditText>(R.id.etMensagem1)
         val edSobrenome:EditText = findViewById<EditText>(R.id.etMensagem2)*/

       // val txtResultado: TextView = findViewById<TextView>(R.id.txtResultado)
        val opcoesTela = arrayOf("Selecione", "Inicial", "Modelos", "Encontre - nos")

        var home = Intent(this, MainActivity::class.java)
        var modelo = Intent(this, Modelos::class.java)
        var nossaLocalizacao = Intent(this, LocalizacaoCar::class.java)
        opcoes = findViewById(R.id.spopcoes) as Spinner

        opcoes.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opcoesTela)


        opcoes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position : Int, id: Long) {
               // txtResultado.text = opcoesTela.get(position)
                if (position.equals(1)){
                    startActivity(home)

                }else if (position.equals(2)){
                   startActivity(modelo)

                }else if(position.equals(3)){
                    startActivity(nossaLocalizacao)

                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

/*
        btnEnviar.setOnClickListener{
            val nome: String = edNome.editableText.toString()
            txtResultado.text = nome


            }*/



    }

    fun onClickaqui(view: View) {
        Toast.makeText(this, "foi clicado!", Toast.LENGTH_SHORT).show()
    }

}