package com.example.exchange


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*


import org.json.JSONArray
import java.net.URL


class MainActivity : AppCompatActivity() {

    private val url: String = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json"

    private var currency_1: TextView? = null
    private var currency_2: TextView? = null
    private var convert_btn: Button? = null
    private var currency_1_value: EditText? = null
    private var currency_2_value: TextView? = null

    private var cost: EditText? = null

    private var currency_name: String? = null
    private var currency_value: Double? = null

    private var currencys_names = mutableListOf<String>()
    private var currencys_values = mutableListOf<Double>()
    private var currencys_cc = mutableListOf<String>()



    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currency_1 = findViewById(R.id.currency_1)
        currency_2 = findViewById(R.id.currency_2)
        convert_btn = findViewById(R.id.convert_btn)
        currency_1_value = findViewById(R.id.currency_1_value)
        currency_2_value = findViewById(R.id.currency_2_value)
        cost = findViewById(R.id.cost)

        CoroutineScope(Dispatchers.IO).launch {
            async { fillArrays() }.await()
        }


        convert_btn?.setOnClickListener {
            if (currency_1_value?.text?.toString()?.trim()?.equals("")!!)
            {
                Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show()
            }
            else
            {
                convert()
            }

        }


    }

    fun convert() {
        //val currency_сс: String = currency_2?.text?.toString()?.trim()!!
        val currency_сс: String = "USD"


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


        /*GlobalScope.launch {
            val job1 = launch { currency_name = getCurrencyName(currency_сс) }
            val job2 = launch { currency_value = getCurrencyValue(currency_сс) }
            job1.join()
            job2.join()


            Log.d("currency_name", currency_name!!)
            Log.d("currency_value", currency_value!!)



            currency_2?.setText(currency_name)!!

            val currency_1_value_double = currency_1_value?.text?.toString()?.toDouble()!!
            val currency_value_double = currency_value?.toDouble()!!

            val currency_2_value_float = currency_1_value_double * currency_value_double

            currency_2_value?.setText(currency_2_value_float.toString())
        }*/
        currency_name = getCurrencyName(currency_сс)
        currency_value = getCurrencyValue(currency_сс)


        Log.d("currency_name", currency_name!!)
        Log.d("currency_value", currency_value.toString())


        currency_2?.setText(currency_name)!!

        val currency_1_value_double = currency_1_value?.text?.toString()?.toDouble()!!

        val currency_2_value_float = currency_1_value_double * currency_value!!

        currency_2_value?.setText(currency_2_value_float.toString())
    }

    suspend fun fillArrays() {
        val response = URL(url).readText()
        val jsonArray = JSONArray(response)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            currencys_names.add(jsonObject.getString("txt"))
            currencys_values.add(jsonObject.getDouble("rate"))
            currencys_cc.add(jsonObject.getString("cc"))
        }
    }

    fun getCurrencyName(currency_сс: String): String {
        var currency_name: String = ""
        for (i in 0 until currencys_cc.size) {
            if (currencys_cc[i].equals(currency_сс)) {
                currency_name = currencys_names[i]
            }
        }
        return currency_name
    }

    fun getCurrencyValue(currency_сс: String): Double {
        var currency_value: Double = 0.0
        for (i in 0 until currencys_cc.size) {
            if (currencys_cc[i].equals(currency_сс)) {
                currency_value = currencys_values[i]
            }
        }
        return currency_value
    }

}