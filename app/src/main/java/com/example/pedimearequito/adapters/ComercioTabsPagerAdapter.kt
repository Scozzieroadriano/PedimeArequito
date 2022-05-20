package com.example.pedimearequito.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pedimearequito.fragments.client.ClientOrderStatusFragment
import com.example.pedimearequito.fragments.comercio.ComercioOrdersStatusFragment

class ComercioTabsPagerAdapter (
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        var numberOfTabs: Int
): FragmentStateAdapter(fragmentManager,lifecycle)
{
    override fun getItemCount(): Int {
        return numberOfTabs    }

    override fun createFragment(position: Int): Fragment {



        when(position) {
            0 -> {
                val bundle = Bundle()
                bundle.putString("status", "PAGADO")
                val fragment = ComercioOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status", "DESPACHADO")
                val fragment = ComercioOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("status", "EN CAMINO")
                val fragment = ComercioOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            3 -> {
                val bundle = Bundle()
                bundle.putString("status", "ENTREGADO")
                val fragment = ComercioOrdersStatusFragment()
                fragment.arguments = bundle
                return fragment
            }
            else -> return ComercioOrdersStatusFragment()

        }
    }

}


