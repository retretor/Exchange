package com.example.design2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.exchange.CalculatorFragment
import com.example.exchange.ExchangeRatesFragment

class PageViewAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return CalculatorFragment()
            1 -> return ExchangeRatesFragment()
            else -> return CalculatorFragment()
        }
    }

}