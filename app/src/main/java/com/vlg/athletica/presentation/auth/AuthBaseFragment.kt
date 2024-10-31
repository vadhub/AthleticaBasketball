package com.vlg.athletica.presentation.auth

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.vlg.athletica.R
import com.vlg.athletica.data.remote.HandleResponse
import com.vlg.athletica.data.remote.RemoteInstance
import com.vlg.athletica.data.remote.UnauthorizedException
import com.vlg.athletica.data.remote.UserAlreadyExistException
import com.vlg.athletica.data.remote.UserNotFoundException
import com.vlg.athletica.data.repository.UserRepository
import com.vlg.athletica.model.User
import com.vlg.athletica.presentation.AuthViewModel
import com.vlg.athletica.presentation.AuthViewModelFactory
import com.vlg.athletica.presentation.BaseFragment

open class AuthBaseFragment : BaseFragment(), HandleResponse<User>  {

    protected var qwrt = ""

    protected val authViewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(UserRepository(RemoteInstance), this)
    }

    override fun success(t: User) {

        configuration.saveIdUser(t.userId)
        configuration.saveLogin(t.email)
        configuration.saveNickname(t.nickname)
        configuration.savePass(qwrt)
        configuration.saveFirstStart(true)

        RemoteInstance.setUser(User(t.userId, t.nickname, t.firstname, t.lastname, t.email, qwrt))

        findNavController().navigate(R.id.mapFragment)

    }

    override fun error(e: Exception) {
        if (e is UserAlreadyExistException) {
            Toast.makeText(
                thisContext,
                getString(R.string.user_with_this_nik_already_exist), Toast.LENGTH_SHORT
            ).show()
        }
        if (e is UserNotFoundException) {
            Toast.makeText(
                thisContext,
                getString(R.string.invalid_password_or_username), Toast.LENGTH_SHORT
            ).show()
        }

        if (e is UnauthorizedException) {
            Toast.makeText(
                thisContext,
                getString(R.string.invalid_password_or_username), Toast.LENGTH_SHORT
            ).show()
        }
    }

}