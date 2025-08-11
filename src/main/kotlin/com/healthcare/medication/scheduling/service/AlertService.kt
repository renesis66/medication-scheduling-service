package com.healthcare.medication.scheduling.service

import com.healthcare.medication.scheduling.model.Alert
import com.healthcare.medication.scheduling.model.AlertStatus
import com.healthcare.medication.scheduling.repository.AlertRepository
import jakarta.inject.Singleton
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Singleton
class AlertService(private val alertRepository: AlertRepository) {

    suspend fun getActiveAlerts(): List<Alert> {
        return alertRepository.findByStatus(AlertStatus.ACTIVE)
    }

    suspend fun acknowledgeAlert(alertId: String, acknowledgedBy: String): Alert {
        val alert = alertRepository.findById(alertId)
            ?: throw IllegalArgumentException("Alert not found: $alertId")
        
        val acknowledgedAlert = alert.copy(
            status = AlertStatus.ACKNOWLEDGED,
            acknowledgedBy = acknowledgedBy
        )
        
        return alertRepository.save(acknowledgedAlert)
    }
}