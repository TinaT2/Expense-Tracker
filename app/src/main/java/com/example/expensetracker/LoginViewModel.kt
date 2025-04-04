package com.example.expensetracker

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Stable
interface LoginUiState {
    val email: String
    val password: String
    val errorMessage: String?
}

private class MutableLoginUiState : LoginUiState {
    override var email: String by mutableStateOf("")
    override var password: String by mutableStateOf("")
    override var errorMessage: String? by mutableStateOf(null)
}


@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableLoginUiState()
    val uiState: LoginUiState = _uiState

    fun updateEmail(newEmail: String) {
        _uiState.email = newEmail
    }

    fun updatePassword(newPassword: String) {
        _uiState.password = newPassword
    }
    fun updateErrorMessage(errorMessage: String) {
        _uiState.errorMessage = errorMessage
    }

    fun login() {
        if (_uiState.email.isNotEmpty() && _uiState.password.isNotEmpty()) {
            _uiState.errorMessage = null
            // Call authentication logic here...
        } else {
            _uiState.errorMessage = "Please enter both email and password!"
        }
    }
}
