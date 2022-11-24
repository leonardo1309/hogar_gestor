package com.example.hogargestor

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.accept
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private var regName: EditText ?= null
    private var regLastName: EditText ?= null
    private var regNit: EditText ?= null
    private var regPhone: EditText ?= null
    private var regEmail: EditText ?= null
    private var regPassword: EditText ?= null
    private var confirmPassword : TextInputEditText?=null
    private var checkPolicies: CheckBox ?= null
    private val text_pattern : Pattern = Pattern.compile("[a-zA-Z]*")
    private val num_pattern : Pattern = Pattern.compile("[0-9]*")
    private val password_pattern : Pattern = Pattern.compile(
        "^"+
                "(?=.*[0-9])"+
                "(?=.*[a-z])"+
                "(?=.*[A-Z])"+
                "(?=.*[^A-Za-z0-9])"+
                "(?=.*[@#\$%+=*/_-])"+
                "(?=\\S+$)"+
                ".{8,}"+
                "$"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        regName = findViewById(R.id.regName)
        regLastName = findViewById(R.id.regLastName)
        regNit = findViewById(R.id.regNit)
        regPhone = findViewById(R.id.regPhone)
        regEmail = findViewById(R.id.regEmail)
        regPassword = findViewById(R.id.regPassword)
        confirmPassword = findViewById(R.id.confirmPassword)
        checkPolicies = findViewById(R.id.checkPolicies)
        val btnRegister = findViewById<Button>(R.id.btnReg)
    }

    private fun ValidateForm() : Boolean {
        var validate: Boolean = true
        val user = regName!!.text.toString()
        val lastName = regLastName!!.text.toString()
        val nit = regNit!!.text.toString()
        val phone = regPhone!!.text.toString()
        val email = regEmail!!.text.toString()
        val password = regPassword!!.text.toString()
        val confirm = confirmPassword!!.text.toString()

        if(TextUtils.isEmpty(user))
        {
            regName!!.error = getString(R.string.required)
            validate = false
        } else if (!text_pattern.matcher(user.replace(" ", "")).matches()){
            regName!!.error = getString(R.string.invalidInput)
            validate = false
        }
        if(TextUtils.isEmpty(lastName))
        {
            regLastName!!.error = getString(R.string.required)
            validate = false
        } else if (!text_pattern.matcher(lastName.replace(" ", "")).matches()){
            regLastName!!.error = getString(R.string.invalidInput)
            validate = false
        }
        if(TextUtils.isEmpty(nit))
        {
            regNit!!.error = getString(R.string.required)
            validate = false
        } else if (!num_pattern.matcher(nit.replace(" ", "")).matches()){
            regNit!!.error = getString(R.string.invalidInput)
            validate = false
        }
        if(TextUtils.isEmpty(phone))
        {
            regPhone!!.error = getString(R.string.required)
            validate = false
        } else if (!num_pattern.matcher(phone.replace(" ", "")).matches()){
            regPhone!!.error = getString(R.string.invalidInput)
            validate = false
        }
        if(TextUtils.isEmpty(email))
        {
            regEmail!!.error = getString(R.string.required)
            validate = false
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email.replace(" ", "")).matches()){
            regEmail!!.error = getString(R.string.invalidInput)
            validate = false
        }
        if(TextUtils.isEmpty(password))
        {
            regPassword!!.error = getString(R.string.required)
            validate = false
        } else if (!password_pattern.matcher(password.replace(" ", "")).matches()){
            regPassword!!.error = getString(R.string.invalidInput)
            validate = false
        }
        if(TextUtils.isEmpty(confirmPassword!!.text.toString()))
        {
            confirmPassword!!.error = getString(R.string.required)
            validate = false
        } else if (confirm != password){
            confirmPassword!!.error = getString(R.string.passwordNotMatch)
            validate = false
        }
        return validate
    }

    fun onCheck(view: View) {
        val btnRegister = findViewById<Button>(R.id.btnReg)
        if(checkPolicies!!.isChecked) {
            btnRegister.isEnabled = true
            btnRegister.isClickable = true
            btnRegister.setTextColor(Color.BLACK)
            btnRegister.setBackgroundColor(Color.rgb(66,142,146))
        }
        else{
            btnRegister.isEnabled = false
            btnRegister.isClickable = false
            btnRegister.setTextColor(Color.GRAY)
            btnRegister.setBackgroundColor(getColor(R.color.gray_disabled))
        }
    }

    fun onRegister(view: View) {
        if(ValidateForm()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                regEmail!!.text.toString(),
                regPassword!!.text.toString()
            ).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "User created succesfully", Toast.LENGTH_SHORT).show()
                }else showAlert()
            }
            val intent = Intent(this, DashboardActivity:: class.java)
            startActivity(intent)
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