package com.example.currencyconvertor

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var date1: TextView
    lateinit var toSpinner: Spinner
    lateinit var amount: EditText
    lateinit var convertBtn: Button
    lateinit var result: TextView
    private var curencyDetails: CurrencyDetails.Datum? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        date1 = findViewById(R.id.tvDate)
        toSpinner = findViewById(R.id.spinner2)
        amount = findViewById(R.id.amount)
        convertBtn = findViewById(R.id.button)
        result = findViewById(R.id.tvResult)

        val cur = Constants.myArray

        var selected: Int = 0
       if (toSpinner != null) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cur)
        toSpinner.adapter = adapter

        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                selected = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(applicationContext, "please select", Toast.LENGTH_SHORT).show();
            }
        }
        }

        convertBtn.setOnClickListener {

            var sel = amount.text.toString()
            var currency: Double = sel.toDouble()

            getCurrency(onResult = {
                curencyDetails = it

                when (selected) {
                    0 -> disp(calc(curencyDetails?.eur?.inr?.toDouble(), currency));
                    1 -> disp(calc(curencyDetails?.eur?.usd?.toDouble(), currency));
                    2 -> disp(calc(curencyDetails?.eur?.aud?.toDouble(), currency));
                    3 -> disp(calc(curencyDetails?.eur?.sar?.toDouble(), currency));
                    4 -> disp(calc(curencyDetails?.eur?.cny?.toDouble(), currency));
                    5 -> disp(calc(curencyDetails?.eur?.jpy?.toDouble(), currency));
                }
            })
        }
        date1.text = "Date:  ${curencyDetails?.date.toString()}"

    }

    private fun disp(calc: Double) {

        result.text = "Result:  " + calc
    }

    private fun calc(i: Double?, sel: Double): Double {
        var s = 0.0
        if (i != null) {
            s = (i * sel)
        }
        return s
    }

    private fun getCurrency(onResult: (CurrencyDetails.Datum?) -> Unit) {
        val apiInterface = APIClient.getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCurrency()?.enqueue(object : Callback<CurrencyDetails.Datum> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<CurrencyDetails.Datum>,
                    response: Response<CurrencyDetails.Datum>
                ) {
                    onResult(response.body())

                }

                override fun onFailure(call: Call<CurrencyDetails.Datum>, t: Throwable) {

                    Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
                    call.cancel()
                }

            })
        }
    }
}