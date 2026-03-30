package com.ayfernaz.airlineapi.controller;

import com.ayfernaz.airlineapi.dto.BuyTicketRequestDto;
import com.ayfernaz.airlineapi.dto.BuyTicketResponseDto;
import com.ayfernaz.airlineapi.dto.CheckInRequestDto;
import com.ayfernaz.airlineapi.dto.CheckInResponseDto;
import com.ayfernaz.airlineapi.dto.CreateFlightRequestDto;
import com.ayfernaz.airlineapi.dto.FileUploadResponseDto;
import com.ayfernaz.airlineapi.dto.FlightResponseDto;
import com.ayfernaz.airlineapi.dto.PassengerResponseDto;
import com.ayfernaz.airlineapi.dto.RoundTripFlightResponseDto;
import com.ayfernaz.airlineapi.ratelimit.RateLimitService;
import com.ayfernaz.airlineapi.service.FlightService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private RateLimitService rateLimitService;

   @PostMapping
public FlightResponseDto addFlight(@Valid @RequestBody CreateFlightRequestDto request) {
    return flightService.addFlight(request);
}

    @PostMapping("/upload")
    public FileUploadResponseDto addFlightByFile(@RequestParam("file") MultipartFile file) {
        return flightService.addFlightByFile(file);
    }

    @GetMapping("/search")
    public Page<FlightResponseDto> searchFlights(
            @RequestParam String airportFrom,
            @RequestParam String airportTo,
            @RequestParam int numberOfPeople,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(defaultValue = "oneway") String tripType,
            @RequestParam(defaultValue = "0") int page,
            HttpServletRequest request
    ) {
        String clientIp = request.getRemoteAddr();

        rateLimitService.checkLimit(clientIp);

        return flightService.searchFlights(
                airportFrom,
                airportTo,
                dateFrom,
                dateTo,
                numberOfPeople,
                tripType,
                PageRequest.of(page, 10)
        );
    }

    @GetMapping("/search/roundtrip")
    public RoundTripFlightResponseDto searchRoundTripFlights(
            @RequestParam String airportFrom,
            @RequestParam String airportTo,
            @RequestParam int numberOfPeople,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            HttpServletRequest request
    ) {
        String clientIp = request.getRemoteAddr();

        rateLimitService.checkLimit(clientIp);

        return flightService.searchRoundTripFlights(
                airportFrom,
                airportTo,
                dateFrom,
                dateTo,
                numberOfPeople
        );
    }

    @PostMapping("/tickets/buy")
public BuyTicketResponseDto buyTicket(@Valid @RequestBody BuyTicketRequestDto request) {
    return flightService.buyTicket(request);
}

    @PostMapping("/check-in")
public CheckInResponseDto checkIn(@Valid @RequestBody CheckInRequestDto request) {
    return flightService.checkIn(request);
}

    @GetMapping("/{flightNumber}/passengers")
public Page<PassengerResponseDto> getPassengerList(
        @PathVariable String flightNumber,
        @RequestParam(required = false) String date,
        @RequestParam(defaultValue = "0") int page
) {
    return flightService.getPassengerList(
            flightNumber,
            date,
            PageRequest.of(page, 10)
    );
  }
}