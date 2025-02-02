package com.test.begin2.repositories

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.test.begin2.dataclass.User

class PeopleRepository {
    private val database = Firebase.database
    private val classroomsRef = database.getReference("classrooms")
    private val usersRef = database.getReference("Users")

    fun getStudents(classId: String, onSuccess: (List<User?>) -> Unit) {
        var AllStudentData = mutableStateOf<List<User?>>(emptyList())
        usersRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(user: DataSnapshot) {
                    try {
                        val alluser =
                            user.children.mapNotNull { it.getValue(User::class.java) }
                        classroomsRef.child(classId).child("students").addValueEventListener(
                            object : ValueEventListener {
                                override fun onDataChange(studentsId: DataSnapshot) {
                                    try {
                                        val AllStudentsId =
                                            studentsId.getValue(object :
                                                GenericTypeIndicator<List<String>>() {})
                                                ?: emptyList()

                                        for (i in AllStudentsId) {
                                            AllStudentData.value =
                                                (AllStudentData.value + alluser.filter { user ->
                                                    user.useruid == i == true
                                                })
                                        }
                                        onSuccess(AllStudentData.value)

                                    } catch (e: Exception) {
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })

                    } catch (e: Exception) {
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }

        )
    }

    fun getTeachers(classId: String, onSuccess: (List<User?>) -> Unit) {
        var AllTeacherData = mutableStateOf<List<User?>>(emptyList())
        usersRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(user: DataSnapshot) {
                    try {
                        val alluser =
                            user.children.mapNotNull { it.getValue(User::class.java) }
                        classroomsRef.child(classId).child("teachers")
                            .addValueEventListener(
                                object : ValueEventListener {
                                    override fun onDataChange(teachersId: DataSnapshot) {
                                        try {
                                            val AllTeachersId =
                                                teachersId.getValue(object :
                                                    GenericTypeIndicator<List<String>>() {})
                                                    ?: emptyList()
                                            for (i in AllTeachersId) {
                                                AllTeacherData.value =
                                                    (AllTeacherData.value + alluser.filter { user ->
                                                        user.useruid == i == true
                                                    })
                                            }
                                            onSuccess(AllTeacherData.value)
                                        } catch (e: Exception) {
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                    } catch (e: Exception) {
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            }

        )
    }

    fun removeStudent(classId: String, studentId: String) {
        classroomsRef.child(classId).child("students").get()
            .addOnSuccessListener { studentsId ->
                val currentStudentsId =
                    studentsId.getValue(object :
                        GenericTypeIndicator<List<String>>() {})
                        ?: emptyList()
                val updatedStudentsId =
                    currentStudentsId.filterNot { eachstudentId ->
                        eachstudentId == studentId
                    }
                classroomsRef.child(classId).child("students")
                    .setValue(updatedStudentsId)
            }
            .addOnFailureListener {}
    }

}