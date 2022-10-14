package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityDatosRegistroBinding
import com.example.myapplication.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DatosRegistro : AppCompatActivity() {
    private lateinit var binding: ActivityDatosRegistroBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDatosRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        //traigo correo del registro
        val objetoIntent : Intent=intent
        var Correo = objetoIntent.getStringExtra("Correo")
        binding.txtCorreo.setText("$Correo")

        //Guardamos info en users
        binding.button.setOnClickListener {
            val nombre = binding.txtName.text.toString()
            val correo = binding.txtCorreo.text.toString()
            val telefono = binding.txtNumeroTel.text.toString()
            val direccion = binding.txtDireccion.text.toString()
            val pass = "0"
            val puntos = "0"



            //Creamos database Users
            val user = User(nombre, correo, telefono, direccion, pass, puntos)
            if (uid != null){
                if (telefono.length < 10){
                    Toast.makeText(this, "El numero de télefono debe tener 10 caracteres", Toast.LENGTH_SHORT).show()
                }else{
                    databaseReference.child(uid).setValue(user).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }


    }
}