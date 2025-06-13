package com.lottofun.controller;


import com.lottofun.domain.Ticket;
import com.lottofun.dto.request.TicketRequest;
import com.lottofun.dto.response.TicketResponse;
import com.lottofun.services.impl.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

//    @PostMapping("/buy/{userId}")
//    public ResponseEntity<Ticket> buyTicket(@PathVariable Long userId,
//                                            @Valid @RequestBody TicketRequest request) {
//        Ticket ticket = ticketService.buyTicket(userId, request);
//        return ResponseEntity.ok(ticket);
//    }

    @PostMapping("/buy/{userId}")
    public ResponseEntity<TicketResponse> buyTicket(@PathVariable Long userId,
                                                    @Valid @RequestBody TicketRequest request) {

        Ticket ticket = ticketService.buyTicket(userId, request);

        // DTO'ya dönüştür
        TicketResponse response = TicketResponse.builder()
                .ticketNumber(ticket.getTicketNumber())
                .purchaseTime(ticket.getPurchaseTime())
                .selectedNumbers(ticket.getSelectedNumbers())
                .status(ticket.getStatus())
                .matchCount(ticket.getMatchCount())
                .prize(ticket.getPrize())
                .drawId(ticket.getDraw().getId())
                .drawDate(ticket.getDraw().getDrawDate())
                .winningNumbers(ticket.getDraw().getWinningNumbers())
                .build();

        return ResponseEntity.ok(response);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TicketResponse>> getTicketsByUser(@PathVariable Long userId) {
        List<TicketResponse> tickets = ticketService.getTicketsByUser(userId);
        return ResponseEntity.ok(tickets);
    }
}
