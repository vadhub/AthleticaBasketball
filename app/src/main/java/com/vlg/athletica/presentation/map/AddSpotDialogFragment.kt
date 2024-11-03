package com.vlg.athletica.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.vlg.athletica.databinding.FragmentDialogAddSpotBinding


class AddSpotDialogFragment(private val saveSpot: (name: String, description: String) -> Unit): DialogFragment() {

    private var _binding: FragmentDialogAddSpotBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = FragmentDialogAddSpotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val spotText = binding.nameSpotEditText
        val spotDescription = binding.descriptionSpotEditText
        val addSpot = binding.createSpot
        addSpot.setOnClickListener {
            saveSpot.invoke(spotText.text.toString(), spotDescription.text.toString())
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }
}