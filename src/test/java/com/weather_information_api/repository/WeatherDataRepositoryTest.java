package com.weather_information_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.weather_information_api.entity.Location;
import com.weather_information_api.entity.WeatherData;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WeatherDataRepositoryTest {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testFindByLocationIdAndForDate() {
        Location location = new Location();
        location.setPincode("123456");
        location.setLatitude(19.0760);
        location.setLongitude(72.8777);
        location = locationRepository.save(location);

        WeatherData weatherData = new WeatherData();
        weatherData.setLocation(location);
        weatherData.setForDate(LocalDate.of(2024, 12, 1));
        weatherData.setTemperatureCurrent(30.0);
        weatherData.setTemperatureFeelsLike(35.0);
        weatherData.setHumidity(80);
        weatherData.setWeatherInfo("{\"temp\":30}");
        weatherDataRepository.save(weatherData);

        Optional<WeatherData> foundData = weatherDataRepository.findByLocationIdAndForDate(location.getId(), LocalDate.of(2024, 12, 1));
        assertThat(foundData).isPresent();
        assertThat(foundData.get().getTemperatureCurrent()).isEqualTo(30.0);
    }
}

