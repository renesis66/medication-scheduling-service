package com.medscheduler.service

import com.medscheduler.model.Alert
import com.medscheduler.model.Schedule
import com.medscheduler.repository.ScheduleRepository
import io.micronaut.runtime.ApplicationConfiguration
import jakarta.inject.Singleton
import kotlinx.datetime.LocalDate
import java.util.*

@Singleton
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
    private val alertService: AlertService
) {

    suspend fun getScheduleForDate(patientId: UUID, date: LocalDate): Schedule? {
        return scheduleRepository.findByPatientIdAndDate(patientId, date)
    }

    suspend fun getActiveAlerts(): List<Alert> {
        return alertService.getActiveAlerts()
    }

    suspend fun acknowledgeAlert(alertId: String, acknowledgedBy: String): Alert {
        return alertService.acknowledgeAlert(alertId, acknowledgedBy)
    }
}