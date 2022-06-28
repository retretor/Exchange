package com.example.exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun convert(value: Double, from: String, to: String): Double {
        val fromRate = getRate(from)
        val toRate = getRate(to)
        return value * fromRate / toRate
    }
    
}