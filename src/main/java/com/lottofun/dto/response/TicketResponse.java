package com.lottofun.dto.response;


import com.lottofun.enums.TicketStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TicketResponse {

    private String ticketNumber;
    private LocalDateTime purchaseTime;
    private List<Integer> selectedNumbers;

    private TicketStatus status;
    private Integer matchCount;
    private Double prize;

    private Long drawId;
    private LocalDateTime drawDate;
    private List<Integer> winningNumbers;
}