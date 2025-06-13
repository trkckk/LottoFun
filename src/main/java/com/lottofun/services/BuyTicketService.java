package com.lottofun.services;

import com.lottofun.domain.Ticket;
import com.lottofun.dto.request.TicketRequest;

public interface BuyTicketService {
    Ticket buyTicket(Long userId, TicketRequest request);
}
