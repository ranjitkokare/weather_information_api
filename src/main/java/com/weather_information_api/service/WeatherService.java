package com.weather_information_api.service;

import java.time.LocalDate;

public interface WeatherService {
	String getWeatherInfo(String pincode, LocalDate forDate) throws Exception;
}
