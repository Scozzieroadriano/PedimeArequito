package com.example.pedimearequito.activities.client.payments.payment_method

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.client.payments.form.ClientPaymentsFormActivity


class ClientPaymentMethodActivity : AppCompatActivity() {

    var imageViewMercadoPago: ImageView? = null
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payment_method)

        imageViewMercadoPago = findViewById(R.id.imageview_mercadopago)

        toolbar = findViewById(R.id.toolbar)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar?.title = "Metodos de pago"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageViewMercadoPago?.setOnClickListener { goToMercadoPago() }


    }

    private fun goToMercadoPago(){
        val i = Intent(this, ClientPaymentsFormActivity::class.java)
        startActivity(i)
    }

}