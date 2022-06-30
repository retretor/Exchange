package com.example.exchange


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.viewpager2.widget.ViewPager2
import com.example.design2.PageViewAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*

import org.json.JSONArray
import java.net.URL
import java.time.MonthDay
import java.util.*


class MainActivity : AppCompatActivity() {

    private val url: String = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json"

    private val c = Calendar.getInstance()

    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private val url2: String = "https://api.privatbank.ua/p24api/exchange_rates?json&date=$day.$month.$year"


    //calculation activity
    private var currency_1_name: TextView? = null
    private var currency_2_name: TextView? = null
    private var convert_btn: Button? = null
    private var currency_1_value: EditText? = null
    private var currency_2_value: TextView? = null

    private var cost: TextView? = null


    //navigation bar activity
    private var calculate_btn: Button? = null
    private var rates_btn: Button? = null

    private var calculate_btn_is_active: Boolean = true
    private var rates_btn_is_active: Boolean = false

    //currency list activity
    private var text_list_2: TextView? = null


    //variables for currency list activity

    private var currency_name: String? = null
    private var currency_value: Double? = null

    private var currencys_names_nbu = mutableListOf<String>()
    private var currencys_values_nbu = mutableListOf<Double>()
    private var currencys_cc_nbu = mutableListOf<String>()

    private var currencys_names_priv = mutableListOf<String>()
    private var currencys_value_buy_priv = mutableListOf<Double>()
    private var currencys_value_sale_priv = mutableListOf<Double>()
    private var currencys_cc_priv = mutableListOf<String>()

    private var tabTitle = arrayOf("Calculator", "Exchange rates")



    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //слайдер-менюшка
        var pager = findViewById<ViewPager2>(R.id.view_pager)
        var tl = findViewById<TabLayout>(R.id.tab_layout)
        pager.adapter = PageViewAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tl, pager) {
                tab, position ->
            tab.text = tabTitle[position]
        }.attach()



        currency_1_name = findViewById(R.id.currency_1)
        currency_2_name = findViewById(R.id.currency_2)
        convert_btn = findViewById(R.id.convert_btn)
        currency_1_value = findViewById(R.id.currency_1_value)
        currency_2_value = findViewById(R.id.currency_2_value)
        cost = findViewById(R.id.cost)

        calculate_btn = findViewById(R.id.calculate_btn)
        rates_btn = findViewById(R.id.rates_btn)
        text_list_2 = findViewById(R.id.text_list_sec)

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
        calculate_btn?.setOnClickListener {
            if(calculate_btn_is_active)
            {
                calculate_btn_is_active = false
                rates_btn_is_active = true

                text_list_2?.setVisibility(View.VISIBLE)
            }
        }
        rates_btn?.setOnClickListener {
            if(rates_btn_is_active)
            {
                calculate_btn_is_active = true
                rates_btn_is_active = false

                text_list_2?.setVisibility(View.INVISIBLE)
            }
        }



    }

    fun convert() {
        //val currency_сс: String = currency_2?.text?.toString()?.trim()!!
        val currency_сс: String = "USD" //ввод ??


        currency_name = getCurrencyName(currency_сс)
        currency_value = getCurrencyValue(currency_сс)


        Log.d("currency_name", currency_name!!)
        Log.d("currency_value", currency_value.toString())


        currency_2_name?.setText(currency_name)!!

        val currency_1_value_double = currency_1_value?.text?.toString()?.toDouble()!!

        val currency_2_value_float = currency_1_value_double * currency_value!!

        currency_2_value?.setText(currency_2_value_float.toString())
    }

    suspend fun fillArrays() {
        val response = URL(url).readText()
        val jsonArray = JSONArray(response)
        Log.d("jsonArray", jsonArray.toString())
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            currencys_names_nbu.add(jsonObject.getString("txt"))
            currencys_values_nbu.add(jsonObject.getDouble("rate"))
            currencys_cc_nbu.add(jsonObject.getString("cc"))
        }

        var response2 = URL(url2).readText()
        response2 = response2.substring(response2.indexOf("["), response2.indexOf("]") + 1)
        val jsonArray2 = JSONArray(response2)
        Log.d("jsonArray2", jsonArray2.toString())

        for (i in 1 until jsonArray2.length()) {
            val jsonObject = jsonArray2.getJSONObject(i)
            //if object "saleRate" doesnt exist, then pass this object
            if(jsonObject.has("saleRate") && jsonObject.has("purchaseRate"))
            {
                Log.d("jsonObject", "$i = $jsonObject")
                currencys_names_priv.add(jsonObject.getString("currency"))
                currencys_value_buy_priv.add(jsonObject.getDouble("purchaseRate"))
                currencys_value_sale_priv.add(jsonObject.getDouble("saleRate"))
                currencys_cc_priv.add(jsonObject.getString("currency"))
            }
        }



    }

    fun getCurrencyName(currency_сс: String): String {
        var currency_name: String = ""
        for (i in 0 until currencys_cc_nbu.size) {
            if (currencys_cc_nbu[i].equals(currency_сс)) {
                currency_name = currencys_names_nbu[i]
            }
        }
        return currency_name
    }

    fun getCurrencyValue(currency_сс: String): Double {
        var currency_value: Double = 0.0
        for (i in 0 until currencys_cc_nbu.size) {
            if (currencys_cc_nbu[i].equals(currency_сс)) {
                currency_value = currencys_values_nbu[i]
            }
        }
        return currency_value
    }

}