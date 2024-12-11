package com.example.goodhealth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MedicationInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_medication_information)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val medicinebackBtn = findViewById<ImageButton>(R.id.medicineInfoBackBtn)

        medicinebackBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    fun medicineInfoCall(view : View){
        val progressBar = findViewById<ProgressBar>(R.id.loding_bar)
        progressBar.visibility = View.VISIBLE
        val medicineName = findViewById<EditText>(R.id.medicineInfoInput)
        val medicineInfo = findViewById<TextView>(R.id.outputMedicineInfo)
        val beforemedicine = "This is the medicine name which is Written in bracket  "
        val aftermedicine = "  if this was not medicine then give only this answer Invalid Medicine name and  if medicine name is correct then give all information about medicine"

        val generativeModel = GenerativeModel(
            // The Gemini 1.5 models are versatile and work with both text-only and multimodal prompts
            modelName = "gemini-1.5-flash",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAuc9u6UAiav9EVSydimqcYtn8lStBxEGU"
        )
        val prompt = beforemedicine + medicineName.text.toString() + aftermedicine
        print(prompt)

        MainScope().launch {
            val response = generativeModel.generateContent(prompt)
            medicineInfo.setText(response.text)
            progressBar.visibility = View.GONE
            print(response.text)
        }

    }
    fun clearinfo(view: View) {

        var answer = findViewById<TextView>(R.id.outputMedicineInfo)
        if (answer != null){
            answer.setText("Get more Medicine Info")
        }

    }
}