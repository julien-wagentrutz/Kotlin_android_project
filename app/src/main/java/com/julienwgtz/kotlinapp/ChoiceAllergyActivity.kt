package com.julienwgtz.kotlinapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
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
            wheat_allergy_switch.setChecked(true)
        }
        if (userAllergies.getBoolean("fish", false))
        {
            mustard_allergy_switch.setChecked(true)
        }


    }

    fun saveAllergies(saveButton:View){
        // creation of shared preferences editor
        val userAllergies = getSharedPreferences("com.julienwgtz.kotlinapp", Context.MODE_PRIVATE).edit()
        // if an allergy is checked, set it true in shared preferences
        if (milk_allergy_switch.isChecked){
            userAllergies.putBoolean("milk", true)
        }
        if (wheat_allergy_switch.isChecked){
            userAllergies.putBoolean("gluten", true)
        }
        if (mustard_allergy_switch.isChecked){
            userAllergies.putBoolean("fish", true)
        }
        // if an allergy is not checked, set it false in shared preferences
        if (!milk_allergy_switch.isChecked){
            userAllergies.putBoolean("milk", false)
        }
        if (!wheat_allergy_switch.isChecked){
            userAllergies.putBoolean("gluten", false)
        }
        if (!mustard_allergy_switch.isChecked){
            userAllergies.putBoolean("fish", false)
        }
        // save shared preferences
        userAllergies.apply()
        // test
        showAllergies()
        startActivity(Intent(this, MainActivity::class.java))
    }

    // test to see if it works
    fun showAllergies(){
        // creation of shared preferences reader
        val userAllergiesReader = getSharedPreferences("com.julienwgtz.kotlinapp", Context.MODE_PRIVATE)

        var milk_test = userAllergiesReader.getBoolean("milk", false)
        if (milk_test){
            testResult.text = "Allergique au lait"
        }

        var wheat_test = userAllergiesReader.getBoolean("gluten", false)
        if (wheat_test){
            testResult.text = "Allergique au gluten"
        }

        var mustard_test = userAllergiesReader.getBoolean("fish", false)
        if (mustard_test){
            testResult.text = "Allergique aux poissons"
        }
    }
}
