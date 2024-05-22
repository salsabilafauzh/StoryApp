package com.example.storyapp.ui.submitStory

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import android.Manifest
import android.location.Location
import android.view.View
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ResponseGeneral
import com.example.storyapp.databinding.ActivitySubmitStoryBinding
import com.example.storyapp.ui.StoryViewModelFactory
import com.example.storyapp.ui.camera.CameraActivity
import com.example.storyapp.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File

class SubmitStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmitStoryBinding
    private val submitStoryViewModel: SubmitStoryViewModel by viewModels {
        StoryViewModelFactory.getInstance(this)
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var file: File? = null
    private var currentImageUri: Uri? = null
    private var isShareLocation: Boolean = false
    private lateinit var myLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = binding.topAppBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()
        setupAction()

        submitStoryViewModel.apply {
            isLoading().observe(this@SubmitStoryActivity) {
                showLoading(it)
            }
        }
    }


    private fun setupAction() {
        binding.apply {
            galleryButton.setOnClickListener {
                startGallery()
            }
            cameraButton.setOnClickListener {
                startCameraX()
            }
            postButton.setOnClickListener {
                createPost(isShareLocation)
            }
            locationSwitch.setOnCheckedChangeListener { _, isChecked ->
                isShareLocation = isChecked
            }
        }
    }

    private fun createPost(isShareLocation: Boolean) {
        file = currentImageUri?.let { uriToFile(it, this).reduceFileImage() }
        val description = binding.description.text.toString()
        file?.let { file ->
            if (isShareLocation) {
                submitStoryViewModel.postWithLocation(
                    file,
                    description,
                    myLocation.latitude,
                    myLocation.longitude
                ).observe(this@SubmitStoryActivity) {
                    checkResponse(it)
                }
            } else {
                submitStoryViewModel.postStory(file, description)
                    .observe(this@SubmitStoryActivity) { res ->
                        checkResponse(res)
                    }
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
            }
        }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    myLocation = location
                } else {
                    showToast(getString(R.string.location_is_not_found))
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    private fun startCameraX() {
        if (checkPermission(Manifest.permission.CAMERA)) {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA
                )
            )
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            currentImageUri?.let { uri -> showImage(uri) }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage(uri)
        } else {
            showToast(getString(R.string.no_media_selected))
        }
    }

    private fun showImage(uri: Uri) {
        binding.apply {
            imagePlaceholder.visibility = View.INVISIBLE
            previewImageView.setImageURI(uri)
        }
    }

    private fun checkResponse(res: ResponseGeneral) {
        if (res.error == false) {
            showToast(resources.getString(R.string.success_message))
            finish()
        } else {
            showToast(resources.getString(R.string.error_message))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}