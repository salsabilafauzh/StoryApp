package com.example.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.AuthViewModelFactory
import com.example.storyapp.ui.main.MainActivity
import com.example.storyapp.ui.register.RegisterActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels {
        AuthViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBtn()
        playAnimation()

        loginViewModel.isLoading().observe(this) {
            showLoading(it)
        }
    }

    private fun setupActionBtn() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = binding.edLoginEmail.text.toString()
                val password = binding.edLoginPassword.text.toString()
                if (password.length >= 8) {
                    loginViewModel.apply {
                        loginUser(
                            email,
                            password
                        ).observe(this@LoginActivity) { res ->
                            if (res.error == false) {
                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        MainActivity::class.java
                                    )
                                )
                                finish()
                            } else {
                                when (res.message) {
                                    "400" -> {
                                        showDialog(getString(R.string.invalid_input_message))
                                    }

                                    "401" -> {
                                        showDialog(getString(R.string.user_not_found_401))
                                    }

                                    else -> {
                                        showDialog(getString(R.string.error_message))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }
        }
    }

    private fun showDialog(message: String) {
        MaterialAlertDialogBuilder(this@LoginActivity)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(message)
            .setNeutralButton(resources.getString(R.string.ok_dialog_button)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun playAnimation() {
        val tvWelcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(200)
        val tvLogin =
            ObjectAnimator.ofFloat(binding.tvLoginContinue, View.ALPHA, 1f).setDuration(200)
        val image = ObjectAnimator.ofFloat(binding.image, View.ALPHA, 1f).setDuration(200)
        val edEmail =
            ObjectAnimator.ofFloat(binding.edLoginEmailLayout, View.ALPHA, 1f).setDuration(200)
        val edPassword =
            ObjectAnimator.ofFloat(binding.edLoginPasswordLayout, View.ALPHA, 1f).setDuration(200)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(200)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(200)

        val btnTogether = AnimatorSet().apply {
            playTogether(btnLogin, btnRegister)
        }

        AnimatorSet().apply {
            playSequentially(tvWelcome, tvLogin, image, edEmail, edPassword, btnTogether)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}