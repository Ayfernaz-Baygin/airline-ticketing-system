package com.ayfernaz.airlineapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateFlightRequestDto {

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Airport from is required")
    private String airportFrom;

    @NotBlank(message = "Airport to is required")
    private String airportTo;

    @NotNull(message = "Departure date time is required")
    private LocalDateTime departureDateTime;

    @NotNull(message = "Arrival date time is required")
    private LocalDateTime arrivalDateTime;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int durationMinutes;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirportFrom() {
        return airportFrom;
    }

    public void setAirportFrom(String airportFrom) {
        this.airportFrom = airportFrom;
    }

    public String getAirportTo() {
        return airportTo;
    }

    public void setAirportTo(String airportTo) {
        this.airportTo = airportTo;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}