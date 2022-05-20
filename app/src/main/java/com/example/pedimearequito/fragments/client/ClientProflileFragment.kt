package com.example.pedimearequito.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pedimearequito.R
import com.example.pedimearequito.activities.MainActivity
import com.example.pedimearequito.activities.SelectRolesActivity
import com.example.pedimearequito.activities.client.update.ClientUpdateActivity
import com.example.pedimearequito.models.Category
import com.example.pedimearequito.models.User
import com.example.pedimearequito.utils.SharedPref
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView


class ClientProflileFragment : Fragment() {

    var myView: View? = null
    var buttonSelectRol: Button? = null
    var buttonUpdateProfile: Button? = null
    var circleImageUser: CircleImageView? = null
    var textViewName: TextView? = null
    var textViewEmail: TextView? = null
    var textViewPhone: TextView? = null
    var sharedPref: SharedPref? = null
    var user: User? = null
    var buttonLogOut: Button? = null





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_client_proflile, container, false)

        sharedPref= SharedPref(requireActivity())


        buttonLogOut= myView?.findViewById(R.id.imgview_logout)
        buttonSelectRol= myView?.findViewById(R.id.btn_select_rol)
        buttonUpdateProfile=myView?.findViewById(R.id.btn_update_profile)
        textViewName=myView?.findViewById(R.id.txvnameclient_fragment)
        textViewEmail=myView?.findViewById(R.id.txv_email_fragment)
        textViewPhone=myView?.findViewById(R.id.txvphone_fragment)
        circleImageUser=myView?.findViewById(R.id.circleimage_user_fragment)


        buttonSelectRol?.setOnClickListener { goToSelectRoles() }
        buttonLogOut?.setOnClickListener{logout()}
        buttonUpdateProfile?.setOnClickListener { goToUpdate() }
        getUserFromSession()

        textViewName?.text= "${user?.name}  ${user?.lastname}"
        textViewEmail?.text = user?.email
        textViewPhone?.text = user?.phone

        if(!user?.image.isNullOrBlank()) {
            Glide.with(requireContext()).load(user?.image).into(circleImageUser!!)

        }
        return  myView
    }
    private  fun getUserFromSession(){

        val gson= Gson()

        if (!sharedPref?.getdata("user").isNullOrBlank()){
            //SI EL USUARIO EXISTE EN SESION, OBTENGO INFO
           user= gson.fromJson(sharedPref?.getdata("user"), User::class.java)

        }
    }

    private fun goToUpdate() {
        val i = Intent(requireContext(),ClientUpdateActivity::class.java)
        startActivity(i)
    }

    private fun goToSelectRoles(){
        val i= Intent(requireContext(),SelectRolesActivity::class.java)
        i.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)

    }

    private fun logout(){
        sharedPref?.remove("user")
        val i= Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
    }

}