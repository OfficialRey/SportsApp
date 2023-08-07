package com.sportsapp.activity.main.ui.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sportsapp.R
import com.sportsapp.databinding.FragmentAddBinding
import com.sportsapp.logic.GlobalValues
import com.sportsapp.logic.models.Address
import com.sportsapp.logic.models.SportPlace


class AddFragment : Fragment() {

    private lateinit var invalidInput: TextView
    private lateinit var street: TextView
    private lateinit var houseNumber: TextView
    private lateinit var postalCode: TextView
    private lateinit var city: TextView

    private lateinit var photoPreview: ImageView
    private lateinit var photoIcon: ImageView
    private lateinit var imageIcon: ImageView
    private lateinit var createSpotButton: Button

    private var photo: Bitmap? = null


    private lateinit var dropDown: Spinner
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        val root = binding.root

        invalidInput = root.findViewById(R.id.inputError)

        // Initialise text fields
        street = root.findViewById(R.id.locationStreet)
        houseNumber = root.findViewById(R.id.locationNumber)
        postalCode = root.findViewById(R.id.locationPostalCode)
        city = root.findViewById(R.id.locationCity)

        // Initialise drop-down menu
        dropDown = root.findViewById(R.id.placeTypeSpinner)
        arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            GlobalValues.getSportPlaceLogic()!!.loadSportPlaceList()
        )
        dropDown.adapter = arrayAdapter

        // Initialise photo option
        photoPreview = root.findViewById(R.id.photoPreview)

        // Take picture
        photoIcon = root.findViewById(R.id.addSpotTakePhoto)
        photoIcon.setOnClickListener(ClickPhotoIconListener())

        // Select image from gallery
        imageIcon = root.findViewById(R.id.addSpotUploadImage)
        imageIcon.setOnClickListener(ClickImageIconListener())

        // Initialise button
        createSpotButton = root.findViewById(R.id.createSpotButton)
        createSpotButton.setOnClickListener(CreateSpotListener())

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ClickPhotoIconListener : OnClickListener {
        override fun onClick(v: View?) {
            // Check permissions
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permissions
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_REQUEST_CODE
                )
                return
            }

            // Create camera intent
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoLauncher.launch(intent)
        }
    }

    private inner class ClickImageIconListener : OnClickListener {
        override fun onClick(v: View?) {
            // Check permissions
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request permissions
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    MEDIA_REQUEST_CODE
                )
                //return
            }

            // Create camera intent
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        }
    }

    private inner class CreateSpotListener : OnClickListener {
        override fun onClick(v: View?) {
            if (street.text.isNotBlank() && houseNumber.text.isNotBlank() && city.text.isNotBlank() && postalCode.text.isNotBlank()) {
                invalidInput.visibility = GONE

                val address = Address(
                    postalCode = postalCode.text.toString().toInt(),
                    city = city.text.toString(),
                    street = street.text.toString(),
                    houseNumber = houseNumber.text.toString()
                )
                val addressID: Int? = GlobalValues.getAddressLogic()?.createAddress(address)
                val sportPlace = SportPlace(
                    creator = GlobalValues.getUser()!!.userID,
                    address = addressID!!,
                    type = dropDown.selectedItemPosition,
                    image = photo
                )
                val sportPlaceID = GlobalValues.getSportPlaceLogic()!!.createSportPlace(sportPlace)
                if (photo != null) {
                    GlobalValues.getImageLogic()!!.createImage(photo!!, sportPlaceID)
                }
            } else {
                invalidInput.visibility = VISIBLE
            }
        }
    }

    var photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val picture = data?.extras?.get("data")
                photo = picture as Bitmap
                photoPreview.setImageBitmap(photo)
            }
        }

    var galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val uri = data?.data
                photo = (photoPreview.drawable as BitmapDrawable).bitmap
                photoPreview.setImageBitmap(photo)
            }
        }

    companion object {
        private const val CAMERA_REQUEST = 1888
        private const val CAMERA_REQUEST_CODE = 0
        private const val MEDIA_REQUEST_CODE = 1
    }
}