package com.healthcare.medication.scheduling.model

import io.micronaut.serde.annotation.Serdeable
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.util.*

@Serdeable
data class Schedule(
    val patientId: UUID,
    val scheduleDate: LocalDate,
    val medications: List<ScheduledMedication>,
    val generatedAt: Instant,
    val version: Int = 1
)

@Serdeable
data class ScheduledMedication(
    val prescriptionId: UUID,
    val medicationName: String,
    val scheduledTimes: List<LocalTime>,
    val dosage: Double,
    val unit: String
)