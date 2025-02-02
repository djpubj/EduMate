package com.test.begin2.dataclass

import java.util.UUID

data class User(
    val useruid: String = "",
    var username: String = "",
    val email: String = "",
//    val role: Role,
)


// ClassRoom.kt
data class ClassRoom(
    val classId: String = UUID.randomUUID().toString(),
    val className: String = "",
    val batch: String = "",
    val teacherId: String? = "",
    val teachername: String = "",
    val noofstudents: String = "",
    val teachers: List<String?> = emptyList(), // Reference to teacher
    val students: List<String?> = emptyList(), // List of students
    val assignments: List<Assignment> = emptyList(),
    val announcements: List<Announcement> = emptyList(),
)

// Assignment.kt

data class Assignment(
    val assignmentId: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val dueDate: String = "",
    val AllSubmittedAssignment: List<Assignmentsubmited> = emptyList()
)

data class Assignmentsubmited(
    val submissionId: String = UUID.randomUUID().toString(),
    val username: String = "",
    val useruid: String = "",
    val submitedAssignment: String = "",
)


// Announcement.kt
data class Announcement(
    val announcementId: String = UUID.randomUUID().toString(),
    val announcementTitle: String = "",
    val message: String = "",
    val date: String = ""
)




