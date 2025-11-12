package fr.quinenaire.stellarforecast.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import fr.quinenaire.stellarforecast.domain.model.WeatherReportModel
import java.util.Calendar

/**
 * Main class represents OpenWeather API response.
 * It contains a list of forecasts.
 * @param forecasts list of forecasts
 * @constructor Create empty Open weather forecasts response
 *
 */

@JsonClass(generateAdapter = true)
data class OpenWeatherForecastsResponse(

    @Json(name = "list")
    val forecasts: List<ForecastResponse>,
) {
    /**
     * Forecast response represents a single forecast.
     * @param time time of the forecast
     * @param temperature temperature of the forecast
     * @param weather weather of the forecast
     * @constructor Create empty Forecast response
     */
    @JsonClass(generateAdapter = true)
    data class ForecastResponse(
        @Json(name = "dt")
        val time: Int,
        @Json(name = "main")
        val temperature: TemperatureResponse,
        @Json(name = "weather")
        val weather: List<WeatherResponse>,
    ) {
        /**
         * Temperature response represents a temperature in kelvin.
         * @param temp temperature
         * @constructor Create empty Temperature response
         */
        @JsonClass(generateAdapter = true)
        data class TemperatureResponse(
            @Json(name = "temp")
            val temp: Double,
        )

        /**
         * Weather response represents a details about the weather for a forecast.
         * @param id id of the weather
         * @param title title of the weather
         * @param description description of the weather
         * @constructor Create empty Weather response
         */
        @JsonClass(generateAdapter = true)
        data class WeatherResponse(
            @Json(name = "id")
            val id: Int,
            @Json(name = "main")
            val title: String,
            @Json(name = "description")
            val description: String
        )
    }

    /**
     * connection between WeatherReportModel, and OpenWeatherForecastsResponse
     */
    fun toDomainModel(): List<WeatherReportModel> {
        return forecasts.map { forecast ->
            val calendar = Calendar.getInstance().apply { timeInMillis = forecast.time * 1000L }

            // Check if the sky is clear (IDs 800 to 802 indicate clear sky conditions)
            val isClearSky = forecast.weather.isNotEmpty() && forecast.weather[0].id in 800..802

            // Get the hour of the date and determine if it's night
            val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            val isNight = hourOfDay < 6 || hourOfDay >= 18

            // Convert temperature to Celsius
            val temperatureCelsius = (forecast.temperature.temp - 273.15).toInt()

            WeatherReportModel(
                isGoodForStargazing = isClearSky && isNight,
                date = calendar,
                temperatureCelsius = temperatureCelsius,
                weatherTitle = forecast.weather[0].title,
                weatherDescription = forecast.weather[0].description
            )
        }
    }
}