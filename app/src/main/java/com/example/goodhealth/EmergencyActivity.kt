package com.example.goodhealth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.app.ActivityCompat
import android.Manifest

class EmergencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emergency)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
// Back button on Emergency Activity
        val backButton = findViewById<ImageButton>(R.id.emergencyBackBtn)

        backButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

//        make call while clicking on emergency button

        // Request CALL_PHONE permission at runtime
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)

//        All button on Emergency Activity
        // Find buttons by their IDs
        val ambulanceBtn = findViewById<Button>(R.id.ambulanceBtn)
        val policeBtn = findViewById<Button>(R.id.policeBtn)
        val fireBtn = findViewById<Button>(R.id.fireBtn)
        val womenhelpBtn = findViewById<Button>(R.id.womenhelpBtn)
        val womendomeBtn = findViewById<Button>(R.id.womendomeBtn)
        val railwayEnqBtn = findViewById<Button>(R.id.railwayEnqBtn)
        val seniorCitiBtn = findViewById<Button>(R.id.seniorCitiBtn)
        val roadAcciBtn = findViewById<Button>(R.id.roadAcciBtn)
        val cyberCrimeBtn = findViewById<Button>(R.id.cyberCrimeBtn)

        // Set up the click listeners

        ambulanceBtn.setOnClickListener{
            makeCall("102") // Ambulance Call
        }

        policeBtn.setOnClickListener{
            makeCall("100") //police call
        }

        fireBtn.setOnClickListener{
            makeCall("101") //fire call
        }

        womenhelpBtn.setOnClickListener{
            makeCall("1097") //Women helpline call
        }

        womendomeBtn.setOnClickListener{
            makeCall("181") //Women helpline call for domestic abuse
        }

        railwayEnqBtn.setOnClickListener{
            makeCall("139") //Railway Enquiry
        }

        seniorCitiBtn.setOnClickListener{
            makeCall("14567") //Senior citizen call
        }

        roadAcciBtn.setOnClickListener{
            makeCall("1073") //Road Accident call
        }

        cyberCrimeBtn.setOnClickListener{
            makeCall("155620") //Cyber crime call
        }



    }
    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }
}