package com.example.charactercodex.activities

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.charactercodex.R
import com.example.charactercodex.data.PreferenceManager
import com.example.charactercodex.data.Character as MyCharacter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var containerLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        containerLayout = findViewById(R.id.containerLayout)
        val btnShowInfo = findViewById<ImageButton>(R.id.btnShowInfo)
        val btnGoToProfile = findViewById<ImageButton>(R.id.btnGoToProfile)
        val btnAddCharacter = findViewById<FloatingActionButton>(R.id.btnAddCharacter)
        btnAddCharacter.setOnClickListener {
            startActivity(Intent(this, CreatorActivity::class.java))
        }
        btnGoToProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        btnShowInfo.setOnClickListener {
            showInfoPopup()
        }
        loadCharacters()
    }
    override fun onResume() {
        super.onResume()
        loadCharacters()
    }
    private fun loadCharacters() {
        containerLayout.removeAllViews()

        val characterList = PreferenceManager.getCharacters(this)
        for (charItem: MyCharacter in characterList) {
            val inflater = LayoutInflater.from(this)
            val cardView = inflater.inflate(R.layout.item_character_card, containerLayout, false)
            val txtName = cardView.findViewById<TextView>(R.id.txtCardName)
            val txtRole = cardView.findViewById<TextView>(R.id.txtCardRole)
            txtName.text = charItem.name
            txtRole.text = charItem.role

            //Will reference the exact character information when clicked
            cardView.setOnClickListener {
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra("CHARACTER_DATA", charItem)
                intent.putExtra("CHARACTER_POSITION", characterList.indexOf(charItem))
                startActivity(intent)
            }
            containerLayout.addView(cardView)
        }
    }
    //This will call the CUSTOM MADE popup, the file name being layout_info_popup.xml
    //It is usually a toast popup, but this time we're using the xml suggested by Gemini
    private fun showInfoPopup() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.layout_info_popup, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val btnDismiss = dialogView.findViewById<Button>(R.id.btnDismissPopup)
        btnDismiss.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}