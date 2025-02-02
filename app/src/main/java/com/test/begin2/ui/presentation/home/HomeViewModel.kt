package com.test.begin2.ui.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.test.begin2.dataclass.ClassRoom
import com.test.begin2.repositories.AuthRepository
import com.test.begin2.repositories.HomeRepository
import com.test.begin2.ui.presentation.login_signin.AuthViewmodel

class HomeViewModel(
    private val repository: HomeRepository = HomeRepository(),
    val authViewmodel: AuthViewmodel = AuthViewmodel(),
    private val authrepository: AuthRepository = AuthRepository(),
) : ViewModel() {

    private val _createdclassrooms = mutableStateOf<List<ClassRoom>>(emptyList())
    val createdclassrooms: State<List<ClassRoom>> = _createdclassrooms

    private val _enrolledclassrooms = mutableStateOf<List<ClassRoom>>(emptyList())
    val enrolledclassrooms: State<List<ClassRoom>> = _enrolledclassrooms

    val user = authViewmodel.fetchuser

    private val _errorMessage = mutableStateOf<String?>(null)
    var errorMessage: State<String?> = _errorMessage

    private val _addClassroomStatus = mutableStateOf<String?>(null)
    val addClassroomStatus: State<String?> = _addClassroomStatus

    private val _deleteClassroomStatus = mutableStateOf<String?>(null)
    val deleteClassroomStatus: State<String?> = _deleteClassroomStatus

    fun addClassroom(classRoom: ClassRoom) {
        repository.addClassroom(classRoom = classRoom, useruid = user.value?.useruid)
        _addClassroomStatus.value = "Classroom added successfully"
        fetchClassrooms()
    }

    fun addStudentinClassroom(classId: String){
        repository.addStudentsinClassroom(classId = classId, studentId = user.value?.useruid)
    }

    fun fetchClassrooms() {
        user.value?.let {
            repository.getCreatedClassrooms(
                useruid = it.useruid,
                onSuccess = { classrooms ->
                    _createdclassrooms.value = classrooms
                }
            )
            repository.getEnrolledClassrooms(
                useruid = it.useruid,
                onSuccess = { classrooms ->
                    _enrolledclassrooms.value = classrooms
                })
        }
    }

    fun unenrollStudent(classId: String){
        repository.unEnrollClassroom(classId = classId, useruid = user.value?.useruid)
    }
    fun deleteClassroom(classId: String) {
        repository.deleteClassroom(classId) { isSuccess ->
            if (isSuccess) {
                _deleteClassroomStatus.value = "Classroom deleted successfully"
                // Refresh the list of classrooms after deletion
                fetchClassrooms()
            } else {
                _deleteClassroomStatus.value = "Failed to delete classroom"
            }
        }
    }

}

