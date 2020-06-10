package com.julienwgtz.kotlinapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_choice_allergy.*


class ChoiceAllergyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_allergy)
        val userAllergies = getSharedPreferences("com.julienwgtz.kotlinapp", Context.MODE_PRIVATE)

        if (userAllergies.getBoolean("milk", false))
        {
            milk_allergy_switch.setChecked(true)
        }
        if (userAllergies.getBoolean("gluten", false))
        {
            gluten_allergy_switch.setChecked(true)
        }
        if (userAllergies.getBoolean("fish", false))
        {
            fish_allergy_switch.setChecked(true)
        }
        if (userAllergies.getBoolean("mustard", false))
        {
            mustard_allergy_switch.setChecked(true)
        }
        if (userAllergies.getBoolean("egg", false))
        {
            egg_allergy_switch.setChecked(true)
        }
    }

    fun saveAllergies(saveButton:View){
        // creation of shared preferences editor
        val userAllergies = getSharedPreferences("com.julienwgtz.kotlinapp", Context.MODE_PRIVATE).edit()
        // if an allergy is checked, set it true in shared preferences
        if (milk_allergy_switch.isChecked){
            userAllergies.putBoolean("milk", true)
        }
        if (gluten_allergy_switch.isChecked){
            userAllergies.putBoolean("gluten", true)
        }
        if (fish_allergy_switch.isChecked){
            userAllergies.putBoolean("fish", true)
        }
        if (mustard_allergy_switch.isChecked){
            userAllergies.putBoolean("mustard", true)
        }
        if (egg_allergy_switch.isChecked){
            userAllergies.putBoolean("egg", true)
        }
        // if an allergy is not checked, set it false in shared preferences
        if (!milk_allergy_switch.isChecked){
            userAllergies.putBoolean("milk", false)
        }
        if (!gluten_allergy_switch.isChecked){
            userAllergies.putBoolean("gluten", false)
        }
        if (!fish_allergy_switch.isChecked){
            userAllergies.putBoolean("fish", false)
        }
        if (!mustard_allergy_switch.isChecked){
            userAllergies.putBoolean("mustard", false)
        }
        if (!egg_allergy_switch.isChecked){
            userAllergies.putBoolean("egg", false)
        }
        // save shared preferences
        userAllergies.apply()
        startActivity(Intent(this, MainActivity::class.java))
    }
}
