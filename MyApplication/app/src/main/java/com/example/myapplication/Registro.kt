package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.PhantomReference

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        //guardar datos y registrar
        binding.button.setOnClickListener {
            val email = binding.txtCorreo.text.toString()
            val pass = binding.txtPass.text.toString()
            val pass2 = binding.txtConfi.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty() && pass2.isNotEmpty()) {

                if (pass == pass2){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            //envio correo a pantalla de datos
                            var Correo:String = binding.txtCorreo.text.toString()
                            val intent = Intent(this, DatosRegistro::class.java)
                            intent.putExtra("Correo", Correo)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }



        }
        }

        val txtPass = findViewById<EditText>(R.id.txtPass);
        val GoLogin = findViewById<TextView>(R.id.btn_goLogin);

            GoLogin.setOnClickListener {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }

            }
}

