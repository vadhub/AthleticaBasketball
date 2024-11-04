package com.vlg.athletica.presentation.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.vlg.athletica.R
import com.vlg.athletica.databinding.FragmentDialogAddSlotTimeBinding
import com.vlg.athletica.model.TimeSlot


class AddTimeSlotDialogFragment(private val saveSlot: (TimeSlot, String, Int) -> Unit, private val spotId: Long) :
    DialogFragment(), TimePicker.OnTimeChangedListener {

    private var _binding: FragmentDialogAddSlotTimeBinding? = null
    private val binding get() = _binding!!
    private val setStartTime: TextView by lazy { binding.startTime }
    private val setEndTime: TextView by lazy { binding.endTime }

    private var countPlayer = 0

    private val nameEvent by lazy { binding.nameEventEditText }

    private val countText: TextView by lazy { binding.countText }
    private val countPlayerPicker: NumberPicker by lazy { binding.countPlayer }

    private val timePickerStart: TimePicker by lazy { binding.timePickerStart }
    private val timePickerEnd: TimePicker by lazy { binding.timePickerEnd }

    private val closeImageButton: ImageView by lazy { binding.close }
    private val addSpot: Button by lazy { binding.createSlot }

    private val startTimeText by lazy { binding.startTimeText }
    private val endTimeText by lazy { binding.endTimeText }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogAddSlotTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addSpot.setOnClickListener {
            saveSlot.invoke(
                TimeSlot(0,
                    spotId,
                    setStartTime.text.toString()+":00",
                    setEndTime.text.toString()+":00",
                    1
                ), nameEvent.text.toString(), countPlayer
            )
            dismiss()
        }

        countPlayerPicker.minValue = 3
        countPlayerPicker.maxValue = 10
        countPlayerPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            countPlayer = newVal
        }

        setStartTime.setOnClickListener {
            setVisibility(true)
            timePickerStart.visibility = View.VISIBLE
            startTimeText.visibility = View.VISIBLE
        }

        setEndTime.setOnClickListener {
            setVisibility(true)
            timePickerEnd.visibility = View.VISIBLE
            endTimeText.visibility = View.VISIBLE
        }

        timePickerStart.setOnTimeChangedListener(this)
        timePickerStart.setIs24HourView(true)
        timePickerEnd.setOnTimeChangedListener(this)
        timePickerEnd.setIs24HourView(true)

        closeImageButton.setOnClickListener {
            timePickerStart.visibility = View.GONE
            timePickerEnd.visibility = View.GONE
            endTimeText.visibility = View.GONE
            startTimeText.visibility = View.GONE
            setVisibility(false)
        }
    }

    private fun setVisibility(case: Boolean) {
        if (case) {
            addSpot.visibility = View.GONE
            closeImageButton.visibility = View.VISIBLE
            countText.visibility = View.GONE
            countPlayerPicker.visibility = View.GONE
            setEndTime.visibility = View.GONE
            setStartTime.visibility = View.GONE
            nameEvent.visibility = View.GONE
        } else {
            addSpot.visibility = View.VISIBLE
            closeImageButton.visibility = View.GONE
            setEndTime.visibility = View.VISIBLE
            setStartTime.visibility = View.VISIBLE
            countText.visibility = View.VISIBLE
            countPlayerPicker.visibility = View.VISIBLE
            nameEvent.visibility = View.VISIBLE
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

    @SuppressLint("SetTextI18n")
    override fun onTimeChanged(view: TimePicker, hourOfDay: Int, minute: Int) {
        var minuteNew = minute.toString()
        var hourNew = hourOfDay.toString()
        if (minute <= 9) {
            minuteNew = "0$minute"
        }
        if (hourOfDay <= 9) {
            hourNew = "0$hourNew"
        }
        if (view == timePickerStart) {
            setStartTime.text = "${getString(R.string.set_time_start)}: $hourNew:$minuteNew"
        } else if (view == timePickerEnd) {
            setEndTime.text = "${getString(R.string.set_time_end)}: $hourNew:$minuteNew"
        }
    }
}