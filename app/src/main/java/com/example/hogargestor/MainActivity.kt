package com.example.hogargestor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private var editUsername: EditText?= null
    private var editPassword : EditText?=null
    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editUsername = findViewById(R.id.editUserName)
        editPassword = findViewById(R.id.editPassword)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoogleLogin = findViewById<Button>(R.id.btnGoogleLogin)


        btnRegister?.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnLogin?.setOnClickListener{
            login()
        }
        btnGoogleLogin?.setOnClickListener{
            googleLogin()
        }
        forgotPassword?.setOnClickListener{
            forgotPassword()
        }
    }

    private fun forgotPassword() {
        val intent = Intent(this, ForgetPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun googleLogin() {
        val googleconf : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        val googleClient : GoogleSignInClient = GoogleSignIn.getClient(this, googleconf)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_SIGN_IN){
            val task : com.google.android.gms.tasks.Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account : GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if(account != null){
                    val credential : AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this, DashboardActivity::class.java)
                            startActivity(intent)
                        }else showAlert()
                    }
                }
            }
            catch (e: ApiException){
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.error))
                builder.setMessage(e.toString())
                builder.setPositiveButton(getString(R.string.accept), null)
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }
        }
    }
    private fun login() {
        if (editUsername!!.text.isNotEmpty() && editPassword!!.text.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                editUsername!!.text.toString(),
                editPassword!!.text.toString()
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                } else showAlert()
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.error))
        builder.setMessage(getString(R.string.authError))
        builder.setPositiveButton(getString(R.string.accept), null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}