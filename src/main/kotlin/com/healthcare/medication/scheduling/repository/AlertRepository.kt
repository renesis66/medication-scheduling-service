package com.healthcare.medication.scheduling.repository

import com.healthcare.medication.scheduling.model.Alert
import com.healthcare.medication.scheduling.model.AlertStatus

interface AlertRepository {
    suspend fun findById(alertId: String): Alert?
    suspend fun findByStatus(status: AlertStatus): List<Alert>
    suspend fun save(alert: Alert): Alert
}