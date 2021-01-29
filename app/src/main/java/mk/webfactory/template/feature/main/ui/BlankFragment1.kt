package mk.webfactory.template.feature.main.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_blank1.*
import mk.webfactory.template.R


class BlankFragment1 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_next_fragment.setOnClickListener { findNavController().navigate(BlankFragment1Directions.actionBlankFragment1ToBlankFragment2()) }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) = BlankFragment1().apply {}
    }
}