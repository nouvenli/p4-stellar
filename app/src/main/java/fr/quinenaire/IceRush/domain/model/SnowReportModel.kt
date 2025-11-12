package fr.quinenaire.IceRush.domain.model

import java.util.Calendar

data class SnowReportModel(
    val isRaining: Boolean,
    val date: Calendar,
    val temperatureCelsius: Int,
    val weatherTitle: String,
    val weatherDescription: String

)