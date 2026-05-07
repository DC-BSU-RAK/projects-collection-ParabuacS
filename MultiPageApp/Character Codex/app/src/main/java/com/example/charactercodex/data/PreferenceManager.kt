package com.example.charactercodex.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.charactercodex.data.Character as MyCharacter



object PreferenceManager {
    // This file is a different type of kotlin file, which saves user preferences
    // We start off by using constants to make sure the saved data is the same
    private const val PREFS_NAME = "character_codex_prefs"
    private const val KEY_CHARACTERS = "character_list"
    // A gson is a java library that converts the objects into JSON objects to save them in the file
    private val gson = Gson()

    fun saveCharacter(context: Context, character: MyCharacter) {
        val characterList = getCharacters(context).toMutableList()
        characterList.add(character)

        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        // Converting the data into JSON
        val jsonString = gson.toJson(characterList)
        editor.putString(KEY_CHARACTERS, jsonString)
        editor.apply()
    }
    // Gets the character information, whether it is initial creation or editing
    fun getCharacters(context: Context): List<MyCharacter> {
        val type = object : TypeToken<List<MyCharacter>>() {}.type
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(KEY_CHARACTERS, null) ?: return emptyList()
        return gson.fromJson(jsonString, type)
    }
    // Character info is updated accordingly for that specific persona
    fun updateCharacter(context: Context, character: MyCharacter, position: Int) {
        val characters = getCharacters(context).toMutableList()
        if (position >= 0 && position < characters.size) {
            characters[position] = character // Replace the old version with the new one, basically delete and add new stuff
            val jsonString = gson.toJson(characters)
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putString(KEY_CHARACTERS, jsonString).apply()
        }
    }
}