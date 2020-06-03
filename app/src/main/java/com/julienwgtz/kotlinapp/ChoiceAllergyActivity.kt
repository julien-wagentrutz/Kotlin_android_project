package com.julienwgtz.kotlinapp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_choice_allergy.*


class ChoiceAllergyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_allergy)
    }

    fun saveAllergies(saveButton:View){
        // creation of shared preferences editor
        val userAllergies = getSharedPreferences("com.julienwgtz.kotlinapp", Context.MODE_PRIVATE).edit()
        // if an allergy is checked, set it true in shared preferences
        if (milk_allergy_switch.isChecked){
            userAllergies.putBoolean("milk_allergy", true)
        }
        if (wheat_allergy_switch.isChecked){
            userAllergies.putBoolean("wheat_allergy", true)
        }
        if (mustard_allergy_switch.isChecked){
            userAllergies.putBoolean("mustard_allergy", true)
        }
        // if an allergy is not checked, set it false in shared preferences
        if (!milk_allergy_switch.isChecked){
            userAllergies.putBoolean("milk_allergy", false)
        }
        if (!wheat_allergy_switch.isChecked){
            userAllergies.putBoolean("wheat_allergy", false)
        }
        if (!mustard_allergy_switch.isChecked){
            userAllergies.putBoolean("mustard_allergy", false)
        }
        // save shared preferences
        userAllergies.apply()
        // test
        showAllergies()
    }

    // test to see if it works
    fun showAllergies(){
        // creation of shared preferences reader
        val userAllergiesReader = getSharedPreferences("com.julienwgtz.kotlinapp", Context.MODE_PRIVATE)

        var milk_test = userAllergiesReader.getBoolean("milk_allergy", false)
        if (milk_test){
            testResult.text = "Allergique au lait"
        }

        var wheat_test = userAllergiesReader.getBoolean("wheat_allergy", false)
        if (wheat_test){
            testResult.text = "Allergique au blé"
        }

        var mustard_test = userAllergiesReader.getBoolean("mustard_allergy", false)
        if (mustard_test){
            testResult.text = "Allergique à la moutarde"
        }
    }
}
