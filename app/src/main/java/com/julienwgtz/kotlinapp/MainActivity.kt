package com.julienwgtz.kotlinapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_result_scan.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    var aller = true
    val URL = "https://world.openfoodfacts.org/api/v0/product/"
    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userAllergiesReader = getSharedPreferences("com.julienwgtz.kotlinapp", Context.MODE_PRIVATE)
        if(!userAllergiesReader.getBoolean("gluten", false) && !userAllergiesReader.getBoolean("milk", false) && !userAllergiesReader.getBoolean("fish", false))
        {
            aller = false
        }
        if(aller)
        {
            setContentView(R.layout.activity_main)
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }
        else
        {
            startActivity(Intent(this, ChoiceAllergyActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choice_allergy, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.choice->
            {
                startActivity(Intent(this, ChoiceAllergyActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {

        if (resultCode == Activity.RESULT_OK)
        {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null)
            {
                if (result.contents == null)
                {
                    Toast.makeText(this, "Nous avons rien trouvé", Toast.LENGTH_LONG).show()

                }
                else
                {
                    // Requette API
                    //getInfoJsonFromApi("737628064502")
                    getInfoJsonFromApi(result.contents,this,this)
                }
            } else
            {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun getInfoJsonFromApi(codeBarre : String, context : Context,activity: Activity ) {
        val fullUrl = URL + codeBarre + ".json"
        val request: Request = Request.Builder().url(fullUrl).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) { }

            override fun onResponse(call: Call?, response: Response?) {
                val json = response?.body()?.string()
                if((JSONObject(json)["status_verbose"] as String) == "product found")
                {

                    val userAllergiesReader = getSharedPreferences("com.julienwgtz.kotlinapp", Context.MODE_PRIVATE)
                    val resultScan = ResultScan()
                    val bundle = Bundle()


                    val allergensString = (JSONObject(json).getJSONObject("product")["allergens"] as String)
                    if(allergensString.length > 0 )
                    {
                        val allergens = allergensString.split(",")

                        var allerges = HashMap<String, Boolean>()
                        allerges.put("milk" , false)
                        allerges.put("gluten",false)
                        allerges.put("fish" , false)
                        allerges.put("fish" , false)
                        allerges.put("mustard" , false)
                        allerges.put("egg" , false)


                        for (allergy in allergens)
                        {

                            if((userAllergiesReader.getBoolean("milk", false) && allergy.substringAfter(":") == "milk")  )
                            {
                                allerges["milk"] = true
                            }
                            if(userAllergiesReader.getBoolean("gluten", false) && allergy.substringAfter(":") == "gluten")
                            {
                                allerges["gluten"] = true
                            }

                            if(userAllergiesReader.getBoolean("fish", false) && allergy.substringAfter(":") == "fish")
                            {
                                allerges["fish"] = true
                            }
                            if(userAllergiesReader.getBoolean("mustard", false) && allergy.substringAfter(":") == "mustard")
                            {
                                allerges["mustard"] = true
                            }
                            if(userAllergiesReader.getBoolean("egg", false) && allergy.substringAfter(":") == "egg")
                            {
                                allerges["egg"] = true
                            }

                        }
                        bundle.putSerializable("HashMap",allerges);
                        resultScan.setArguments(bundle)
                    }

                    runOnUiThread {
                        val parsedValue = json
                        bundle.putString("bundleValue", parsedValue)
                        resultScan.setArguments(bundle)
                        val manager = supportFragmentManager
                        val transaction = manager.beginTransaction()
                        transaction.replace(R.id.fragment_container,resultScan)
                        transaction.addToBackStack(null)

                        // Finishing the transition
                        transaction.commitAllowingStateLoss();
                    }
                }
                else
                {
                    runOnUiThread {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Produit inconnu")
                        builder.setMessage("Désolé nous ne trouvons pas votre produit")
                        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                            val scanner = IntentIntegrator(activity)
                            scanner.initiateScan()
                        }
                        builder.show()
                    }
                }


            }
        })
    }

    override fun onBackPressed() {

        val scanner = IntentIntegrator(this)
        scanner.initiateScan()
    }
}
