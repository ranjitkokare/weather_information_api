# Weather Information API for Pincode
## Project Overview
This is a Spring Boot REST API to fetch and store weather information for a particular day and pincode. The system:
1. Retrieves latitude and longitude for a given pincode using the OpenWeather Geocoding API.
2. Fetches weather information based on latitude and longitude using the OpenWeather API.
3. Saves the pincode's latitude, longitude, and weather information in a MySQL database for optimized subsequent calls.

### Features
- **Input**: Pincode (e.g., `411014`) and date (e.g., `2020-10-15`).
- **Storage**:
  - Stores latitude and longitude of the pincode.
  - Caches weather information to reduce external API calls.
- **Optimization**: 
  - If weather data for a pincode and date is already present, it fetches from the database.
  - External API calls are minimized.
- **Testing**: The API has been tested using Postman.
- **Postman Collection Documentation**: [View Collection](https://documenter.getpostman.com/view/33540913/2sAYBYfAGk)

---

## Prerequisites

### 1. Tools Required
- **Java 17+**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Postman** (for testing)

### 2. External API Key
- Sign up on [OpenWeather](https://openweathermap.org/current) to get an API key.
- Add your API key in `Constants` class for `GEOCODING_API_URL` and `WEATHER_API_URL`.

---

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd weather-information-api
```

### 2. Configure Database
- Update `application.properties` with your MySQL credentials:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/weather_api_db
  spring.datasource.username=your_username
  spring.datasource.password=your_password
  ```

- Create the database manually:
  ```sql
  CREATE DATABASE weather_api_db;
  ```

### 3. Add API Keys
- Open `Constants.java` and update:
  ```java
  public static final String GEOCODING_API_URL = "https://api.openweathermap.org/geo/1.0/zip?zip=%s,IN&appid={YOUR_API_KEY}";
  public static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid={YOUR_API_KEY}";
  ```

---

## Build and Run

### 1. Build the Project
```bash
mvn clean install
```

### 2. Run the Project
```bash
mvn spring-boot:run
```

- The application will run on `http://localhost:8080/api/v1`.

---

## API Endpoints

### 1. **Fetch Weather Information**
   - **Endpoint**: `/weather`
   - **Method**: `POST`
   - **Description**: Fetches weather data for a given pincode and date.
   - **Request Params**:
     - `pincode` (String): Pincode for the location (e.g., `411014`).
     - `forDate` (LocalDate): Date for which weather is requested (e.g., `2020-10-15`).
   - **Sample Request**:
     ```bash
     curl -X POST "http://localhost:8080/api/v1/weather" \
     -d "pincode=411014" \
     -d "forDate=2020-10-15"
     ```

---

## Database Schema

### 1. **Location Table**
| Field     | Type         | Description       |
|-----------|--------------|-------------------|
| `id`      | BIGINT (PK)  | Auto-generated ID |
| `pincode` | VARCHAR(255) | Pincode           |
| `latitude`| DOUBLE       | Latitude          |
| `longitude`| DOUBLE      | Longitude         |

### 2. **WeatherData Table**
| Field               | Type         | Description                    |
|---------------------|--------------|--------------------------------|
| `id`                | BIGINT (PK)  | Auto-generated ID             |
| `location_id`       | BIGINT (FK)  | References Location table      |
| `forDate`           | DATE         | Weather date                  |
| `temperatureCurrent`| DOUBLE       | Current temperature (Celsius) |
| `temperatureFeelsLike`| DOUBLE     | Feels-like temperature (Celsius)|
| `humidity`          | INT          | Humidity (%)                  |
| `weatherInfo`       | JSON         | Raw weather info JSON          |

---

## Testing with Postman
1. Open [Postman Collection](https://documenter.getpostman.com/view/33540913/2sAYBYfAGk).
2. Update variables (`baseURL`, API key) if necessary.
3. Test the `/weather` endpoint with required parameters.

---

## Key Highlights

- **Optimized Caching**: Weather data is stored in the database to avoid repeated API calls.
- **Error Handling**: Comprehensive error handling for API failures and database issues.
- **Modular Design**: Service and repository layers for maintainability.

---

## Testing and TDD Approach

Our project incorporates **JUnit** for comprehensive testing and follows a **Test-Driven Development (TDD)** methodology to ensure code reliability, maintainability, and alignment with project requirements.

### Testing Frameworks and Tools Used
- **JUnit**: For writing and running test cases.
- **Mockito**: For mocking dependencies and simulating behaviors in unit tests.
- **AssertJ**: For fluent and expressive assertions.
- **H2 Database**: For in-memory integration testing with a clean, isolated database state.

### Key Test Categories
1. **Unit Tests**:
   - Focused on individual components like services and repositories.
   - Mocked external dependencies using `Mockito` to test isolated logic.
   - Example: `WeatherServiceImplTest` verifies weather data retrieval with and without cached data.

2. **Integration Tests**:
   - Validated end-to-end functionality of components interacting with each other.
   - Used the **H2 in-memory database** to simulate real database behavior.
   - Example: `LocationRepositoryTest` and `WeatherDataRepositoryTest` test CRUD operations and custom queries.

3. **Test Coverage**:
   - Comprehensive coverage of all major components.
   - Assured validation logic, database interactions, and service flows are functioning as expected.

A Maven project structure with the appropriate placement of test files:
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           ├── controller
│   │           ├── service
│   │           ├── repository
│   │           ├── model
│   │           └── config
│   └── resources
│       └── application.properties
│       
└── test
    └── java
        └── com
            └── example
                ├── service
                │   └── WeatherServiceImplTest.java
                └── repository
                    ├── LocationRepositoryTest.java
                    └── WeatherDataRepositoryTest.java

### Configuration for Testing
- The `application.properties` file ensures isolated testing with in-memory or dedicated test databases:
  ```properties
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.jpa.hibernate.ddl-auto=create-drop
  spring.datasource.driver-class-name=org.h2.Driver
  spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
  ```

### Running Tests
To run all test cases:
```bash
mvn clean test
```
---
