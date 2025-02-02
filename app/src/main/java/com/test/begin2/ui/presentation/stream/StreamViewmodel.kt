package com.test.begin2.ui.presentation.stream

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.test.begin2.dataclass.Announcement
import com.test.begin2.dataclass.ClassRoom
import com.test.begin2.repositories.StreamRepository

class StreamViewmodel(
    private val repository: StreamRepository = StreamRepository(),
) : ViewModel() {

    private val _classroom = mutableStateOf<ClassRoom>(ClassRoom())
    val classroom: State<ClassRoom> = _classroom

    private val _announcements = mutableStateOf<List<Announcement>>(emptyList())
    val announcements: State<List<Announcement>> = _announcements


    fun addAnnouncement(classId: String, announcement: Announcement) {
        repository.addAnnouncement(classId = classId, announcement = announcement)
    }

    fun getAnnouncements(classId: String) {
        repository.getAnnouncements(
            classId = classId,
            onSuccess = { classroom ->
                _announcements.value = classroom.announcements.toMutableList()
                _classroom.value= classroom
            },
            onFailure = { exception ->
            })

    }

    fun deleteAnnouncement(classId: String, announcementId: String) {
        repository.deleteAnnouncement(classId, announcementId)
    }
}