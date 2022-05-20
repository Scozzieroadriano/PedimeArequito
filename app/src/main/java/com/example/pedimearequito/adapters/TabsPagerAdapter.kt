package com.example.pedimearequito.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pedimearequito.fragments.client.ClientOrderStatusFragment

class TabsPagerAdapter (
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
                val clientStatusFragment = ClientOrderStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status", "DESPACHADO")
                val clientStatusFragment = ClientOrderStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            2 -> {
                val bundle = Bundle()
                bundle.putString("status", "EN CAMINO")
                val clientStatusFragment = ClientOrderStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            3 -> {
                val bundle = Bundle()
                bundle.putString("status", "ENTREGADO")
                val clientStatusFragment = ClientOrderStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            else -> return ClientOrderStatusFragment()

        }
    }

}


