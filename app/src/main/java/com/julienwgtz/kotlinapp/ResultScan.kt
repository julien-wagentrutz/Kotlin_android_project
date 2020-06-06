package com.julienwgtz.kotlinapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_result_scan.*
import org.json.JSONObject
import java.io.InputStream
import java.net.URL


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
        val name = (JSONObject(json).getJSONObject("product")["generic_name"] as String)
        val url = (JSONObject(json).getJSONObject("product")["image_thumb_url"] as String)
        val view = inflater.inflate(R.layout.fragment_result_scan, container, false)
        Picasso.get().load(url).into(view.findViewById<ImageView>(R.id.imageView4))
        view.findViewById<TextView>(R.id.productName).setText(name)
        return view
    }



}
