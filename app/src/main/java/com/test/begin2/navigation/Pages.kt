package com.test.begin2.navigation

import com.test.begin2.dataclass.Role
import kotlinx.serialization.Serializable


@Serializable
object home

@Serializable
data class bottomnavigation(
    val classId: String?,
    val classname: String?,
    val role: Role
)

@Serializable
data class assignment(
    val classId: String?
)

@Serializable
data class announcement(
    val classId: String?
)

@Serializable
object join

@Serializable
object create

@Serializable
data class studentadd(
    val classId: String?
)

@Serializable
object teacheradd

@Serializable
data class assignmentsubmissionui(
    val classId: String?,
    val assignmentId: String,
    val title: String,
    val decription: String,
    val duedate: String
)

@Serializable
data class assignmentmanagementui(
    val classId: String?,
    val assignmentId: String?
)

@Serializable
object login

@Serializable
object signup

@Serializable
data class showdescription(
    val studentname: String,
    val description: String
)


