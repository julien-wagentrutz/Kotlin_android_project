package com.julienwgtz.kotlinapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_result_scan.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val aller = true
    val URL = "https://world.openfoodfacts.org/api/v0/product/"
    var okHttpClient: OkHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(aller)
        {
            //setContentView(R.layout.activity_main)
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
            getInfoJsonFromApi("737628064502")
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

                    getInfoJsonFromApi("737628064502")
                    //getInfoJsonFromApi(result.contents)
                }
            } else
            {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun getInfoJsonFromApi(codeBarre : String) {
        val fullUrl = URL + codeBarre + ".json"
        val request: Request = Request.Builder().url(fullUrl).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) { }

            override fun onResponse(call: Call?, response: Response?) {
                val json = response?.body()?.string()
                val content = (JSONObject(json)["code"] as String)

                runOnUiThread {
                    // Afficher la data reçu
                    setContentView(R.layout.fragment_result_scan)
                    textView.text = "chocolat"
                }

            }
        })
    }
}
