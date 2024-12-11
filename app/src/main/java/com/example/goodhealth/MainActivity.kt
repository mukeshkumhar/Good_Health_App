package com.example.goodhealth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {


    private lateinit var editText: EditText //voice input save in edit text
    private lateinit var voiceInputButton: Button
    private lateinit var speechRecognizer: SpeechRecognizer // voice input recognize
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200


    private lateinit var cardRecyclerView: RecyclerView // card recyclerview
    private lateinit var cardAdapter: CardAdapter // card adapter learn more pupop view


//    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        cardRecyclerView = findViewById(R.id.cardRecyclerView)
        cardRecyclerView.layoutManager = LinearLayoutManager(this)

//        Voice input text view and button
        editText = findViewById(R.id.inputTextView)
        voiceInputButton = findViewById(R.id.voicebutton)


        // Check and request audio recording permission
         if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
            ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION)
            }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(recognitionListener)

        voiceInputButton.setOnClickListener {
         startSpeechToText()
        }




        // Sample data for cards
        val cardData = listOf(
            CardItem("90% of people having this health issues !", "It's important to understand that 90% of the population likely isn't experiencing health issues in India. However, a significant portion of the population does face various health challenges. Here's a breakdown of some prominent health issues and solutions:\n" +
                    "\n\n" +
                    "1. Communicable Diseases:\n" +
                    "\n" +
                    "Issues: Diarrhoea, respiratory infections, tuberculosis, malaria, dengue fever.\n" +
                    "Why: Poor sanitation, inadequate access to clean water, and malnutrition contribute to these diseases.\n" +
                    "How to Save: Improved sanitation facilities, access to clean water, hygiene education, and childhood vaccinations are crucial.\n\n" +
                    "2. Non-Communicable Diseases (NCDs):\n" +
                    "\n" +
                    "Issues: Cardiovascular diseases (heart disease, stroke), diabetes, chronic respiratory diseases, cancers.\n" +
                    "Why: Lifestyle changes like unhealthy diet, physical inactivity, tobacco use, and rising air pollution contribute to NCDs.\n" +
                    "How to Save: Promote healthy eating habits, encourage physical exercise, raise awareness about the dangers of tobacco, and implement stricter air pollution control measures.\n\n" +
                    "3. Malnutrition:\n" +
                    "\n" +
                    "Issues: Undernutrition, micronutrient deficiencies (iron, Vitamin A), obesity.\n" +
                    "Why: Poverty, lack of access to diverse and nutritious food, and inadequate dietary practices contribute to malnutrition.\n" +
                    "How to Save: Food fortification programs, promoting kitchen gardens, education on balanced diets, and addressing poverty are key areas for improvement.\n\n" +
                    "4. Maternal and Child Health:\n" +
                    "\n" +
                    "Issues: High maternal mortality rates, infant mortality, and stunting in children.\n" +
                    "Why: Inadequate access to quality healthcare, particularly in rural areas, and lack of awareness about prenatal care are contributing factors.\n" +
                    "How to Save: Improve access to prenatal care, skilled birth attendants, and essential healthcare services for mothers and children.\n" +
                    "These are some of the major health concerns in India. Addressing them requires a multi-pronged approach involving government initiatives, community engagement, and individual lifestyle changes."),


//            Card Item 2
            CardItem("Only 3% of Indian Follows this Healthy tips", "1. Focus on Whole Grains over Refined Grains:\n" +
                    "\n" +
                    "While rice is a staple in many Indian diets, it's often consumed as refined white rice.\n" +
                    "Switch to whole grains like brown rice, millets (jowar, bajra), and whole wheat roti.\n" +
                    "Whole grains offer more fiber, vitamins, and minerals, promoting better digestion and blood sugar control.\n\n" +
                    "2. Include More Plant-Based Protein Sources:\n" +
                    "\n" +
                    "While lentils and pulses are common, explore a wider variety of plant-based protein sources:\n" +
                    "Soybeans and tofu\n" +
                    "Sprouted grains and legumes\n" +
                    "Nuts and seeds (almonds, peanuts, flaxseeds)\n" +
                    "This can help reduce dependence on meat protein and provide a more diverse nutrient profile.\n" +
                    "3. Embrace Traditional Cooking Methods:\n" +
                    "\n" +
                    "Traditional methods like steaming, boiling, and stir-frying often involve less oil compared to deep-frying.\n\n" +
                    "This can help reduce unhealthy fat intake and promote healthier cooking practices.\n" +
                    "4. Limit Added Sugar and Salt:\n" +
                    "\n" +
                    "Many commercially prepared Indian snacks and processed foods can be high in added sugar and salt.\n" +
                    "Opt for homemade snacks using fresh ingredients and limit added sugar and salt in cooking.\n" +
                    "Pay attention to food labels and choose options with lower sodium content.\n\n" +
                    "5. Prioritize Sleep:\n" +
                    "\n" +
                    "Getting enough sleep (7-8 hours per night) is crucial for overall health and well-being.\n" +
                    "Adequate sleep regulates hormones that control hunger and metabolism, impacting weight management.\n\n" +
                    "6. Manage Stress:\n" +
                    "\n" +
                    "Stress can be a major contributor to unhealthy lifestyle choices.\n" +
                    "Explore stress management techniques like yoga, meditation, or deep breathing exercises.\n\n" +
                    "7. Regular Physical Activity:\n" +
                    "\n" +
                    "While gym memberships might be uncommon, incorporate physical activity into daily life:\n" +
                    "Take the stairs instead of elevators.\n" +
                    "Walk or cycle for short errands.\n" +
                    "Engage in traditional Indian activities like brisk walking, yoga, or sports like badminton.\n" +
                    "These tips can be valuable additions to a healthy lifestyle, regardless of how many people currently follow them. Remember, consistency is key. Start by incorporating small changes and gradually build them into sustainable habits."),


//            CArd Item 3
            CardItem("If you having Heart problen then click Learn more", "Diet:\n" +
                    "\n" +
                    "Focus on fruits, vegetables, and whole grains: These foods provide essential nutrients and fiber, which can help lower cholesterol and blood pressure.\n" +
                    "Limit saturated and unhealthy fats: Found in red meat, processed meats, and fried foods. Opt for lean protein sources like fish, chicken without skin, beans, and lentils.\n" +
                    "Reduce added sugar and salt: Excessive intake can contribute to high blood pressure and other health problems.\n" +
                    "Consider a Mediterranean diet: This diet emphasizes fruits, vegetables, whole grains, healthy fats (like olive oil), and fish, which has been shown to benefit heart health.\n\n" +
                    "Lifestyle:\n" +
                    "\n" +
                    "Regular exercise: Aim for at least 150 minutes of moderate-intensity exercise or 75 minutes of vigorous-intensity exercise per week.\n" +
                    "Maintain a healthy weight: Obesity is a risk factor for heart disease.\n" +
                    "Manage stress: Chronic stress can raise blood pressure and increase the risk of heart problems. Consider relaxation techniques like yoga, meditation, or deep breathing.\n" +
                    "Don't smoke: Smoking is a major risk factor for heart disease. If you smoke, quitting is the single best thing you can do for your heart health.\n" +
                    "Limit alcohol consumption: Excessive alcohol intake can increase blood pressure and contribute to heart problems.\n\n" +
                    "Medications:\n" +
                    "\n" +
                    "Your doctor might prescribe medications like cholesterol-lowering drugs or blood pressure medications to manage your heart condition. It's crucial to follow their recommendations.\n\n" +
                    "Additional Tips:\n" +
                    "\n" +
                    "Get regular checkups: This allows your doctor to monitor your heart health and adjust treatment as needed.\n" +
                    "Learn about your condition: The more you understand about heart disease and your specific risk factors, the better equipped you'll be to manage your health.\n" +
                    "Join a support group: Connecting with others who have heart disease can provide valuable support and encouragement.")
            // Add more card items as needed
        )


        cardAdapter = CardAdapter(cardData) { cardItem, cardView ->
            showPopup(cardView, cardItem.explanation)
        }
        cardRecyclerView.adapter = cardAdapter


//        Button Links
        val shareBtn = findViewById<Button>(R.id.shareBtn)
        val linkedInBtn = findViewById<Button>(R.id.linkedInbtn)
        val githubBtn = findViewById<Button>(R.id.githubBtn)
        val browserBtn = findViewById<Button>(R.id.browserBtn)
        val instagramBtn = findViewById<Button>(R.id.instagramBtn)

        val shareUrl = "https://drive.google.com/file/d/1BlgG4hUWpvOZlPy1kJt26p3TOYzPdn59/view?usp=sharing" //App downlode Link
        val linkedInUrl = "https://www.linkedin.com/in/mukesh-kumhar/" //LinkedIn Profile Link
        val githubUrl = "https://github.com/Codewhatyouwant" //GitHub Profile Link
        val browserUrl = "https://codewhatyouwant.github.io/My-PortFolio/" //Portfolio Link
        val instagramUrl = "https://www.instagram.com/hog.____/" //Instagram Profile Link


        shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl))
            startActivity(intent)
        }
        linkedInBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl))
            startActivity(intent)
        }
        githubBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
            startActivity(intent)
        }
        browserBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(browserUrl))
            startActivity(intent)
        }
        instagramBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
            startActivity(intent)
        }


//        Emergency Contact Card btn
    val emergencybtn = findViewById<Button>(R.id.emergencybtn)

    emergencybtn.setOnClickListener{
        val intent = Intent(this, EmergencyActivity::class.java)
        startActivity(intent)
    }

//    Medication information btn
    val medicationbtn = findViewById<Button>(R.id.searchMedicineBtn)

    medicationbtn.setOnClickListener{
        val intent = Intent(this, MedicationInformationActivity::class.java)
        startActivity(intent)
    }


    }

// Speech to text function

    private fun startSpeechToText() {
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")

        try {
            startActivityForResult(speechIntent, REQUEST_RECORD_AUDIO_PERMISSION)
        } catch (e: Exception) {
            Toast.makeText(this, "Speech recognition failed", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION && resultCode == RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (results != null && results.isNotEmpty()) {
                editText.setText(results[0]) // Set the recognized text to the EditText
            }
        }
    }

    private val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            // Called when the speech recognizer is ready
        }

        override fun onBeginningOfSpeech() {
            // Called when the user starts speaking
        }

        override fun onRmsChanged(rmsdB: Float) {
            //
        }

        fun onRearingSound(params: Bundle?) {
            // Called when the recognizer detects a "beep" sound
        }

        fun onSpeechStart(timestampMillis: Long) {
            // Called when actual speech starts
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            // Called when voice data is received
        }

        override fun onEndOfSpeech() {
            // Called when the user stops speaking
        }

        override fun onError(error: Int) {
            // Called when an error occurs during speech recognition// Handle errors appropriately (e.g., display a message)
        }

        override fun onResults(results: Bundle?) {
            // Called when recognition results are available
            val matches = results?.getStringArray(SpeechRecognizer.RESULTS_RECOGNITION)
            if (matches != null && matches.isNotEmpty()) {
                editText.setText(matches[0])
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            // Called with partial recognition results (if enabled)
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            // Called for various events during speech recognition
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy() // Release resources
    }




//    Learn more popup view

    private fun showPopup(anchorView: CardView, explanation: String) {
        val popupView = LayoutInflater.from(this).inflate(R.layout.popup_layout, null)
        val popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )

        val explanationText = popupView.findViewById<TextView>(R.id.explanationText)
        explanationText.text = explanation

        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
        popupWindow.showAsDropDown(anchorView)



    }


//    private fun modelcall() {
//        var textView = findViewById<TextView>(R.id.textView)
//        val generativeModel = GenerativeModel(
//            modelName = "gemini-1.5-flash",
//            apiKey = "AIzaSyAuc9u6UAiav9EVSydimqcYtn8lStBxEGU"
//        )
//
//        val prompt = "Write a story about a magic backpack."
//
//        // Launch the coroutine on the IO dispatcher for network operations
//        coroutineScope.launch(Dispatchers.IO) {
//            val response = generativeModel.generateContent(prompt)
//            // Switch back to the main thread to update the UI
//            coroutineScope.launch(Dispatchers.Main) {
//                print(response.text)
//                // Update your UI elements with the response here
//            }
//        }
//    }


    // A basic keyword-based approach to identify health-related questions
    fun isHealthRelated(prompt: String): Boolean {
        val healthKeywords = listOf("health",
            "medicine",
            "symptom",
            "doctor",
            "treatment",
            "disease",
            "condition",
            "diet",
            "exercise",
            "wellness",
            "Diseases",
            "Common cold",
            "Fever",
            "Cough",
            "Headache",
            "Back pain",
            "Joint pain",
            "eye pain",
            "Stomach pain",
            "Heart pain",
            "heart",
            "Diabetes",
            "Hypertension",
            "Cancer",
            "Asthma",
            "Allergies",
            "Kidney stone",
            "Kidney",
            "Lever",
            "Stroke",
            "Heart disease",
            "Malaria",
            "Dengue fever",
            "Tuberculosis",
            "Hepatitis",
            "Chikungunya",
            "HIV",
            "AIDS",
            "cardiovascular",
            "organ",
            "health",
            "eye",
            "nose",
            "head",
            "female",
            "male",
            "reproductive system",
            "mail reproductive system",
            "female reproductive sister",
            "girl",
            "boy",
            "organ system",
            "woman",
            "pain",
            "Medical","Medicals","covid 19","covid-19","Hepatitis","Yellow Fever","Bacterial","Virus","Viral fever","Teeth","ear","hair","Skin","Nose","Blood pressure")
        return healthKeywords.any { prompt.contains(it, ignoreCase = true) }
    }



     fun modelcall(view : View){
         val peogressBar = findViewById<ProgressBar>(R.id.loding_bar)
         peogressBar.visibility = View.VISIBLE
        var question = findViewById<TextView>(R.id.inputTextView)
        var answer = findViewById<TextView>(R.id.outputTextView)
//        val limited = ",In India"

        val generativeModel = GenerativeModel(
            // The Gemini 1.5 models are versatile and work with both text-only and multimodal prompts
            modelName = "gemini-1.5-flash",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyAuc9u6UAiav9EVSydimqcYtn8lStBxEGU"
        )

        val prompt = question.text.toString()
        print(prompt)

         if (isHealthRelated(prompt)){
             MainScope().launch {
                 val response = generativeModel.generateContent(prompt)
                 answer.setText(response.text)
                 peogressBar.visibility = View.GONE
                 print(response.text)
             }
         } else {
             answer.setText("I will give answer only about health related questions.\n Please ask a question related to health.")
             peogressBar.visibility = View.GONE
         }



     }


//    on pressing clear button answer will clear
    fun clearText(view: View) {

        var answer = findViewById<TextView>(R.id.outputTextView)
        if (answer != null){
            answer.setText("Ask more questions about health")
        }

    }
}



// Data class for card items
data class CardItem(val title: String, val explanation: String)

// Adapter for the RecyclerView
class CardAdapter(
    private val cardItems: List<CardItem>,
    private val onLearnMoreClick: (CardItem, CardView) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTitle: TextView = itemView.findViewById(R.id.cardTitle)
        val learnMoreButton: Button = itemView.findViewById(R.id.learnMoreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = cardItems[position]
        holder.cardTitle.text = cardItem.title
        holder.learnMoreButton.setOnClickListener {
            onLearnMoreClick(cardItem, holder.itemView as CardView)
        }
    }

    override fun getItemCount(): Int = cardItems.size
}

