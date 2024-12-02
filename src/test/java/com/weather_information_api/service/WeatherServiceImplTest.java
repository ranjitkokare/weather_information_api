package com.weather_information_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.weather_information_api.entity.Location;
import com.weather_information_api.entity.WeatherData;
import com.weather_information_api.repository.LocationRepository;
import com.weather_information_api.repository.WeatherDataRepository;
import com.weather_information_api.util.ApiClient;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {

    @Mock
    private LocationRepository locationRepo;

    @Mock
    private WeatherDataRepository weatherDataRepo;

    @Mock
    private ApiClient apiClient;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    public void testGetWeatherInfo_CachedData() throws Exception {
        Location location = new Location();
        location.setId(1L);
        location.setPincode("123456");
        location.setLatitude(19.0760);
        location.setLongitude(72.8777);

        WeatherData cachedData = new WeatherData();
        cachedData.setLocation(location);
        cachedData.setForDate(LocalDate.of(2024, 12, 1));
        cachedData.setWeatherInfo("{\"temp\":30}");

        when(locationRepo.findByPincode("123456")).thenReturn(Optional.of(location));
        when(weatherDataRepo.findByLocationIdAndForDate(1L, LocalDate.of(2024, 12, 1))).thenReturn(Optional.of(cachedData));

        String weatherInfo = weatherService.getWeatherInfo("123456", LocalDate.of(2024, 12, 1));

        assertThat(weatherInfo).isEqualTo("{\"temp\":30}");
        verify(apiClient, never()).fetchWeather(anyDouble(), anyDouble());
    }

    @Test
    public void testGetWeatherInfo_ApiCall() throws Exception {
        Location location = new Location();
        location.setId(1L);
        location.setPincode("123456");
        location.setLatitude(19.0760);
        location.setLongitude(72.8777);

        when(locationRepo.findByPincode("123456")).thenReturn(Optional.of(location));
        when(weatherDataRepo.findByLocationIdAndForDate(1L, LocalDate.of(2024, 12, 1))).thenReturn(Optional.empty());
        when(apiClient.fetchWeather(19.0760, 72.8777)).thenReturn("{\"main\":{\"temp\":30,\"feels_like\":35,\"humidity\":80}}");

        String weatherInfo = weatherService.getWeatherInfo("123456", LocalDate.of(2024, 12, 1));

        assertThat(weatherInfo).contains("\"temp\":30");
        verify(weatherDataRepo, times(1)).save(any(WeatherData.class));
    }
}

