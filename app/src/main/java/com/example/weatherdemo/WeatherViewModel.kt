package com.example.weatherdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())

    val uiState: StateFlow<WeatherUiState> =
        _uiState.asStateFlow()

    init {
        getWeather()
    }

    fun getWeather() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {

                val result = RetrofitInstance.api.getWeather(
                    latitude = 17.5747,
                    longitude = 120.3869,
                    current = "temperature_2m,wind_speed_10m"
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weather = result
                    )
                }

            } catch (e: Exception) {

                e.printStackTrace()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unable to get weather."
                    )
                }
            }
        }
    }
}