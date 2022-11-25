package com.example.hogargestor

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {

    private var recoveryEmail : EditText? = null
    private var btnRecover : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        recoveryEmail = findViewById(R.id.recoveryEmail)
        btnRecover = findViewById(R.id.btnRecover)
        btnRecover?.setOnClickListener{onForgotPass()}
    }

    private fun onForgotPass() {
            FirebaseAuth.getInstance().sendPasswordResetEmail(recoveryEmail!!.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this,"Correo enviado", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Dirección de correo no existe", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}