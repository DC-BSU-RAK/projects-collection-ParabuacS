package com.example.charactercodex.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.charactercodex.R
import com.example.charactercodex.data.PreferenceManager

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Author info is currently hard coded
        val txtAuthorName = findViewById<TextView>(R.id.txtAuthorName)
        val txtPersonaCount = findViewById<TextView>(R.id.txtPersonaCount)
        val container = findViewById<LinearLayout>(R.id.authorPersonasContainer)

        // Characters in the library are loaded again for activity_profile
        val characters = PreferenceManager.getCharacters(this)

        // Updates the number of personas according to how many the author made
        txtPersonaCount.text = "${characters.size} Personas Created"

        // Adds the character cards
        for (character in characters) {
            val inflater = LayoutInflater.from(this)
            val cardView = inflater.inflate(R.layout.item_character_card, container, false)
            val txtName = cardView.findViewById<TextView>(R.id.txtCardName)
            val txtRole = cardView.findViewById<TextView>(R.id.txtCardRole)
            txtName.text = character.name
            txtRole.text = character.role
            container.addView(cardView)
        }
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.scaleType = ImageView.ScaleType.FIT_CENTER
        btnBack.setOnClickListener {
            finish()
        }

    }
}