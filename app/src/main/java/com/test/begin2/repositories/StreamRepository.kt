package com.test.begin2.repositories

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.test.begin2.dataclass.Announcement
import com.test.begin2.dataclass.ClassRoom


class StreamRepository {
    private val database = Firebase.database
    private val classroomsRef = database.getReference("classrooms")

    fun addAnnouncement(classId: String, announcement: Announcement) {
        val classroomRef = classroomsRef.child(classId)

        classroomRef.child("announcements").get()
            .addOnSuccessListener { snapshot ->
                val currentAnnouncement =
                    snapshot.getValue(object : GenericTypeIndicator<List<Announcement>>() {})
                        ?: emptyList()
                val updatedAnnouncements = (currentAnnouncement + announcement).distinct()

                classroomRef.child("announcements").setValue(updatedAnnouncements)
                    .addOnSuccessListener {
                        Log.e("Announcement", "announcement added successfully")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Announcement", "announcement failed due to ${exception.message}")
                    }
            }
            .addOnFailureListener { exception ->
                println("Error fetching existing classes, creating a new list: ${exception.message}")
                classroomRef.child("createdClasses").setValue(listOf(announcement))
                    .addOnSuccessListener {
                        Log.e("Announcement", "announcement added successfully in new list")
                    }
                    .addOnFailureListener { ex ->
                        Log.e("Announcement", "Failed to create new list: ${ex.message}")
                    }
            }
    }

    fun getAnnouncements(
        classId: String,
        onSuccess: (ClassRoom) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val query = classroomsRef.orderByChild("classId").equalTo(classId)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val classroom = snapshot.children.firstOrNull()?.getValue(ClassRoom::class.java)
                    if (classroom != null) {
                        val Announcements = classroom.announcements.toMutableList()
                        val classtitle = classroom.className
                        val batch = classroom.batch
                        onSuccess(classroom)
                    }
                } else {
                    onFailure(Exception())
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    fun deleteAnnouncement(classId: String, announcementId: String) {
        val classroomRef = classroomsRef
            .child(classId)
        classroomRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val classroom = dataSnapshot.getValue(ClassRoom::class.java)
                val AllAnnouncements = (classroom?.announcements as? MutableList) ?: mutableListOf()

                for (i in AllAnnouncements) {
                    if (i.announcementId == announcementId) {
                        AllAnnouncements.remove(i)
                        classroomRef.child("announcements").setValue( AllAnnouncements)
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