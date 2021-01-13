package com.binkypv.presentation.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.binkypv.presentation.R
import com.binkypv.presentation.databinding.ActivityDeezearchBinding

class DeezearchActivity : AppCompatActivity() {
    private var binding: ActivityDeezearchBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_deezearch
        )
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}