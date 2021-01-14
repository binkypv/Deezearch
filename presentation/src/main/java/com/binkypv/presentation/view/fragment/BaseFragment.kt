package com.binkypv.presentation.view.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.binkypv.presentation.R

open class BaseFragment : Fragment() {

    fun showError(msg: String? = null) {
        Toast.makeText(requireContext(),
            msg ?: getString(R.string.default_error_message),
            Toast.LENGTH_LONG).show()
    }
}