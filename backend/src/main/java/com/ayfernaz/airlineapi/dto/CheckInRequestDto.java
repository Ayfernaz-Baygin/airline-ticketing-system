package com.ayfernaz.airlineapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CheckInRequestDto {

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Passenger name is required")
    private String passengerName;

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
}