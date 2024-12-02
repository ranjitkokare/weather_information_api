package com.weather_information_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.weather_information_api.entity.Location;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testSaveAndFindByPincode() {
        Location location = new Location();
        location.setPincode("123456");
        location.setLatitude(19.0760);
        location.setLongitude(72.8777);

        locationRepository.save(location);

        Optional<Location> foundLocation = locationRepository.findByPincode("123456");
        assertThat(foundLocation).isPresent();
        assertThat(foundLocation.get().getLatitude()).isEqualTo(19.0760);
        assertThat(foundLocation.get().getLongitude()).isEqualTo(72.8777);
    }
}

