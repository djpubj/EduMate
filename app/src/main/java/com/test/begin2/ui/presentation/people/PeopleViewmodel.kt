package com.test.begin2.ui.presentation.people

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.test.begin2.dataclass.User
import com.test.begin2.repositories.PeopleRepository

class PeopleViewmodel(
    private val repository: PeopleRepository = PeopleRepository()
) : ViewModel() {
    private val _allStudents = mutableStateOf<List<User>>(emptyList())
    val allStudents: State<List<User>> = _allStudents

    private val _allTeachers = mutableStateOf<List<User>>(emptyList())
    val allTeachers: State<List<User>> = _allTeachers


    fun getstudents(classId: String) {
        repository.getStudents(
            classId = classId,
            onSuccess = { stundents ->
                _allStudents.value = stundents as List<User>
            }
        )
    }

    fun getteachers(classId: String) {
        repository.getTeachers(
            classId = classId,
            onSuccess = { teachers ->
                _allTeachers.value = teachers as List<User>
            }
        )
    }

    fun removestudent(classId: String, studentId: String) {
        repository.removeStudent(classId = classId, studentId = studentId)
    }

}