package com.weather_information_api.util;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Fetch latitude and longitude for a given pincode using the Geocoding API.
     *
     * @param pincode The pincode for which to fetch lat/long.
     * @return A double array containing latitude and longitude.
     * @throws Exception If the API call or response parsing fails.
     */
    public double[] fetchLatLong(String pincode) {
        try {
            // Make API call
            String url = String.format(Constants.GEOCODING_API_URL, pincode);
            
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            // Parse response
            if (response != null && response.get("lat") != null && response.get("lon") != null) {
                double latitude = ((Number) response.get("lat")).doubleValue();
                double longitude = ((Number) response.get("lon")).doubleValue();
                return new double[]{latitude, longitude};
            } else {
                throw new RuntimeException("Invalid response from Geocoding API");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching lat/long for pincode: " + pincode, e);
        }
    }

    /**
     * Fetch weather information for a given latitude and longitude using the Weather API.
     *
     * @param lat Latitude of the location.
     * @param lon Longitude of the location.
     * @return The full weather information JSON as a string.
     * @throws Exception If the API call fails.
     */
    public String fetchWeather(double lat, double lon) {
        try {
            // Make API call
            String url = String.format(Constants.WEATHER_API_URL, lat, lon);

            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching weather for coordinates: lat=" + lat + ", lon=" + lon, e);
        }
    }

}
