package com.example.car4ever

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide

class Modelos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modelos)
        lateinit var opcoesModel : Spinner

       // val txtResultadoModel: TextView = findViewById<TextView>(R.id.txtResultadoModel)
        val opcoesTelaModel = arrayOf("none", "Home", "Model", "encontre nos")

        var home = Intent(this, MainActivity::class.java)
        var modelo = Intent(this, Modelos::class.java)
        val reservar1:Button = findViewById<Button>(R.id.btnReservar1)
        val reservar2:Button = findViewById<Button>(R.id.btnReservar2)
        val reservar3:Button = findViewById<Button>(R.id.btnReservar3)
        val image1 = findViewById<ImageView>(R.id.imageView1)
        val image2 = findViewById<ImageView>(R.id.imageView2)
        val image3 = findViewById<ImageView>(R.id.imageView3)
        val modelo1 = findViewById<TextView>(R.id.txtModelo1)
        val modelo2 = findViewById<TextView>(R.id.txtModelo2)
        val modelo3 = findViewById<TextView>(R.id.txtModelo3)

        val text1 = findViewById<TextView>(R.id.textView1)
        val text2 = findViewById<TextView>(R.id.textView2)
        val text3 = findViewById<TextView>(R.id.textView3)

        val carros = arrayOf("azera", "tucson", "ix35")



            reservar1.setOnClickListener{
                val sharedPref: SharedPreferences = this.getSharedPreferences("Pref", Context.MODE_PRIVATE)
                val editor : SharedPreferences.Editor = sharedPref.edit()

                editor.putString("nome", text1.text.toString())
                editor.putString("modelo", modelo1.text.toString())
                editor.apply()
                Toast.makeText(this, "modelo gravado", Toast.LENGTH_SHORT).show()

            val r = Intent(this, ReservaActivity::class.java)
                startActivity(r)

            }




        text1.setText(carros[0])
        text2.setText(carros[1])
        text3.setText(carros[2])

        val i1 = "https://hyundai-motor.com.br/src/assets/img/banner/hyundai-motors_banner_azera.jpg"
        val i2 =    "https://hyundai-motor.com.br/src/assets/img/banner/hyundai-motors_banner_tucson.jpg"
        val i3 = "https://hyundai-motor.com.br/src/assets/img/banner/hyundai-motors_banner_ix35.jpg"


        Glide.with(this).load(i1).into(image1)
        Glide.with(this).load(i2).into(image2)
        Glide.with(this).load(i3).into(image3)
        var nossaLocalizacao = Intent(this, LocalizacaoCar::class.java)
        opcoesModel = findViewById(R.id.modelOpcoes) as Spinner

        opcoesModel.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, opcoesTelaModel)


        opcoesModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position : Int, id: Long) {
              //  txtResultadoModel.text = opcoesTelaModel.get(position)
                if (position.equals(1)){
                    startActivity(home)


                }else if (position.equals(2)){
                    startActivity(modelo)

                }else if(position.equals(3)){
                    startActivity(nossaLocalizacao)
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }



    }

}
