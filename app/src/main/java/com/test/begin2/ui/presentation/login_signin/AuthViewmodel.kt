package com.test.begin2.ui.presentation.login_signin

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.begin2.dataclass.AuthState
import com.test.begin2.dataclass.User
import com.test.begin2.repositories.AuthRepository

// ViewModel class
class AuthViewmodel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _fetchuser = MutableLiveData<User?>()
    val fetchuser: LiveData<User?> = _fetchuser

    private val _fetchusererror = MutableLiveData<String?>()
    val fetchusererror: LiveData<String?> = _fetchusererror

    private val _addUserStatus = mutableStateOf<String?>(null)
    val addUserStatus: State<String?> = _addUserStatus

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (repository.isAuthenticated()) {
            _authState.value = AuthState.Authenticated
            fetchUserByUid()
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun fetchUserByUid(useruid: String? = repository.getCurrentUserUid()) {
        repository.fetchUserByUid(
            useruid,
            onSuccess = { fetchedUser ->
                _fetchuser.value = fetchedUser
            },
            onFailure = { exception ->
                _fetchusererror.value = exception.message
            }
        )
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading

        repository.login(email, password) { success, message ->
            if (success) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(message ?: "Something went wrong")
            }
        }
    }

    fun signup(email: String, password: String, username: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading

        repository.signup(email, password) { success, message ->
            if (success) {
                _authState.value = AuthState.Authenticated
                val userUid = repository.getCurrentUserUid()
                addUser(User(useruid = userUid.toString(), username = username, email = email))
            } else {
                _authState.value = AuthState.Error(message ?: "Something went wrong")
            }
        }
    }

    fun signOut() {
        repository.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun addUser(user: User) {
        repository.addUserToDatabase(user) { success, message ->
            if (success) {
                _addUserStatus.value = "User added successfully"
            } else {
                _addUserStatus.value = message
            }
        }
    }

    fun updateUser(user: User){
        repository.updateUserinDatabase(user=user)
    }
}