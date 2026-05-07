package com.example.charactercodex.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.charactercodex.R
import com.example.charactercodex.data.Character as MyCharacter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        //Retrieving the character data from the intent, taken from MainActivity and Character.kt
        //MyCharacter is the alias used to grab data from Character.kt because I experienced several issues with importing it
        val character = intent.getSerializableExtra("CHARACTER_DATA") as? MyCharacter
        val position = intent.getIntExtra("CHARACTER_POSITION", -1)

        //Adding info from the specific character card to the details page
        if (character != null) {
            findViewById<TextView>(R.id.txtDetailsName).text = character.name
            findViewById<TextView>(R.id.txtDetailsRole).text = character.role
            findViewById<TextView>(R.id.txtDetailsBio).text = character.bio
        }
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.scaleType = ImageView.ScaleType.FIT_CENTER
        btnBack.setOnClickListener {
            finish()
        }

        //Edit button sends us to activity_creator but will tell CreatorActivity that we came from the edit screen, NOT the creation screen
        findViewById<FloatingActionButton>(R.id.btnEditPersona).setOnClickListener {
            val editIntent = Intent(this, CreatorActivity::class.java)
            editIntent.putExtra("EDIT_MODE", true)
            editIntent.putExtra("CHARACTER_DATA", character)
            editIntent.putExtra("POSITION", position)
            startActivity(editIntent)
            //CreatorActivity sends us back to the MainActivity then into the library
            finish()
        }
    }
}