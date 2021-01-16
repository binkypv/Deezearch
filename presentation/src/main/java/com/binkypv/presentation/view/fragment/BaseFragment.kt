package com.binkypv.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.binkypv.presentation.R

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T

    protected val binding: T
        get() = _binding as T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return _binding.let { it?.root }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showError(msg: String? = null) {
        Toast.makeText(requireContext(),
            msg?.let { getString(R.string.default_error_message_specific, msg) }
                ?: getString(R.string.default_error_message),
            Toast.LENGTH_LONG).show()
    }

    fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}