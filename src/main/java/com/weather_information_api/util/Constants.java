package com.weather_information_api.util;

public class Constants {
    public static final String GEOCODING_API_URL = "https://api.openweathermap.org/geo/1.0/zip?zip=%s,IN&appid={YOUR_API_KEY}";
    public static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid={YOUR_API_KEY}";
}
