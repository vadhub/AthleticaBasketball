package com.vlg.athletica.presentation.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.vlg.athletica.R
import com.vlg.athletica.data.remote.RemoteInstance
import com.vlg.athletica.data.repository.EventRepository
import com.vlg.athletica.data.repository.SpotRepository
import com.vlg.athletica.data.repository.VoteRepository
import com.vlg.athletica.databinding.FragmentMapBinding
import com.vlg.athletica.model.Event
import com.vlg.athletica.model.SpotResponse
import com.vlg.athletica.model.TimeSlot
import com.vlg.athletica.presentation.BaseFragment
import com.vlg.athletica.presentation.EventViewModel
import com.vlg.athletica.presentation.EventViewModelFactory
import com.vlg.athletica.presentation.SpotsViewModel
import com.vlg.athletica.presentation.SpotsViewModelFactory
import com.vlg.athletica.presentation.VoteViewModel
import com.vlg.athletica.presentation.VoteViewModelFactory
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlin.math.floor


class MapFragment : BaseFragment(), InputListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private val spotViewModel: SpotsViewModel by activityViewModels {
        SpotsViewModelFactory(SpotRepository(RemoteInstance))
    }

    private val eventViewModel: EventViewModel by activityViewModels {
        EventViewModelFactory(EventRepository(RemoteInstance))
    }

    private val voteViewModel: VoteViewModel by activityViewModels {
        VoteViewModelFactory(VoteRepository(RemoteInstance))
    }

    private val placemarkTapListener = MapObjectTapListener { _, point ->
        spotViewModel.getSpotByLatAndLon(floorToSixAfterDot(point.latitude).toString(), floorToSixAfterDot(point.longitude).toString())
        val dialog = BottomSheetDialogSpot(thisContext, voteViewModel, viewLifecycleOwner, configuration.getIdUser())
        dialog.setView()
        spotViewModel.spot.observe(viewLifecycleOwner) {
            dialog.setField(it) { idSpot ->
                startDialogForCreateSlot(idSpot) { timeSlot, name, i ->
                    eventViewModel.saveEvent(Event(0, configuration.getIdUser(), timeSlot, name, i, ""))
                }
            }
        }
        dialog.show()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(thisContext)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mapView = binding.mapview
        mapView.map.move(
            CameraPosition(
                Point(48.641626, 44.429012),
                18f,
                0f,
                0f
            )
        )

        spotViewModel.spots.observe(viewLifecycleOwner) { spots ->
            spots.forEach {
                val placemark = mapView.map.mapObjects.addPlacemark().apply {
                    geometry = Point(it.lat.toDouble(), it.lon.toDouble())
                    setIcon(ImageProvider.fromResource(thisContext, R.drawable.ic_action_name))
                }
                placemark.addTapListener(placemarkTapListener)
            }
        }

        mapView.mapWindow.map.addInputListener(this)
        spotViewModel.getSpots()

    }

    private fun createAddSpotDialog(saveSpot: (name: String, description: String) -> Unit) {
        val addSpotDialog = AddSpotDialogFragment(saveSpot)
        addSpotDialog.show(childFragmentManager, "Add Spot Fragment")
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onMapTap(p0: Map, p1: Point) {}

    override fun onMapLongTap(p0: Map, p1: Point) {
        Toast.makeText(thisContext, p1.toString(), Toast.LENGTH_SHORT).show()
        val placemark = p0.mapObjects.addPlacemark().apply {
            createAddSpotDialog {name, description ->
                val lat = floorToSixAfterDot(p1.latitude)
                val lon = floorToSixAfterDot(p1.longitude)
                Log.d("###", "$lat $lon")
                spotViewModel.saveSpot(SpotResponse(0, name, lat.toString(), lon.toString(), description, null, null))
                geometry = p1
                setIcon(ImageProvider.fromResource(thisContext, R.drawable.ic_action_name))
            }
        }
        placemark.addTapListener(placemarkTapListener)
    }

    private fun floorToSixAfterDot(value: Double) = floor(value * 10000.0) / 10000.0

    private fun startDialogForCreateSlot(spotId: Long, saveSlot: (TimeSlot, String, Int) -> Unit) {
        val addTimeSlotDialogFragment = AddTimeSlotDialogFragment(saveSlot, spotId)
        addTimeSlotDialogFragment.show(childFragmentManager, "Time slot add")
    }

}