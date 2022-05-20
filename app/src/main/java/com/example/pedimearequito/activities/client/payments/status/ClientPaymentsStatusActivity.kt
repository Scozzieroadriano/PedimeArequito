package com.example.pedimearequito.activities.client.payments.status

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.client.home.ClientHomeActivity
import de.hdodenhof.circleimageview.CircleImageView

class ClientPaymentsStatusActivity : AppCompatActivity() {

    var textViewStatus: TextView? = null
    var circleImageStatus: CircleImageView? = null
    var buttonFinish: Button? = null
    var textviewthanks: TextView? = null

    var paymentMethodId = ""
    var paymentStatus = ""
    var lastFourDigits = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_payments_status)

        textViewStatus = findViewById(R.id.textview_status)
        textviewthanks = findViewById(R.id.textview_thanks)
        circleImageStatus = findViewById(R.id.circle_image_status)
        buttonFinish = findViewById(R.id.btn_finish)

        paymentMethodId = intent.getStringExtra("paymentMethodId").toString()
        paymentStatus = intent.getStringExtra("paymentStatus").toString()
        lastFourDigits = intent.getStringExtra("lastFourDigits").toString()

        if (paymentStatus == "approved") {
            circleImageStatus?.setImageResource(R.drawable.ic_check)
            textViewStatus?.text = "Tu orden fue procesada exitosamente usando ( $paymentMethodId **** $lastFourDigits ) \n\nMira el estado de tu compra en la seccion de Mis Pedidos"
            textviewthanks?.text = "GRACIAS POR TU COMPRA!"
        }
        else {
            circleImageStatus?.setImageResource(R.drawable.ic_cancel)
            textViewStatus?.text = "Ocurrió un error al procesar el pago, verifique sus datos o pongase en contacto con su banco."
            textviewthanks?.text = "##ERROR!! "
        }

        buttonFinish?.setOnClickListener { goToHome() }

    }

    private fun goToHome() {
        val i = Intent(this, ClientHomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
}