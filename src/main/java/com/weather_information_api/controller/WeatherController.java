package com.weather_information_api.controller;


import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather_information_api.service.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping
    public ResponseEntity<String> getWeather(@RequestParam String pincode,
            @RequestParam LocalDate forDate) {
    	try {
			String weatherInfo = weatherService.getWeatherInfo(pincode, forDate);
			return ResponseEntity.ok(weatherInfo);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(e.getMessage());
		}
    }
}
