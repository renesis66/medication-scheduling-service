package com.medscheduler.model

import io.micronaut.serde.annotation.Serdeable
import kotlinx.datetime.Instant
import java.util.*

@Serdeable
data class Alert(
    val alertId: String,
    val patientId: UUID,
    val prescriptionId: UUID,
    val alertType: AlertType,
    val priority: AlertPriority,
    val scheduledTime: Instant,
    val currentTime: Instant,
    val message: String,
    val status: AlertStatus,
    val acknowledgedBy: String? = null,
    val createdAt: Instant
)

enum class AlertType {
    OVERDUE,
    UPCOMING,
    MISSED
}

enum class AlertPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class AlertStatus {
    ACTIVE,
    ACKNOWLEDGED,
    RESOLVED
}