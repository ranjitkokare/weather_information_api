package com.weather_information_api.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather_information_api.entity.Location;
import com.weather_information_api.entity.WeatherData;
import com.weather_information_api.repository.LocationRepository;
import com.weather_information_api.repository.WeatherDataRepository;
import com.weather_information_api.util.ApiClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WeatherServiceImpl implements WeatherService {

    private final LocationRepository locationRepo;
    private final WeatherDataRepository weatherDataRepo;
    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;

    @Override
    public String getWeatherInfo(String pincode, LocalDate forDate) throws Exception {

        // Step 1: Get or save location
        Location location = locationRepo.findByPincode(pincode)
                .orElseGet(() -> fetchAndSaveLocation(pincode));

        // Step 2: Check cached weather data
        Optional<WeatherData> cachedWeather = weatherDataRepo
                .findByLocationIdAndForDate(location.getId(), forDate);

        if (cachedWeather.isPresent()) {
            return cachedWeather.get().getWeatherInfo(); // Return cached weather info
        }

        // Step 3: Fetch weather from API and save
        String weatherJson = apiClient.fetchWeather(location.getLatitude(), location.getLongitude());
        
        // Parse JSON to extract key fields
        JsonNode weatherNode = objectMapper.readTree(weatherJson);
        
        Double temperatureCurrent = weatherNode.path("main").path("temp").asDouble();
        Double temperatureFeelsLike = weatherNode.path("main").path("feels_like").asDouble();
        Integer humidity = weatherNode.path("main").path("humidity").asInt();
        
        saveWeatherData(location, forDate, temperatureCurrent, temperatureFeelsLike, humidity, weatherJson);

        return weatherJson;
    }

    private Location fetchAndSaveLocation(String pincode) {
        double[] latLong = apiClient.fetchLatLong(pincode);
        Location location = new Location();
        location.setPincode(pincode);
        location.setLatitude(latLong[0]);
        location.setLongitude(latLong[1]);
        return locationRepo.save(location);
    }

    private void saveWeatherData(Location location, LocalDate forDate, Double temperatureCurrent, Double temperatureFeelsLike, Integer humidity, String weatherInfo) {
        WeatherData weatherData = new WeatherData();
        weatherData.setLocation(location);
        weatherData.setForDate(forDate);
        weatherData.setTemperatureCurrent(temperatureCurrent);
        weatherData.setTemperatureFeelsLike(temperatureFeelsLike);
        weatherData.setHumidity(humidity);
        weatherData.setWeatherInfo(weatherInfo);
        weatherDataRepo.save(weatherData);
    }
}
