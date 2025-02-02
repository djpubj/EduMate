package com.test.begin2.repositories

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.test.begin2.dataclass.User

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database

    fun addUserToDatabase(user: User, onComplete: (Boolean, String?) -> Unit) {
        val usersRef = database.getReference("Users")
        usersRef.child(user.useruid).setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun updateUserinDatabase(user: User) {
        val usersRef = database.getReference("Users")
        usersRef.child(user.useruid).setValue(user)
    }

    fun fetchUserByUid(
        useruid: String?,
        onSuccess: (User?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val usersRef = database.getReference("Users")

        usersRef.orderByChild("useruid").equalTo(useruid).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.children.firstOrNull()?.getValue(User::class.java)
                    onSuccess(user)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }

    }

    fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }

    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun signup(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }

    fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }
}