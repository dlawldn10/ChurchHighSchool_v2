package com.project.churchschool.DataClass

data class QR_AttendanceData(
    val attndnceData: MutableMap<String, Any> = mutableMapOf(),
    val date: String? = null
)

