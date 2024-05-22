package com.example.storyapp.ui.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentMapsBinding
import com.example.storyapp.ui.StoryViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private lateinit var mapsBinding: FragmentMapsBinding
    private val mapsViewModel: MapsViewModel by viewModels {
        StoryViewModelFactory.getInstance(requireContext())
    }

    private lateinit var gMaps: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        gMaps = googleMap
        val indonesia = LatLng(-0.7893, 113.9213)
        gMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesia, 5f))

        gMaps.uiSettings.isZoomControlsEnabled = true
        gMaps.uiSettings.isIndoorLevelPickerEnabled = true
        gMaps.uiSettings.isCompassEnabled = true
        gMaps.uiSettings.isMapToolbarEnabled = true
        gMaps.uiSettings.isMyLocationButtonEnabled = true

        getMyLocation()
        setMapsStyle()
        showStoriesLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapsBinding = FragmentMapsBinding.inflate(inflater, container, false)
        return mapsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapsViewModel.apply {
            getLocationStory()
            showLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }
        mapFragment.getMapAsync(callback)
    }

    private fun showStoriesLocation() {
        mapsViewModel.getLocationStory().observe(viewLifecycleOwner) { res ->
            res.listStory.forEach { story ->
                val latLng = LatLng(story.lat, story.lon)
                gMaps.addMarker(MarkerOptions().position(latLng).title(story.name))
            }
        }
    }

    private fun setMapsStyle() {
        try {
            val success = gMaps
                .setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.maps_style
                    )
                )
            if (!success) {
                showToast(getString(R.string.style_parsing_failed))
            }
        } catch (exception: Resources.NotFoundException) {
            showToast(getString(R.string.error_cannot_find_style_maps))
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getMyLocation()
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            gMaps.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        mapsBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}