package com.ayfernaz.airlineapi.service;

import com.ayfernaz.airlineapi.dto.BuyTicketRequestDto;
import com.ayfernaz.airlineapi.dto.BuyTicketResponseDto;
import com.ayfernaz.airlineapi.dto.CheckInRequestDto;
import com.ayfernaz.airlineapi.dto.CheckInResponseDto;
import com.ayfernaz.airlineapi.dto.CreateFlightRequestDto;
import com.ayfernaz.airlineapi.dto.FileUploadResponseDto;
import com.ayfernaz.airlineapi.dto.FlightResponseDto;
import com.ayfernaz.airlineapi.dto.PassengerResponseDto;
import com.ayfernaz.airlineapi.dto.RoundTripFlightResponseDto;
import com.ayfernaz.airlineapi.entity.Flight;
import com.ayfernaz.airlineapi.entity.Ticket;
import com.ayfernaz.airlineapi.repository.FlightRepository;
import com.ayfernaz.airlineapi.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public FlightResponseDto addFlight(CreateFlightRequestDto request) {
    Flight flight = new Flight();

    flight.setFlightNumber(request.getFlightNumber());
    flight.setAirportFrom(request.getAirportFrom());
    flight.setAirportTo(request.getAirportTo());
    flight.setDepartureDateTime(request.getDepartureDateTime());
    flight.setArrivalDateTime(request.getArrivalDateTime());
    flight.setDurationMinutes(request.getDurationMinutes());
    flight.setCapacity(request.getCapacity());
    flight.setAvailableSeats(request.getCapacity());

    Flight saved = flightRepository.save(flight);
    return mapToResponse(saved);
}

    public FileUploadResponseDto addFlightByFile(MultipartFile file) {
        FileUploadResponseDto response = new FileUploadResponseDto();

        int total = 0;
        int success = 0;
        int failed = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                total++;

                try {
                    String[] parts = line.split(",");

                    Flight flight = new Flight();
                    flight.setFlightNumber(parts[0].trim());
                    flight.setAirportFrom(parts[1].trim());
                    flight.setAirportTo(parts[2].trim());
                    flight.setDepartureDateTime(LocalDateTime.parse(parts[3].trim()));
                    flight.setArrivalDateTime(LocalDateTime.parse(parts[4].trim()));
                    flight.setCapacity(Integer.parseInt(parts[5].trim()));
                    flight.setAvailableSeats(Integer.parseInt(parts[5].trim()));

                    flightRepository.save(flight);
                    success++;
                } catch (Exception e) {
                    failed++;
                }
            }

            response.setMessage("File processed");
            response.setTotalRecords(total);
            response.setSuccessCount(success);
            response.setFailedCount(failed);

        } catch (Exception e) {
            response.setMessage("File processing failed");
            response.setTotalRecords(total);
            response.setSuccessCount(success);
            response.setFailedCount(failed);
        }

        return response;
    }

    public Page<FlightResponseDto> searchFlights(String from,
                                                 String to,
                                                 String dateFrom,
                                                 String dateTo,
                                                 int numberOfPeople,
                                                 String tripType,
                                                 Pageable pageable) {

        Page<Flight> flights = flightRepository.findAll(pageable);

        List<FlightResponseDto> filtered = flights.getContent().stream()
                .filter(f -> f.getAirportFrom().equalsIgnoreCase(from))
                .filter(f -> f.getAirportTo().equalsIgnoreCase(to))
                .filter(f -> f.getAvailableSeats() >= numberOfPeople)
                .filter(f -> {
                    if ((dateFrom == null || dateFrom.isBlank()) &&
                            (dateTo == null || dateTo.isBlank())) {
                        return true;
                    }

                    LocalDate flightDate = f.getDepartureDateTime().toLocalDate();

                    if (dateFrom != null && !dateFrom.isBlank() &&
                            dateTo != null && !dateTo.isBlank()) {
                        LocalDate fromDate = LocalDate.parse(dateFrom);
                        LocalDate toDate = LocalDate.parse(dateTo);
                        return !flightDate.isBefore(fromDate) && !flightDate.isAfter(toDate);
                    }

                    if (dateFrom != null && !dateFrom.isBlank()) {
                        return flightDate.equals(LocalDate.parse(dateFrom));
                    }

                    if (dateTo != null && !dateTo.isBlank()) {
                        return flightDate.equals(LocalDate.parse(dateTo));
                    }

                    return true;
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(filtered, pageable, filtered.size());
    }

    public RoundTripFlightResponseDto searchRoundTripFlights(String from,
                                                             String to,
                                                             String dateFrom,
                                                             String dateTo,
                                                             int numberOfPeople) {

        List<Flight> allFlights = flightRepository.findAll();

        List<FlightResponseDto> outboundFlights = allFlights.stream()
                .filter(f -> f.getAirportFrom().equalsIgnoreCase(from))
                .filter(f -> f.getAirportTo().equalsIgnoreCase(to))
                .filter(f -> f.getAvailableSeats() >= numberOfPeople)
                .filter(f -> {
                    if (dateFrom == null || dateFrom.isBlank()) {
                        return true;
                    }
                    return f.getDepartureDateTime().toLocalDate().equals(LocalDate.parse(dateFrom));
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        List<FlightResponseDto> returnFlights = allFlights.stream()
                .filter(f -> f.getAirportFrom().equalsIgnoreCase(to))
                .filter(f -> f.getAirportTo().equalsIgnoreCase(from))
                .filter(f -> f.getAvailableSeats() >= numberOfPeople)
                .filter(f -> {
                    if (dateTo == null || dateTo.isBlank()) {
                        return true;
                    }
                    return f.getDepartureDateTime().toLocalDate().equals(LocalDate.parse(dateTo));
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        RoundTripFlightResponseDto response = new RoundTripFlightResponseDto();
        response.setOutboundFlights(outboundFlights);
        response.setReturnFlights(returnFlights);

        return response;
    }

    public BuyTicketResponseDto buyTicket(BuyTicketRequestDto request) {
    Flight flight = flightRepository.findAll()
            .stream()
            .filter(f -> f.getFlightNumber().equals(request.getFlightNumber()))
            .filter(f -> request.getDate() == null ||
                    f.getDepartureDateTime().toLocalDate().equals(request.getDate()))
            .findFirst()
            .orElse(null);

    BuyTicketResponseDto res = new BuyTicketResponseDto();

    if (flight == null) {
        res.setTransactionStatus("Flight not found");
        return res;
    }

    if (request.getPassengerNames() == null || request.getPassengerNames().isEmpty()) {
        res.setTransactionStatus("Passenger list is empty");
        return res;
    }

    if (flight.getAvailableSeats() < request.getPassengerNames().size()) {
        res.setTransactionStatus("SOLD OUT");
        return res;
    }

    flight.setAvailableSeats(flight.getAvailableSeats() - request.getPassengerNames().size());
    flightRepository.save(flight);

    java.util.List<Long> ticketIds = new java.util.ArrayList<>();

    for (String passengerName : request.getPassengerNames()) {
        Ticket t = new Ticket();
        t.setFlight(flight);
        t.setPassengerName(passengerName);
        t.setCheckedIn(false);
        t.setSeatNumber(null);

        Ticket savedTicket = ticketRepository.save(t);
        ticketIds.add(savedTicket.getId());
    }

    res.setTransactionStatus("SUCCESS");
    res.setTicketNumbers(ticketIds);
    return res;
}

    public CheckInResponseDto checkIn(CheckInRequestDto request) {
    Ticket ticket = ticketRepository.findAll()
            .stream()
            .filter(t -> t.getFlight().getFlightNumber().equals(request.getFlightNumber()))
            .filter(t -> request.getDate() == null ||
                    t.getFlight().getDepartureDateTime().toLocalDate().equals(request.getDate()))
            .filter(t -> t.getPassengerName().equals(request.getPassengerName()))
            .findFirst()
            .orElse(null);

    CheckInResponseDto res = new CheckInResponseDto();

    if (ticket == null) {
        res.setTransactionStatus("Ticket not found");
        return res;
    }

    if (ticket.isCheckedIn()) {
        res.setTransactionStatus("Already checked in");
        res.setSeatNumber(ticket.getSeatNumber());
        return res;
    }

    List<Ticket> tickets = ticketRepository.findAll()
            .stream()
            .filter(t -> t.getFlight().getId().equals(ticket.getFlight().getId()))
            .collect(Collectors.toList());

    long count = tickets.stream()
            .filter(Ticket::isCheckedIn)
            .count();

    ticket.setSeatNumber("S" + (count + 1));
    ticket.setCheckedIn(true);
    ticketRepository.save(ticket);

    res.setTransactionStatus("SUCCESS");
    res.setSeatNumber(ticket.getSeatNumber());

    return res;
}

    public Page<PassengerResponseDto> getPassengerList(String flightNumber,
                                                   String date,
                                                   Pageable pageable) {

    List<Ticket> tickets = ticketRepository.findAll()
            .stream()
            .filter(t -> t.getFlight().getFlightNumber().equals(flightNumber))
            .filter(t -> date == null || date.isBlank() ||
                    t.getFlight().getDepartureDateTime().toLocalDate().toString().equals(date))
            .collect(Collectors.toList());

    List<PassengerResponseDto> list = tickets.stream().map(t -> {
        PassengerResponseDto dto = new PassengerResponseDto();
        dto.setPassengerName(t.getPassengerName());
        dto.setSeatNumber(t.getSeatNumber());
        return dto;
    }).collect(Collectors.toList());

    return new PageImpl<>(list, pageable, list.size());
}

    private FlightResponseDto mapToResponse(Flight f) {
        FlightResponseDto dto = new FlightResponseDto();

        dto.setFlightNumber(f.getFlightNumber());
        dto.setAirportFrom(f.getAirportFrom());
        dto.setAirportTo(f.getAirportTo());
        dto.setDepartureDateTime(f.getDepartureDateTime());
        dto.setArrivalDateTime(f.getArrivalDateTime());
        dto.setAvailableSeats(f.getAvailableSeats());

        return dto;
    }
}