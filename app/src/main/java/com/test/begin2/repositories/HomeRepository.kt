package com.test.begin2.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.test.begin2.dataclass.ClassRoom

class HomeRepository{

    private val database = Firebase.database
    private val classroomsRef = database.getReference("classrooms")
    private val usersRef = database.getReference("Users")

    fun addClassroom(classRoom: ClassRoom, useruid: String?) {
        val classroom = ClassRoom(
            classId = classRoom.classId,
            className = classRoom.className,
            teachername = classRoom.teachername,
            teachers = listOf(useruid),
            batch = classRoom.batch,
            teacherId = useruid
        )
        classroomsRef.child(classroom.classId).setValue(classroom)
    }//create classroom

    fun addStudentsinClassroom(
        classId: String, studentId: String?
    ) {
        classroomsRef.child(classId).child("students").get()
            .addOnSuccessListener { studentsId ->
                val currentStudentsId =
                    studentsId.getValue(object : GenericTypeIndicator<List<String>>() {})
                        ?: emptyList()
                val updatedStudentsId = (currentStudentsId + studentId).distinct()
                classroomsRef.child(classId).child("students").setValue(updatedStudentsId)
            }
            .addOnFailureListener {}
    } //add students in classroom

    fun getEnrolledClassrooms(
        onSuccess: (List<ClassRoom>) -> Unit, useruid: String
    ) {
        classroomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val classrooms =
                        snapshot.children.mapNotNull { it.getValue(ClassRoom::class.java) }
                    val filteredClassrooms = classrooms.filter { classRoom ->
                        classRoom.students.contains(useruid) == true
                    }
                    onSuccess(filteredClassrooms)
                } catch (e: Exception) {
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getCreatedClassrooms(onSuccess: (List<ClassRoom>) -> Unit, useruid: String) {
        classroomsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val classrooms =
                        snapshot.children.mapNotNull { it.getValue(ClassRoom::class.java) }
                    val filteredClassrooms = classrooms.filter { classRoom ->
                        classRoom.teachers.contains(useruid) == true
                    }
                    Log.e("createdclass", "filtered ${filteredClassrooms}")
                    onSuccess(filteredClassrooms)
                } catch (e: Exception) {
                }
            }


            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun unEnrollClassroom(classId: String, useruid: String?) {

        classroomsRef.child(classId).child("students").get()
            .addOnSuccessListener { studentsId ->
                val currentStudentsId =
                    studentsId.getValue(object : GenericTypeIndicator<List<String>>() {})
                        ?: emptyList()
                val updatedStudentsId = currentStudentsId.filterNot { eachstudentId ->
                    eachstudentId == useruid
                }
                classroomsRef.child(classId).child("students").setValue(updatedStudentsId)
            }
            .addOnFailureListener {}

    }

    fun deleteClassroom(classId: String, onComplete: (Boolean) -> Unit) {
        if (classId.isNotBlank()) {
            classroomsRef.child(classId).removeValue()
                .addOnCompleteListener { task ->
                    onComplete(task.isSuccessful)
                }
        } else {
            onComplete(false)
        }
    }
}


