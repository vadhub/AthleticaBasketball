package com.vlg.athletica.presentation.map

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vlg.athletica.R
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.model.TimeSlot
import com.vlg.athletica.model.SpotResponse
import com.vlg.athletica.model.Vote
import com.vlg.athletica.presentation.VoteViewModel

class BottomSheetDialogSpot(
    private val context: Context,
    private val voteViewModel: VoteViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val userId: Long
) : BottomSheetDialog(context), CompoundButton.OnCheckedChangeListener {

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
    private lateinit var countIsCome: TextView
    private var vote = 0
    private var slotId = -1L

    fun setView() {
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
        countIsCome = view.findViewById(R.id.comeCount)

        back.setOnClickListener {
            visibilityChange(false, TimeSlot.empty())
        }

        setContentView(view)
        setCancelable(false)

        close.setOnClickListener { dismiss() }

        voteViewModel.voteLiveData.observe(viewLifecycleOwner) {
            countIsCome.text  = context.getString(R.string.come) + "${vote++}"
        }

        voteViewModel.voteOneLiveData.observe(viewLifecycleOwner) {
            if (it.isCome == 1) {
                radioButtonYes.isChecked = true
            } else {
                radioButtonNo.isChecked = true
            }
        }
    }


    fun setField(resource: Resource<SpotResponse>, addNewSpot: (spotId: Long) -> Unit) {
        when (resource) {
            is Resource.Loading -> {}

            is Resource.Success -> {
                name.text = resource.result.name
                address.text = resource.result.address
                resource.result.timeSlots?.forEach {
                    slotList.addView(createSlot(it.startTime, it.availability) {
                        visibilityChange(true, it)
                    })
                }

                slotList.addView(createSlotButton(context.getString(R.string.create_new_slot)) {
                    addNewSpot.invoke(resource.result.idSpot)
                })
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

        timeTextView.text = time.substring(0, 5)
        if (available == 0) {
            availableTextView.text = context.getText(R.string.not_available)
            timeTextView.isClickable = false
            availableTextView.setTextColor(context.resources.getColor(R.color.non_available))
        } else {
            availableTextView.text = context.getText(R.string.available)
            timeTextView.setOnClickListener { clickListener.invoke() }
        }

        return view
    }

    private fun createSlotButton(text: String, clickListener: () -> Unit) : View {
        val view: View = layoutInflater.inflate(R.layout.slot, null)
        val timeTextView = view.findViewById<TextView>(R.id.startDateSlot)
        timeTextView.text = text
        timeTextView.setOnClickListener { clickListener.invoke() }
        return view
    }


    @SuppressLint("SetTextI18n")
    private fun visibilityChange(isSlot: Boolean, timeSlotEntity: TimeSlot) {
        if (isSlot) {
            slotId = timeSlotEntity.id
            slot.visibility = View.VISIBLE
            slot.text = timeSlotEntity.startTime.substring(0, 5)
            voteViewModel.getCountVoteBySlotId(timeSlotEntity.id)
            voteViewModel.getVote(userId, slotId)
            voteViewModel.voteCountLiveData.observe(viewLifecycleOwner) {
                vote = it
                countIsCome.text = context.getString(R.string.come) + "$it"
            }
            radioButtonYes.setOnCheckedChangeListener(this)
            radioButtonNo.setOnCheckedChangeListener(this)
            back.visibility = View.VISIBLE
            textAsk.visibility = View.VISIBLE
            radioGroup.visibility = View.VISIBLE
            radioButtonNo.visibility = View.VISIBLE
            radioButtonYes.visibility = View.VISIBLE
            countIsCome.visibility = View.VISIBLE

            subscribe.visibility = View.GONE
            slotList.visibility = View.GONE
        } else {
            slot.text = ""
            countIsCome.text = ""
            vote = 0
            slotId = -1
            slot.visibility = View.GONE
            back.visibility = View.GONE
            textAsk.visibility = View.GONE
            radioGroup.visibility = View.GONE
            radioButtonNo.visibility = View.GONE
            radioButtonYes.visibility = View.GONE
            countIsCome.visibility = View.GONE

            subscribe.visibility = View.VISIBLE
            slotList.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (buttonView == radioButtonYes) {
            voteViewModel.saveVote(Vote(userId, slotId, 1))
        } else if (buttonView == radioButtonNo) {
            voteViewModel.removeVote(userId, slotId)
        }
    }
}