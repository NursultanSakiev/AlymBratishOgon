package com.example.alymbratishogon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.alymbratishogon.databinding.ActivityPhoneAuthBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuth : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityPhoneAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.sendCodeButton.setOnClickListener {
            val phoneNumber = binding.phoneNumberEditText.text.toString()

            // Проверяем, что номер введен корректно
            if (phoneNumber.isNotEmpty()) {
                // Отправляем код аутентификации на указанный номер
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)       // Номер, на который отправляется SMS
                    .setTimeout(60L, TimeUnit.SECONDS) // Тайм-аут для получения SMS-кода
                    .setActivity(this)                 // Текущая активность
                    .setCallbacks(callbacks)           // Обработчики успешной и неудачной аутентификации
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
        }
    }

    // Обработчики успешной и неудачной аутентификации
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Этот метод вызывается, если номер автоматически подтвержден
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // Этот метод вызывается в случае ошибки верификации
            // Обработайте ошибку
        }
    }

    // Метод для входа после успешной аутентификации
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Вход выполнен успешно
                    // Переходите на следующий экран или выполните дополнительные действия
                } else {
                    // Вход не выполнен
                    // Обработайте ошибку
                }
            }
    }
}