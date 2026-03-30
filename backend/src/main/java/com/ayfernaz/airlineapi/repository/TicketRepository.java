package com.ayfernaz.airlineapi.repository;

import com.ayfernaz.airlineapi.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByFlight_Id(Long flightId);

    Ticket findByFlight_FlightNumberAndPassengerName(String flightNumber, String passengerName);

    List<Ticket> findByFlight_FlightNumber(String flightNumber);
}