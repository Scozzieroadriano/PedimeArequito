package com.example.pedimearequito.fragments.comercio

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.pedimearequito.R
import com.example.pedimearequito.adapters.ComercioTabsPagerAdapter
import com.example.pedimearequito.adapters.TabsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ComercioOrdersFragment : Fragment() {
    var myView: View? = null

    var viewpager: ViewPager2? = null
    var tabLayout: TabLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_comercio_orders, container, false)

        viewpager= myView?.findViewById(R.id.viewpager2)
        tabLayout= myView?.findViewById(R.id.tab_layout)

        tabLayout?.setSelectedTabIndicatorColor(Color.BLACK)
        tabLayout?.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
        tabLayout?.tabTextColors = ContextCompat.getColorStateList(requireContext(), R.color.black)
        tabLayout?.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout?.isInlineLabel = true

        var numberOfTabs = 4

        val adapter = ComercioTabsPagerAdapter(requireActivity().supportFragmentManager, lifecycle, numberOfTabs)

        viewpager?.adapter = adapter
        viewpager?.isUserInputEnabled= true

        TabLayoutMediator(tabLayout!!, viewpager!!){tab, position ->

            when(position){
                0 ->{
                    tab.text= "PAGADO"
                }
                1 ->{
                    tab.text= "DESPACHADO"
                }
                2 ->{
                    tab.text= "EN CAMINO"
                }
                3 ->{
                    tab.text= "ENTREGADO"
                }
            }
        }.attach()

        return myView
    }



}