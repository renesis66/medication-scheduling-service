package com.medscheduler.repository

import com.medscheduler.model.Schedule
import kotlinx.datetime.LocalDate
import java.util.*

interface ScheduleRepository {
    suspend fun findByPatientIdAndDate(patientId: UUID, date: LocalDate): Schedule?
    suspend fun save(schedule: Schedule): Schedule
    suspend fun findByDate(date: LocalDate): List<Schedule>
}