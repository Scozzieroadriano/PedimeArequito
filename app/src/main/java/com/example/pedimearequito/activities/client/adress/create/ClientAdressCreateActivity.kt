package com.example.pedimearequito.activities.client.adress.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.client.adress.list.ClientAdressListActivity
import com.example.pedimearequito.activities.client.adress.map.ClientAdressMapActivity
import com.example.pedimearequito.models.Address
import com.example.pedimearequito.models.ResponseHttp
import com.example.pedimearequito.models.User
import com.example.pedimearequito.providers.AddressProvider
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientAdressCreateActivity : AppCompatActivity() {

    val TAG = "ClientAdressCreate"

    var toolbar: Toolbar? = null
    var editTextRefPoint: EditText? = null
    var editTextAddress: EditText? = null
    var editTextNeighborhood: EditText? = null
    var buttonCreateAddress: Button? = null

    var addressLat = 0.0
    var addressLng = 0.0

    var addressProvider: AddressProvider? = null
    var sharedPref: SharedPref? = null
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_adress_create)

        sharedPref = SharedPref(this)

        getUserFromSession()
        addressProvider = AddressProvider(user?.sessionToken!!)

        toolbar = findViewById(R.id.toolbar)
        editTextRefPoint = findViewById(R.id.edittext_ref_point)
        editTextAddress = findViewById(R.id.edittext_address)
        editTextNeighborhood = findViewById(R.id.edittext_neighborhood)
        buttonCreateAddress = findViewById(R.id.button_create_address)

        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar?.title = "Nueva direccion"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editTextRefPoint?.setOnClickListener { goToAddressMap() }
        buttonCreateAddress?.setOnClickListener { createAddress() }
    }

    private fun  getUserFromSession( ) {
        val gson = Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()) {
            // Si el usuario existe en sesion
            user = gson.fromJson(sharedPref?.getdata("user"), User::class.java)
        }
    }

    private fun createAddress() {
        val address = editTextAddress?.text.toString()
        val neighborhood = editTextNeighborhood?.text.toString()

        if (isValidForm(address, neighborhood)) {
            // Lanzar la peticion

            val addressModel = Address(
                address = address,
                neighborhood = neighborhood,
                idUser = user?.id!!,
                lat = addressLat,
                lng = addressLng
            )

            addressProvider?.create(addressModel)?.enqueue(object: Callback<ResponseHttp> {
                override fun onResponse(call: Call<ResponseHttp>, response: Response<ResponseHttp>) {

                    if (response.body() != null) {
                        Toast.makeText(this@ClientAdressCreateActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                        goToAddressList()
                    }
                    else {
                        Toast.makeText(this@ClientAdressCreateActivity, "Ocurrio un error en la peticion", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@ClientAdressCreateActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    private fun goToAddressList() {
        val i = Intent(this, ClientAdressListActivity::class.java)
        startActivity(i)
    }

    private fun isValidForm(address:String, neighborhood: String): Boolean {

        if (address.isNullOrBlank()) {
            Toast.makeText(this, "Ingresa la direccion", Toast.LENGTH_SHORT).show()
            return false
        }
        if (neighborhood.isNullOrBlank()) {
            Toast.makeText(this, "Ingresa el barrio o residencia", Toast.LENGTH_SHORT).show()
            return false
        }

        if (addressLat == 0.0) {
            Toast.makeText(this, "Selecciona el punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }
        if (addressLng == 0.0) {
            Toast.makeText(this, "Selecciona el punto de referencia", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val city = data?.getStringExtra("city")
            val address = data?.getStringExtra("address")
            val country = data?.getStringExtra("country")
            addressLat = data?.getDoubleExtra("lat", 0.0)!!
            addressLng = data?.getDoubleExtra("lng", 0.0)!!

            editTextRefPoint?.setText("$address $city")

            Log.d(TAG, "City: $city")
            Log.d(TAG, "Address: $address")
            Log.d(TAG, "Country: $country")
            Log.d(TAG, "Lat: $addressLat")
            Log.d(TAG, "Lng: $addressLng")
        }
    }

    private fun goToAddressMap () {

        val i = Intent(this, ClientAdressMapActivity::class.java)
        resultLauncher.launch(i)
    }
}