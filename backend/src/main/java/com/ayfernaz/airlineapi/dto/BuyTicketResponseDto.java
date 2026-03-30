package com.ayfernaz.airlineapi.dto;

import java.util.List;

public class BuyTicketResponseDto {

    private String transactionStatus;
    private List<Long> ticketNumbers;

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public List<Long> getTicketNumbers() {
        return ticketNumbers;
    }

    public void setTicketNumbers(List<Long> ticketNumbers) {
        this.ticketNumbers = ticketNumbers;
    }
}