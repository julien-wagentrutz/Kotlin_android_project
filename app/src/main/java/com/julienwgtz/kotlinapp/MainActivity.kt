package com.julienwgtz.kotlinapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val aller = true
    val URL = "https://world.openfoodfacts.org/api/v0/product/"
    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val sharedPref = getPreferences(Context.MODE_PRIVATE)

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
                    getInfoJsonFromApi(result.contents)
                }
            } else
            {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun getInfoJsonFromApi(codeBarre : String ) {
        val fullUrl = URL + codeBarre + ".json"
        val request: Request = Request.Builder().url(fullUrl).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) { }

            override fun onResponse(call: Call?, response: Response?) {
                val json = response?.body()?.string()
                val content = (JSONObject(json)["code"] as String)
                val name = (JSONObject(json).getJSONObject("product")["generic_name"] as String)

                runOnUiThread {
                    val parsedValue = json
                    val bundle = Bundle()
                    bundle.putString("bundleValue", parsedValue)
                    val resultScan = ResultScan()
                    resultScan.setArguments(bundle)

                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.fragment_container,resultScan)
                    transaction.addToBackStack(null)

                    // Finishing the transition
                    transaction.commitAllowingStateLoss();
                }

            }
        })
    }

    override fun onBackPressed() {

        val scanner = IntentIntegrator(this)
        scanner.initiateScan()
    }
}
