package fr.quinenaire.stellarforecast.domain.model

import java.util.Calendar

data class WeatherReportModel(
    val isGoodForStargazing: Boolean,
    val date: Calendar,
    val temperatureCelsius: Int,
    val weatherTitle: String,
    val weatherDescription: String

)