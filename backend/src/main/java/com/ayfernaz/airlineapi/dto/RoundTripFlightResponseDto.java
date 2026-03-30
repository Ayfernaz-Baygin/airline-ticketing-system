package com.ayfernaz.airlineapi.dto;

import java.util.List;

public class RoundTripFlightResponseDto {

    private List<FlightResponseDto> outboundFlights;
    private List<FlightResponseDto> returnFlights;

    public List<FlightResponseDto> getOutboundFlights() {
        return outboundFlights;
    }

    public void setOutboundFlights(List<FlightResponseDto> outboundFlights) {
        this.outboundFlights = outboundFlights;
    }

    public List<FlightResponseDto> getReturnFlights() {
        return returnFlights;
    }

    public void setReturnFlights(List<FlightResponseDto> returnFlights) {
        this.returnFlights = returnFlights;
    }
}