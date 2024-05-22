package com.example.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.AuthViewModelFactory
import com.example.storyapp.ui.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAnimation()
        setupActionBtn()

        registerViewModel.isLoading().observe(this) {
            showLoading(it)
        }
    }

    private fun setupActionBtn() {
        binding.apply {
            btnRegister.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()

                if (password.length >= 8) {
                    registerViewModel.registerUser(name, email, password)
                        .observe(this@RegisterActivity) {
                            if (it.error == true) {
                                when (it.message) {
                                    "400" -> {
                                        showDialog(getString(R.string.account_already_register))
                                    }

                                    "500" -> {
                                        showDialog(getString(R.string.error_message))
                                    }
                                }
                            } else {
                                MaterialAlertDialogBuilder(this@RegisterActivity)
                                    .setTitle(resources.getString(R.string.app_name))
                                    .setMessage(resources.getString(R.string.success_message))
                                    .setNeutralButton(resources.getString(R.string.ok_dialog_button)) { dialog, _ ->
                                        dialog.dismiss()
                                        startActivity(
                                            Intent(
                                                this@RegisterActivity,
                                                LoginActivity::class.java
                                            )
                                        )
                                        finish()
                                    }.show()
                            }
                        }
                }
            }
            btnLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun showDialog(message: String) {
        MaterialAlertDialogBuilder(this@RegisterActivity)
            .setTitle(resources.getString(R.string.app_name))
            .setMessage(message)
            .setNeutralButton(resources.getString(R.string.ok_dialog_button)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun setupAnimation() {
        val tvWelcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(200)
        val tvRegis =
            ObjectAnimator.ofFloat(binding.tvRegisterContinue, View.ALPHA, 1f).setDuration(200)
        val image = ObjectAnimator.ofFloat(binding.image, View.ALPHA, 1f).setDuration(200)
        val edName =
            ObjectAnimator.ofFloat(binding.edRegisterNameLayout, View.ALPHA, 1f).setDuration(200)
        val edEmail =
            ObjectAnimator.ofFloat(binding.edRegisterEmailLayout, View.ALPHA, 1f).setDuration(200)
        val edPassword =
            ObjectAnimator.ofFloat(binding.edRegisterPasswordLayout, View.ALPHA, 1f)
                .setDuration(200)

        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(200)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(200)


        val btnTogether = AnimatorSet().apply {
            playTogether(btnLogin, btnRegister)
        }

        AnimatorSet().apply {
            playSequentially(tvWelcome, tvRegis, image, edName, edEmail, edPassword, btnTogether)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

}