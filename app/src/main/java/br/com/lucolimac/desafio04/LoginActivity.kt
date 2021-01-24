package br.com.lucolimac.desafio04

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.lucolimac.desafio04.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.includeLoginFields.tvCreateAccouont.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }
    }

    override fun onClick(v: View?) {
//        when (v!!.id) {
//            R.id.tvCreateAccouont -> {
//                startActivity(Intent(this, RegisterActivity::class.java))
//            }
//        }
    }
}