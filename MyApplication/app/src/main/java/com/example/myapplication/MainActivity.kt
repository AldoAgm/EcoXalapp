package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.zxing.integration.android.IntentIntegrator
import com.onesignal.OneSignal
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {

    //Onesignal Notificaciones
    private val ONESIGNAL_APP_ID = "cb89591b-bf72-437b-acf7-77e546451c9b"
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dialog: Dialog
    private lateinit var user : User
    private lateinit var uid : String


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
        //Fin Onesignal

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if (uid.isNotEmpty()){
            getPuntosData()
        }

        //vamos a pantalla perfil
        val perfil = findViewById<ImageButton>(R.id.btn_Perfil)
        perfil.setOnClickListener {

            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }

        //Llamamos al Qr reader
        val qr = findViewById<ImageButton>(R.id.btn_entrega)
        qr.setOnClickListener {
            initScanner()
        }
      /**  val actionBar = supportActionBar
        actionBar!!.title = "Prueba app"**/

    }
    //Iniciar qr scanner
    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el Código del recolector")
        integrator.setTorchEnabled(false)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null){
            if (result.contents == null){
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }else{
                val nombre = binding.txtName.text.toString()
                val correo = binding.txtCorreo.text.toString()
                val telefono = binding.txtNumeroTel.text.toString()
                val direccion = binding.txtDireccion.text.toString()
                val pass = binding.txtCon.text.toString()
                //Corregir puntos para que se sume en entero
                val puntos = binding.txtPuntos.text.toString() + "${result.contents}"
               // val qr = "${result.contents}"
                //val qrpuntos = binding.qrPuntos.setText(qr).toString()
                //val intpuntos = puntos.toInt()
                //val intqr = qrpuntos.toInt()
                //val suma = (intpuntos + intqr)

               // val puntossubir = suma.text.toString()

                val user = User(nombre, correo, telefono, direccion, pass, puntos)
                if (uid != null){
                    databaseReference.child(uid).setValue(user).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this, "Haz obtenido  ${result.contents} puntos", Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(this, "Error al obtener los puntos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                Toast.makeText(this, "El valor escaneado es: ${result.contents}", Toast.LENGTH_SHORT).show()
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
    //Fin qr scanner
    private fun getPuntosData() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                binding.txtPuntos.setText(user.puntos)
                val nombre =  binding.txtName.setText(user.nombre)
                val correo =  binding.txtCorreo.setText(user.email)
                val telefono = binding.txtNumeroTel.setText(user.telefono)
                val direccion =  binding.txtDireccion.setText(user.direccion)
                val pass = binding.txtCon.setText(user.pass)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}