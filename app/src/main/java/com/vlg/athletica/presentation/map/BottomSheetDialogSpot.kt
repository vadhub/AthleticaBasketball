package com.vlg.athletica.presentation.map

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vlg.athletica.R
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.model.SpotResponse

class BottomSheetDialogSpot(private val context: Context) : BottomSheetDialog(context) {

    private lateinit var subscribe: Button
    private lateinit var name: TextView
    private lateinit var close: ImageView
    private lateinit var address: TextView
    private lateinit var slotList: LinearLayout

    private lateinit var slot: TextView
    private lateinit var back: ImageView
    private lateinit var textAsk: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButtonYes: RadioButton
    private lateinit var radioButtonNo: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

        subscribe = view.findViewById(R.id.subscribe)
        name = view.findViewById(R.id.spotName)
        close = view.findViewById(R.id.close)
        address = view.findViewById(R.id.address)
        slotList = view.findViewById(R.id.slots)

        slot = view.findViewById(R.id.slot)
        back = view.findViewById(R.id.back)
        textAsk = view.findViewById(R.id.textAsk)
        radioGroup = view.findViewById(R.id.radioGroup)
        radioButtonNo = view.findViewById(R.id.no)
        radioButtonYes = view.findViewById(R.id.yes)

        back.setOnClickListener {
            visibilityChange(false)
        }

        setContentView(view)
        setCancelable(false)

        close.setOnClickListener { dismiss() }
    }

    fun setField(resource: Resource<SpotResponse>, addNewSpot: () -> Unit) {
        when (resource) {
            is Resource.Loading -> {}

            is Resource.Success -> {
                name.text = resource.result.name
                address.text = resource.result.address
                resource.result.slots?.forEach {
                    slotList.addView(createSlot(it.startTime, it.availability) {
                        visibilityChange(true)
                    })
                }

                slotList.addView(createSlot(context.getString(R.string.create_new_slot), 0) {
                    addNewSpot.invoke()
                })
                Log.d("@@@@", resource.result.toString())
            }

            is Resource.Failure -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_loading),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createSlot(time: String, available: Int, clickListener: () -> Unit) : View {
        val view: View = layoutInflater.inflate(R.layout.slot, null)
        val timeTextView = view.findViewById<TextView>(R.id.startDateSlot)
        val availableTextView = view.findViewById<TextView>(R.id.available)

        timeTextView.text = time
        if (available == 0) {
            availableTextView.text = context.getText(R.string.not_available)
            availableTextView.isClickable = false
            availableTextView.setTextColor(context.resources.getColor(R.color.non_available))
        } else {
            availableTextView.text = context.getText(R.string.not_available)
            availableTextView.setOnClickListener { clickListener.invoke() }
        }

        return view
    }

    private fun visibilityChange(isSlot: Boolean) {
        if (isSlot) {
            slot.visibility = View.VISIBLE
            back.visibility = View.VISIBLE
            textAsk.visibility = View.VISIBLE
            radioGroup.visibility = View.VISIBLE
            radioButtonNo.visibility = View.VISIBLE
            radioButtonYes.visibility = View.VISIBLE

            subscribe.visibility = View.GONE
            slotList.visibility = View.GONE
        } else {
            slot.visibility = View.GONE
            back.visibility = View.GONE
            textAsk.visibility = View.GONE
            radioGroup.visibility = View.GONE
            radioButtonNo.visibility = View.GONE
            radioButtonYes.visibility = View.GONE

            subscribe.visibility = View.VISIBLE
            slotList.visibility = View.VISIBLE

        }
    }
}