package com.test.begin2.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.test.begin2.dataclass.Assignment
import com.test.begin2.dataclass.Assignmentsubmited
import com.test.begin2.dataclass.ClassRoom

class ClassworkRepository {
    private val database = Firebase.database
    private val classroomsRef = database.getReference("classrooms")

    fun createAssignment(assignment: Assignment, classId: String) {
        val classroomRef = classroomsRef.child(classId)

        classroomRef.child("assignments").get()
            .addOnSuccessListener { snapshot ->
                val currentAssignments =
                    snapshot.getValue(object : GenericTypeIndicator<List<Assignment>>() {})
                        ?: emptyList()
                val updatedAssignments = (currentAssignments + assignment).distinct()

                classroomRef.child("assignments").setValue(updatedAssignments)
                    .addOnSuccessListener {
                        Log.e("assignments", "assignments added successfully")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("assignments", "assignments failed due to ${exception.message}")
                    }
            }
            .addOnFailureListener { exception ->
                {}
            }
    }

    fun getAssignment(classId: String, onSuccess: (List<Assignment>) -> Unit) {
        classroomsRef.child(classId).child("assignments")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val Allassignments =
                            snapshot.children.mapNotNull { it.getValue(Assignment::class.java) }
                        onSuccess(Allassignments)
                    } catch (e: Exception) {
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun submitassignment(
        classId: String,
        assignmentId: String,
        assignmentsubmited: Assignmentsubmited
    ) {
        val classroomRef = classroomsRef.child(classId)

        classroomRef.child("assignments").get()
            .addOnSuccessListener { snapshot ->
                for (i in snapshot.children) {
                    val assignmentkey = i.child("assignmentId").getValue(String::class.java)
                    if (assignmentkey == assignmentId) {
                        val listref = i.ref.child("AllSubmittedAssignment")
                        listref.get().addOnSuccessListener { listSnapshot ->
                            val existingList = listSnapshot.getValue(object :
                                GenericTypeIndicator<List<Assignmentsubmited>>() {}) ?: emptyList()
                            val updatedList = (existingList + assignmentsubmited).distinct()
                            listref.setValue(updatedList)
                        }

                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Failed to update list")
            }
    }

    fun getAssignmentSubmissions(
        classId: String,
        assignmentId: String,
        onSuccess: (List<Assignmentsubmited>) -> Unit
    ) {
        classroomsRef.child(classId).child("assignments")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val AllAssignment =
                            snapshot.children.mapNotNull { it.getValue(Assignment::class.java) }
                        for (i in AllAssignment){
                            if (i.assignmentId==assignmentId){
                                val AllSubmission=i.AllSubmittedAssignment
                                onSuccess(AllSubmission)
                                break
                            }
                        }

                    } catch (e: Exception) {
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun removeAssignment(classId: String, assignmentId: String) {
        val classrooms = classroomsRef
            .child(classId)
        classrooms.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val classroom = dataSnapshot.getValue(ClassRoom::class.java)
                val AllAssignment = (classroom?.assignments as? MutableList) ?: mutableListOf()

                for (i in AllAssignment) {
                    if (i.assignmentId == assignmentId) {
                        AllAssignment.remove(i)
                       classrooms.child("assignments").setValue(AllAssignment)
                        break
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })
    }
}