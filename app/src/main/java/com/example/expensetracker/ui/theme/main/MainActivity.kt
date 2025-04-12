package com.example.expensetracker.ui.theme.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.webAuthId
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val TAG = "Sign in"
    private lateinit var credentialManager: CredentialManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        credentialManager = CredentialManager.create(this)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                val viewModel = hiltViewModel<LoginViewModel>()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginScreen(
                        uiState = viewModel.uiState,
                        padding = innerPadding,
                        onLogin = { _, _ -> },
                        onGoogleLogin = {
                            loginWithGoogle()
                        },
                        updateEmail = { viewModel.updateEmail(it) },
                        updatePassword = { viewModel.updatePassword(it) },
                        updateErrorMessage = { viewModel.updateErrorMessage(it) })
                }
            }
        }
    }


    private fun loginWithGoogle() {
        val signInWithGoogleOption: GetSignInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(webAuthId)
                .build()
//todo        .setNonce(<nonce string to use when generating a Google ID token>)

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()
        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = applicationContext,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                handleFailure(e)
            }
        }

    }

    fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential

        when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        Log.v(TAG, "Login success: ${googleIdTokenCredential.id}")
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    fun handleFailure(e: GetCredentialException) {
        Log.e(TAG, "Sign in failed", e)
    }

    @Composable
    fun LoginScreen(
        uiState: LoginUiState,
        padding: PaddingValues,
        onLogin: (String, String) -> Unit,
        onGoogleLogin: () -> Unit,
        updateEmail: (String) -> Unit,
        updatePassword: (String) -> Unit,
        updateErrorMessage: (String) -> Unit,
    ) {
        val emptyError = stringResource(id = R.string.error_empty_fields)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.login_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { updateEmail(it) },
                label = { Text(stringResource(id = R.string.email_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { updatePassword(it) },
                label = { Text(stringResource(id = R.string.password_label)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            uiState.errorMessage?.let {
                Text(text = it, color = Color.Red, fontSize = 14.sp)
            }

            Button(
                onClick = {
                    if (uiState.email.isNotEmpty() && uiState.password.isNotEmpty()) {
                        onLogin(uiState.email, uiState.password)
                    } else {
                        updateErrorMessage(emptyError)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.login_button))
            }

            Spacer(modifier = Modifier.height(8.dp))

            GoogleSignInButton(onClick = onGoogleLogin)
        }
    }

    @Composable
    fun GoogleSignInButton(onClick: () -> Unit) {
        Button(
            onClick = onClick,
            border = BorderStroke(1.dp, Color.Gray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.login_google))
        }
    }


    @Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_TYPE_NORMAL)
    @Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun LoginScreenPreview() {
        val previewState = object : LoginUiState {
            override val email = "test@example.com"
            override val password = "123456"
            override val errorMessage = "Email or password is wrong"
        }
        ExpenseTrackerTheme {
            LoginScreen(
                uiState = previewState,
                padding = PaddingValues(16.dp),
                onLogin = { email, pass -> },
                onGoogleLogin = {},
                updateEmail = {},
                updatePassword = {},
                updateErrorMessage = {}
            )
        }
    }

    @Preview(
        name = "Dynamic Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_TYPE_NORMAL
    )
    @Preview(
        name = "Dynamic Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES
    )
    @Composable
    fun LoginScreenDynamicColorPreview() {
        val previewState = object : LoginUiState {
            override val email = "test@example.com"
            override val password = "123456"
            override val errorMessage = null
        }
        ExpenseTrackerTheme(dynamicColor = false) {
            LoginScreen(
                uiState = previewState,
                padding = PaddingValues(16.dp),
                onLogin = { email, pass -> },
                onGoogleLogin = {},
                updateEmail = {},
                updatePassword = {},
                updateErrorMessage = {}
            )
        }
    }
}