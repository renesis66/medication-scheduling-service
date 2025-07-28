package com.healthcare.medication.scheduling.controller

import com.healthcare.medication.scheduling.model.Alert
import com.healthcare.medication.scheduling.model.Schedule
import com.healthcare.medication.scheduling.service.ScheduleService
import io.micronaut.http.annotation.*
import kotlinx.datetime.LocalDate
import java.util.*

@Controller("/schedules")
class ScheduleController(private val scheduleService: ScheduleService) {

    @Get("/patients/{patientId}/{date}")
    suspend fun getPatientSchedule(
        @PathVariable patientId: UUID,
        @PathVariable date: LocalDate
    ): Schedule? {
        return scheduleService.getScheduleForDate(patientId, date)
    }

    @Get("/alerts")
    suspend fun getActiveAlerts(): List<Alert> {
        return scheduleService.getActiveAlerts()
    }

    @Post("/alerts/acknowledge")
    suspend fun acknowledgeAlert(
        @Body request: AcknowledgeAlertRequest
    ): Alert {
        return scheduleService.acknowledgeAlert(request.alertId, request.acknowledgedBy)
    }
}

data class AcknowledgeAlertRequest(
    val alertId: String,
    val acknowledgedBy: String
)