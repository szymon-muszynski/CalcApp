package com.example.calcapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var btSimpleCalc: Button
    private lateinit var btAdvancedCalc: Button
    private lateinit var btAbout: Button
    private lateinit var btExit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btSimpleCalc = findViewById(R.id.btSimpleCalc)
        btAdvancedCalc = findViewById(R.id.btAdvancedCalc)
        btAbout = findViewById(R.id.btAbout)
        btExit = findViewById(R.id.btExit)

        btSimpleCalc.setOnClickListener {
            val intent1 = Intent(this, SimpleCalcActivity::class.java)
            startActivity(intent1)
        }

        btAdvancedCalc.setOnClickListener{
            val intent2 = Intent(this, AdvancedCalcActivity::class.java)
            startActivity(intent2)
        }

        btAbout.setOnClickListener{
            val intent3 = Intent(this, AboutMeActivity::class.java)
            startActivity(intent3)
        }

        btExit.setOnClickListener {
            finishAffinity()
        }
    }
}