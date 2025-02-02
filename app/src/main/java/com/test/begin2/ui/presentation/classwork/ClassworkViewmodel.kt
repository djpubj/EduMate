package com.test.begin2.ui.presentation.classwork

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.test.begin2.dataclass.Assignment
import com.test.begin2.dataclass.Assignmentsubmited
import com.test.begin2.repositories.ClassworkRepository

class ClassworkViewmodel(
    private val repository: ClassworkRepository = ClassworkRepository(),
) : ViewModel() {

    private var _AllAssignment = mutableStateOf<List<Assignment>>(emptyList())
    val AllAssignment: State<List<Assignment>> = _AllAssignment

    fun createassignment(classId: String, assignment: Assignment) {
        repository.createAssignment(classId = classId, assignment = assignment)
    }

    fun getAssignment(classId: String) {
        repository.getAssignment(classId = classId,
            onSuccess = { assignments ->
                _AllAssignment.value = assignments
            })
    }

    fun submitassignment(
        classId: String,
        assignmentId: String,
        assignmentsubmited: Assignmentsubmited
    ) {
        repository.submitassignment(
            classId = classId,
            assignmentId = assignmentId,
            assignmentsubmited = assignmentsubmited
        )
    }



    fun removeAssignment(classId: String, assignmentId: String) {
        repository.removeAssignment(classId = classId, assignmentId = assignmentId)
    }
}