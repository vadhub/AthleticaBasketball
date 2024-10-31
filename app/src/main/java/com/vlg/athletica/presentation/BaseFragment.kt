package com.vlg.athletica.presentation

import android.content.Context
import androidx.fragment.app.Fragment
import com.vlg.athletica.data.SaveConfiguration

open class BaseFragment : Fragment() {

    protected lateinit var thisContext: Context
    protected lateinit var configuration: SaveConfiguration

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.thisContext = context
        configuration = SaveConfiguration(thisContext)
    }

}