package com.medscheduler.repository

import com.medscheduler.model.Alert
import com.medscheduler.model.AlertStatus

interface AlertRepository {
    suspend fun findById(alertId: String): Alert?
    suspend fun findByStatus(status: AlertStatus): List<Alert>
    suspend fun save(alert: Alert): Alert
}