package com.test.begin2.ui.components.classwork_component

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.test.begin2.dataclass.Assignmentsubmited
import com.test.begin2.repositories.ClassworkRepository

class AssignmentManagementViewmodel(
    private val repository: ClassworkRepository = ClassworkRepository(),
) : ViewModel() {
    private val _AllSubmissions = mutableStateOf<List<Assignmentsubmited>>(emptyList())
    val AllSubmissions: State<List<Assignmentsubmited>> = _AllSubmissions

    fun getAssignmentSubmissions(classId: String, assignmentId: String) {
        repository.getAssignmentSubmissions(
            classId = classId,
            assignmentId = assignmentId,
            onSuccess = { allsubmission ->
                _AllSubmissions.value = allsubmission
            }
        )
    }
}