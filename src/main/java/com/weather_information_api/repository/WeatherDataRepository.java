package com.weather_information_api.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weather_information_api.entity.WeatherData;
@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long>{
	
	Optional<WeatherData> findByLocationIdAndForDate(Long locationId, LocalDate forDate);
}
