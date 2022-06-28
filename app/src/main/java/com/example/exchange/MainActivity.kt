package com.example.exchange


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*


import org.json.JSONArray
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var currency_1: TextView? = null
    private var currency_2: TextView? = null
    private var convert_btn: Button? = null
    private var currency_1_value: EditText? = null
    private var currency_2_value: TextView? = null

    private var cost: EditText? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currency_1 = findViewById(R.id.currency_1)
        currency_2 = findViewById(R.id.currency_2)
        convert_btn = findViewById(R.id.convert_btn)
        currency_1_value = findViewById(R.id.currency_1_value)
        currency_2_value = findViewById(R.id.currency_2_value)
        cost = findViewById(R.id.cost)

        convert_btn?.setOnClickListener {
            if (currency_1_value?.text?.toString()?.trim()?.equals("")!!)
            {
                Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show()
            }
            else if(cost?.text?.toString()?.trim()?.equals("")!!)
            {
                Toast.makeText(this, "Please enter a cost", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val currency_сс: String = currency_2?.text?.toString()?.trim()!!


                /*GlobalScope.launch {
                    var currency_name: String = withContext(Dispatchers.IO) { getCurrencyName(currency_сс) }
                    var currency_value: String = withContext(Dispatchers.IO) { getCurrencyValue(currency_сс) }


                    val currency_1_value_float = currency_1_value?.text?.toString()?.toFloat()!!
                    val currency_value_float = currency_value.toFloat()

                    val currency_2_value_float = currency_1_value_float * currency_value_float

                    runOnUiThread {
                        currency_2_value?.setText(currency_2_value_float.toString())
                    }
                }*/

                val currency_name: String = getCurrencyName(currency_сс)
                val currency_value: String = getCurrencyValue(currency_сс)


                val currency_1_value_float = currency_1_value?.text?.toString()?.toFloat()!!
                val currency_value_float = currency_value.toFloat()

                val currency_2_value_float = currency_1_value_float * currency_value_float

                currency_2_value?.setText(currency_2_value_float.toString())


            }

        }


    }
<<<<<<< Updated upstream
=======

    fun getCurrencyName(currency_сс: String): String {

        val url: String = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json"
        var currency_name: String = ""

        val response = URL(url).readText()

        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            if (jsonObject.getString("cc").equals(currency_сс)) {
                currency_name = jsonObject.getString("txt")
            }
        }
        return currency_name
    }

    fun getCurrencyValue(currency_сс: String): String {

        val url: String = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json"
        var currency_value: String = ""

        val response = URL(url).readText()

        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            if (jsonObject.getString("cc").equals(currency_сс)) {
                currency_value = jsonObject.getString("rate")
            }
        }
        return currency_value
    }
>>>>>>> Stashed changes
}