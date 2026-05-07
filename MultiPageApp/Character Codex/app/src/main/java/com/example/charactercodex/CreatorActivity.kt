package com.example.charactercodex.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.charactercodex.R
// Unifying the alias to prevent the java.lang.Character error
import com.example.charactercodex.data.Character as MyCharacter
import com.example.charactercodex.data.PreferenceManager
import com.google.android.material.textfield.TextInputEditText

class CreatorActivity : AppCompatActivity() {
    // There are two modes in activity_creator: Create and Edit
    // Create activates when the user clicks the + button in activity_main
    // Edit activates when the user clicks on any existing character card
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)

        val etName = findViewById<TextInputEditText>(R.id.etCharacterName)
        val etRole = findViewById<TextInputEditText>(R.id.etCharacterRole)
        val etBio = findViewById<TextInputEditText>(R.id.etCharacterBio)
        val btnSave = findViewById<Button>(R.id.btnSaveCharacter)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        // This checks if the user has it on Edit mode
        val isEditMode = intent.getBooleanExtra("EDIT_MODE", false)
        val existingChar = intent.getSerializableExtra("CHARACTER_DATA") as? MyCharacter
        val position = intent.getIntExtra("POSITION", -1)

        // Call the data from that specific character card to fill up the fields (Name, Role, Bio)
        if (isEditMode && existingChar != null) {
            etName.setText(existingChar.name)
            etRole.setText(existingChar.role)
            etBio.setText(existingChar.bio)
            btnSave.text = "Update Persona" // Visual cue that we are editing
        }

        // Saves the new info to the data
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val role = etRole.text.toString().trim()
            val bio = etBio.text.toString().trim()

            if (name.isNotEmpty() && role.isNotEmpty()) {
                val updatedCharacter = MyCharacter(name, role, bio)
                if (isEditMode && position != -1) {
                    // Updates the name field
                    PreferenceManager.updateCharacter(this, updatedCharacter, position)
                    Toast.makeText(this, "Persona updated!", Toast.LENGTH_SHORT).show()
                } else {
                    // Will erase the old information and replace it with the new info
                    // In a sense, the old version of the card is deleted, then the new version is added
                    PreferenceManager.saveCharacter(this, updatedCharacter)
                    Toast.makeText(this, "$name saved to Codex!", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else { // If there's no information added, a popup will show up
                Toast.makeText(this, "Please provide a name and role.", Toast.LENGTH_SHORT).show()
            }
        }
        btnBack.scaleType = ImageView.ScaleType.FIT_CENTER
        btnBack.setOnClickListener {
            finish()
        }
    }
}