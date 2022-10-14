package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.myapplication.databinding.ActivityEditarPerfilBinding
import com.example.myapplication.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditarPerfil : AppCompatActivity() {
    private lateinit var binding: ActivityEditarPerfilBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dialog: Dialog
    private lateinit var user : User
    private lateinit var uid : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        val atras = findViewById<Button>(R.id.btn_back)
        atras.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()){
            getUserData()
        }
        binding.button.setOnClickListener {
            val nombre = binding.txtName.text.toString()
            val correo = binding.txtCorreo.text.toString()
            val telefono = binding.txtNumeroTel.text.toString()
            val direccion = binding.txtDireccion.text.toString()
            val pass = binding.txtCon.text.toString()
            val puntos = binding.txtPun.text.toString()


            //Creamos database Users
            val user = User(nombre, correo, telefono, direccion, pass, puntos)
            if (uid != null){
                databaseReference.child(uid).setValue(user).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Datos actualizados correctamnete", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Perfil::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                val nombre =  binding.txtName.setText(user.nombre)
                val correo =  binding.txtCorreo.setText(user.email)
                val telefono = binding.txtNumeroTel.setText(user.telefono)
                val direccion =  binding.txtDireccion.setText(user.direccion)
                val pass = binding.txtCon.setText(user.pass)
                val puntos = binding.txtPun.setText(user.puntos)


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}