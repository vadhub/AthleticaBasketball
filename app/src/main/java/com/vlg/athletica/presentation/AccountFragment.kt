package com.vlg.athletica.presentation

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.vlg.athletica.R
import com.vlg.athletica.data.remote.RemoteInstance
import com.vlg.athletica.data.remote.Resource
import com.vlg.athletica.data.repository.FileRepository
import com.vlg.athletica.data.repository.UserRepository
import com.vlg.athletica.databinding.FragmentAccountBinding
import com.vlg.athletica.model.User
import java.io.File

class AccountFragment : BaseFragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private var userId = -1L
    private lateinit var userDetails: User
    private lateinit var username: TextView
    private val load: FileViewModel by activityViewModels {
        LoadViewModelFactory(FileRepository(RemoteInstance))
    }

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository(RemoteInstance))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDetails = RemoteInstance.user
        userId = userDetails.idUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.sighOut.setOnClickListener {
            configuration.clear()
            findNavController().navigate(R.id.loginFragment)
        }

        binding.changePhoto.setOnClickListener { startCrop() }
        binding.imageIcon.setOnClickListener { startCrop() }
        binding.changeNik.setOnClickListener { createSettingsDialog() }
        username = binding.usernameTextView
        username.text = userDetails.nickname

        load.uploadIcon.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {}

                is Resource.Success -> {
                    load.invalidate(userId, binding.imageIcon)
                }

                is Resource.Failure -> {
                    Toast.makeText(
                        thisContext,
                        getString(R.string.error_loading_icon),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun createSettingsDialog() {
        val changeNick: (new: String) -> Unit = {
            userViewModel.changeUsername(it, userId)
            configuration.saveLogin(it)
            RemoteInstance.setUser(userDetails.copy(nickname=it))
            username.text = it
        }
        val settingsDialog = SettingsAccount(changeNick)
        settingsDialog.show(parentFragmentManager, "SettingsDialog")
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriContent = result.getUriFilePath(thisContext, true)

            uriContent?.let {
                binding.imageIcon.setImageURI(Uri.parse(uriContent))
                load.uploadIcon(thisContext, File(uriContent), userId)
            }
        } else {
            Toast.makeText(thisContext, getString(R.string.can_t_load_image), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun startCrop() {
        cropImage.launch(
            CropImageContractOptions(
                uri = null,
                cropImageOptions = CropImageOptions(
                    imageSourceIncludeGallery = true,
                    imageSourceIncludeCamera = true
                ),
            ),
        )
    }

}