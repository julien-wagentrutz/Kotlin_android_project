package com.julienwgtz.kotlinapp

import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_result_scan.*
import org.json.JSONObject


class ResultScan : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    fun newInstance( message: String?): ResultScan? {
        val fragment = ResultScan()
        val bundle = Bundle(1)
        bundle.putString(EXTRA_MESSAGE, message)
        fragment.setArguments(bundle)
        return fragment
    }
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val json = arguments?.getString("bundleValue")
        val codeBar = (JSONObject(json)["code"] as String)
        val name = (JSONObject(json).getJSONObject("product")["product_name"] as String)
        val url = (JSONObject(json).getJSONObject("product")["image_url"] as String)
        val allergensString = (JSONObject(json).getJSONObject("product")["allergens"] as String)
        val view = inflater.inflate(R.layout.fragment_result_scan, container, false)
        Picasso.get().load(url).into(view.findViewById<ImageView>(R.id.imageView4))
        view.findViewById<TextView>(R.id.productName).setText(name)
        var checkTest = false;
        val extras = Bundle()
        if(extras != null) {
            val hashMap = arguments?.getSerializable("HashMap")
            if(hashMap != null) {
                hashMap as HashMap<String,Boolean>
                val ElementAller = view.findViewById<TextView>(R.id.contienAllergy)
                if (hashMap["milk"]!!) {

                    val text = view.findViewById<TextView>(R.id.allergyText)
                    ElementAller.visibility = View.VISIBLE
                    checkTest = true
                    text.text = text.text.toString() + "\n Du  Lait"
                }
                if (hashMap["gluten"]!!){
                    val text = view.findViewById<TextView>(R.id.allergyText)
                    ElementAller.visibility = View.VISIBLE
                    checkTest = true
                    text.text = text.text.toString() + " \n Du Gluten"
                }
                if (hashMap["fish"]!!) {

                    val text = view.findViewById<TextView>(R.id.allergyText)
                    ElementAller.visibility = View.VISIBLE
                    checkTest = true
                    text.text = text.text.toString() + " \n Du Poisson"
                }
                if (hashMap["mustard"]!!) {

                    val text = view.findViewById<TextView>(R.id.allergyText)
                    ElementAller.visibility = View.VISIBLE
                    checkTest = true
                    text.text = text.text.toString() + " \n De la moutard"
                }
                if (hashMap["egg"]!!) {

                    val text = view.findViewById<TextView>(R.id.allergyText)
                    ElementAller.visibility = View.VISIBLE
                    checkTest = true
                    text.text = text.text.toString() + " \n Des oeufs"
                }
            }
            if(checkTest)
            {
                val iconToSet = view.findViewById<ImageView>(R.id.checkResult)
                val iconCheck = resources.getDrawable(R.drawable.ic_checknotgood)
                iconToSet.setImageDrawable(iconCheck);
            }
        }


        return view
    }



}
