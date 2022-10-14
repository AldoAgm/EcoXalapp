package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class Perfil : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dialog: Dialog
    private lateinit var user : User
    private lateinit var uid : String




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        //Entramos a users firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()){
            getUserData()
        }

        //Boton atrás
        val atras = findViewById<Button>(R.id.btn_back)
        atras.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        //cerrar sesion
        val cerrar_sesion = findViewById<Button>(R.id.btn_logout)
        cerrar_sesion.setOnClickListener {
            Firebase.auth.signOut()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        //Ir a Editar perfil
        val editar = findViewById<Button>(R.id.btnEditarPerfil)
        editar.setOnClickListener {

            val intent = Intent(this, EditarPerfil::class.java)
            startActivity(intent)
        }
    }

    //Traemos información del usuario
    private fun getUserData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                binding.txtNombrePerfil.setText(user.nombre)
                binding.txtEmailPerfil.setText(user.email)
                binding.txtPuntos.setText(user.puntos)
                getUserProfile()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getUserProfile() {

    }


}