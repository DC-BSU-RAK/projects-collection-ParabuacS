package com.example.charactercodex.data

import java.io.Serializable //This allows us to use gson later

data class Character(
    val name: String,
    val role: String,
    val bio: String
) : Serializable // Lets the data travel around