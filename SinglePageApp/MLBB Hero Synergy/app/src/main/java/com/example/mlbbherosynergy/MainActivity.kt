package com.example.mlbbherosynergy

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mlbbherosynergy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null //this handles the audio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //since we're not using a SplashActivity or a splash screen xml, the logic for the splash is here
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            binding.splashScreen.visibility = View.GONE
        }, 3000)
        binding.btnAnalyze.setOnClickListener {
            showSynergyPopup()
        }
        binding.btnHelp.setOnClickListener {
            showHelpPopup()
        }
    }

    private fun showSynergyPopup() {
        //letting the user choose from the strings.xml file
        val selections = listOf(
            binding.spinnerP1.selectedItem.toString(),
            binding.spinnerP2.selectedItem.toString(),
            binding.spinnerP3.selectedItem.toString(),
            binding.spinnerP4.selectedItem.toString(),
            binding.spinnerP5.selectedItem.toString()
        )
        //adding a default name to the selected lanes if the user did not write down a name
        val hero1 = binding.P1Hero.text.toString().ifEmpty { "Hero 1" }
        val hero2 = binding.P2Hero.text.toString().ifEmpty { "Hero 2" }
        val hero3 = binding.P3Hero.text.toString().ifEmpty { "Hero 3" }
        val hero4 = binding.P4Hero.text.toString().ifEmpty { "Hero 4" }
        val hero5 = binding.P5Hero.text.toString().ifEmpty { "Hero 5" }
        val validLanes = selections.filter { !it.contains("Select Lane") }
        val uniqueLanes = validLanes.distinct().size
        //seeing if the checkboxes are checked
        val hasCC = binding.cbCC.isChecked
        val hasBurst = binding.cbBurst.isChecked
        val hasSustain = binding.cbSustain.isChecked
        val hasMobility = binding.cbMobility.isChecked
        //math logic for the synergy score, one unique lane is 1 point multipled by ten, so 10 points
        //having five unique lanes is equal to a base score of 50
        var score = (uniqueLanes * 10)
        //add to the score depending on the checked checkboxes
        if (hasCC) score += 15
        if (hasBurst) score += 5
        if (hasSustain) score += 10
        if (hasMobility) score += 10
        if (score > 100) score = 100
        //all lanes must be filled
        val isDraftIncomplete = selections.any { it.contains("Select Lane") }
        if (isDraftIncomplete) score = 0

        //the selected sound file is determined by the synergy score
        val soundResource: Int
        val verdict: String
        when {
            isDraftIncomplete -> {
                verdict = "" //this is a placeholder since it will show a popup if the draft is incomplete
                soundResource = R.raw.error
            }
            score >= 90 -> {//the verdict is taken from string.xml depending on the score and will play the sound accordingly
                verdict = getString(R.string.mythic)
                soundResource = R.raw.mythic
            }
            score >= 70 -> {
                verdict = getString(R.string.legend)
                soundResource = R.raw.legend
            }
            score >= 50 -> {
                verdict = getString(R.string.epic)
                soundResource = R.raw.epic
            }
            else -> {
                verdict = getString(R.string.warrior)
                soundResource = R.raw.warrior
            }
        }

        //Play the selected sound
        playSound(soundResource)
        //will showcase the full review of the selected draft via popup.
        val draftSummary = """
            $hero1: ${selections[0]}
            $hero2: ${selections[1]}
            $hero3: ${selections[2]}
            $hero4: ${selections[3]}
            $hero5: ${selections[4]}
        """.trimIndent()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("${getString(R.string.resultTitle)} - $score%")
        if (isDraftIncomplete) {
            builder.setMessage(getString(R.string.incompleteMessage))
        } else {
            builder.setMessage("$verdict\n\n--- Lineup ---\n$draftSummary\n\nUnique Lanes: $uniqueLanes/5")
        }
        builder.setPositiveButton(getString(R.string.dialog), null)
        builder.show()
    }
    private fun playSound(resId: Int) {
        //this will ensure the previous sound player disappears after use, preventing data issues
        //this part is actually suggested by Gemini to avoid RAM issues during app use
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, resId)
        mediaPlayer?.start()
    }
    private fun showHelpPopup() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.instructionTitle))
            .setMessage(getString(R.string.instructionBody))
            .setPositiveButton(getString(R.string.dialog), null)
            .show()
    }
    override fun onDestroy() {
        super.onDestroy()
        //the media player is cleaned after app closes to prevent extra RAM use.
        mediaPlayer?.release()
        mediaPlayer = null
    }
}